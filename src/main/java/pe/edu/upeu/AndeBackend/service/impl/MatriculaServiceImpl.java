package pe.edu.upeu.AndeBackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AndeBackend.dto.DetalleMatriculaRequestDTO;
import pe.edu.upeu.AndeBackend.dto.MatriculaRequestDTO;
import pe.edu.upeu.AndeBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.AndeBackend.entity.Curso;
import pe.edu.upeu.AndeBackend.entity.DetalleMatricula;
import pe.edu.upeu.AndeBackend.entity.Estudiante;
import pe.edu.upeu.AndeBackend.entity.Matricula;
import pe.edu.upeu.AndeBackend.enums.EstadoMatricula;
import pe.edu.upeu.AndeBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AndeBackend.exception.ReglaNegocioException;
import pe.edu.upeu.AndeBackend.mapper.MatriculaMapper;
import pe.edu.upeu.AndeBackend.repository.CursoRepository;
import pe.edu.upeu.AndeBackend.repository.EstudianteRepository;
import pe.edu.upeu.AndeBackend.repository.MatriculaRepository;
import pe.edu.upeu.AndeBackend.service.service.MatriculaService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;
    private final MatriculaMapper matriculaMapper;

    @Value("${matricula.costo-credito:120.00}")
    private BigDecimal costoCredito;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MatriculaResponseDTO registrar(MatriculaRequestDTO request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            log.warn("Solicitud de matrícula rechazada: no contiene cursos");
            throw new ReglaNegocioException("La matrícula debe contener al menos un curso");
        }

        Estudiante estudiante = estudianteRepository.findById(request.getEstudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + request.getEstudianteId()));

        // RN-01: Solo se matricula un estudiante activo
        if (Boolean.FALSE.equals(estudiante.getEstado())) {
            log.warn("RN-01 violada: Intento de matrícula para estudiante inactivo con ID {}", estudiante.getId());
            throw new ReglaNegocioException("No se puede matricular a un estudiante inactivo");
        }

        // RN-03: Un estudiante no puede tener más de una matrícula en estado REGISTRADA en el mismo periodo
        if (matriculaRepository.existsByEstudianteIdAndPeriodoAndEstado(estudiante.getId(), request.getPeriodo(), EstadoMatricula.REGISTRADA)) {
            log.warn("RN-03 violada: El estudiante con ID {} ya tiene matrícula REGISTRADA en el periodo {}", estudiante.getId(), request.getPeriodo());
            throw new ReglaNegocioException("El estudiante ya cuenta con una matrícula registrada en el periodo " + request.getPeriodo());
        }

        // Validar cursos duplicados en la misma solicitud
        Set<Long> cursosEnSolicitud = new HashSet<>();
        List<Curso> cursosValidados = new ArrayList<>();
        int totalCreditos = 0;

        for (DetalleMatriculaRequestDTO detalleDTO : request.getDetalles()) {
            if (detalleDTO.getCursoId() == null) {
                throw new ReglaNegocioException("El identificador del curso es obligatorio");
            }

            if (!cursosEnSolicitud.add(detalleDTO.getCursoId())) {
                log.warn("Curso duplicado en solicitud de matrícula: {}", detalleDTO.getCursoId());
                throw new ReglaNegocioException("No se puede registrar el mismo curso más de una vez en la matrícula");
            }

            Curso curso = cursoRepository.findById(detalleDTO.getCursoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Curso no encontrado con ID: " + detalleDTO.getCursoId()));

            // RN-01: Cursos activos
            if (Boolean.FALSE.equals(curso.getEstado())) {
                log.warn("RN-01 violada: El curso {} (ID: {}) se encuentra inactivo", curso.getNombre(), curso.getIdCurso());
                throw new ReglaNegocioException("No se puede matricular en el curso inactivo: " + curso.getNombre());
            }

            // RN-01: Cursos que pertenezcan a su propia carrera
            Long idCarreraEstudiante = (estudiante.getCarrera() != null) ? estudiante.getCarrera().getIdCarrera() : null;
            Long idCarreraCurso = (curso.getCarrera() != null) ? curso.getCarrera().getIdCarrera() : null;

            if (idCarreraEstudiante == null || !idCarreraEstudiante.equals(idCarreraCurso)) {
                log.warn("RN-01 violada: El curso {} no pertenece a la carrera del estudiante", curso.getNombre());
                throw new ReglaNegocioException("El curso " + curso.getNombre() + " no pertenece a la carrera del estudiante");
            }

            // RN-02: No se puede matricular en un curso con cero vacantes
            if (curso.getVacantes() == null || curso.getVacantes() <= 0) {
                log.warn("RN-02 violada: El curso {} no cuenta con vacantes disponibles", curso.getNombre());
                throw new ReglaNegocioException("El curso " + curso.getNombre() + " no tiene vacantes disponibles");
            }

            cursosValidados.add(curso);
            totalCreditos += (curso.getCreditos() != null ? curso.getCreditos() : 0);
        }

        // RN-04: Una matrícula no puede superar 20 créditos
        if (totalCreditos > 20) {
            log.warn("RN-04 violada: Créditos acumulados ({}) superan el límite permitido de 20", totalCreditos);
            throw new ReglaNegocioException("Una matrícula no puede superar 20 créditos. Total solicitado: " + totalCreditos);
        }

        BigDecimal factorCostoCredito = (costoCredito != null) ? costoCredito : new BigDecimal("120.00");
        BigDecimal montoTotal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        Matricula matricula = new Matricula();
        matricula.setFecha(LocalDateTime.now());
        matricula.setPeriodo(request.getPeriodo());
        matricula.setEstudiante(estudiante);
        matricula.setEstado(EstadoMatricula.REGISTRADA);
        matricula.setTotalCreditos(totalCreditos);

        for (Curso curso : cursosValidados) {
            // RN-02: Cada curso matriculado descuenta una vacante
            curso.setVacantes(curso.getVacantes() - 1);
            cursoRepository.save(curso);

            // RN-04: El costo de cada curso es créditos × costo por crédito
            BigDecimal creditosBD = BigDecimal.valueOf(curso.getCreditos() != null ? curso.getCreditos() : 0);
            BigDecimal costoCurso = creditosBD.multiply(factorCostoCredito).setScale(2, RoundingMode.HALF_UP);
            montoTotal = montoTotal.add(costoCurso);

            DetalleMatricula detalle = new DetalleMatricula();
            detalle.setCurso(curso);
            detalle.setCreditos(curso.getCreditos());
            detalle.setCosto(costoCurso);
            matricula.agregarDetalle(detalle);
        }

        matricula.setMontoTotal(montoTotal.setScale(2, RoundingMode.HALF_UP));

        Matricula guardada = matriculaRepository.save(matricula);
        log.info("Matrícula registrada exitosamente con ID: {}, estudiante: {}, créditos: {}, monto total: {}",
                guardada.getId(), estudiante.getId(), totalCreditos, guardada.getMontoTotal());

        return matriculaMapper.toResponse(guardada);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MatriculaResponseDTO anular(Long id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matrícula no encontrada con ID: " + id));

        if (EstadoMatricula.ANULADA.equals(matricula.getEstado())) {
            log.warn("Intento de anular una matrícula ya anulada con ID: {}", id);
            throw new ReglaNegocioException("La matrícula ya se encuentra anulada");
        }

        matricula.setEstado(EstadoMatricula.ANULADA);

        // RN-02: La anulación devuelve las vacantes
        if (matricula.getDetalles() != null) {
            for (DetalleMatricula detalle : matricula.getDetalles()) {
                Curso curso = detalle.getCurso();
                if (curso != null) {
                    int vacantesActuales = (curso.getVacantes() != null) ? curso.getVacantes() : 0;
                    curso.setVacantes(vacantesActuales + 1);
                    cursoRepository.save(curso);
                }
            }
        }

        Matricula anulada = matriculaRepository.save(matricula);
        log.info("Matrícula con ID {} anulada exitosamente y vacantes restauradas", id);

        return matriculaMapper.toResponse(anulada);
    }

    @Override
    public MatriculaResponseDTO obtenerPorId(Long id) {
        return matriculaRepository.findById(id)
                .map(matriculaMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matrícula no encontrada con ID: " + id));
    }

    @Override
    public List<MatriculaResponseDTO> listarTodas() {
        return matriculaRepository.findAll().stream()
                .map(matriculaMapper::toResponse)
                .toList();
    }

}
