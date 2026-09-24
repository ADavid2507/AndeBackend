package pe.edu.upeu.AndeBackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AndeBackend.dto.EstudianteRequestDTO;
import pe.edu.upeu.AndeBackend.dto.EstudianteResponseDTO;
import pe.edu.upeu.AndeBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.AndeBackend.entity.Carrera;
import pe.edu.upeu.AndeBackend.entity.Estudiante;
import pe.edu.upeu.AndeBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AndeBackend.exception.ReglaNegocioException;
import pe.edu.upeu.AndeBackend.mapper.EstudianteMapper;
import pe.edu.upeu.AndeBackend.mapper.MatriculaMapper;
import pe.edu.upeu.AndeBackend.repository.CarreraRepository;
import pe.edu.upeu.AndeBackend.repository.EstudianteRepository;
import pe.edu.upeu.AndeBackend.repository.MatriculaRepository;
import pe.edu.upeu.AndeBackend.service.service.EstudianteService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final EstudianteMapper estudianteMapper;
    private final MatriculaRepository matriculaRepository;
    private final MatriculaMapper matriculaMapper;

    @Override
    @Transactional
    public EstudianteResponseDTO create(EstudianteRequestDTO request) {
        if (estudianteRepository.existsByCodigo(request.getCodigo())) {
            log.warn("Intento de registro con código duplicado: {}", request.getCodigo());
            throw new ReglaNegocioException("El código ya se encuentra registrado");
        }

        if (estudianteRepository.existsByDni(request.getDni())) {
            log.warn("Intento de registro con DNI duplicado: {}", request.getDni());
            throw new ReglaNegocioException("El DNI ya se encuentra registrado");
        }

        if (estudianteRepository.existsByEmailIgnoreCase(request.getEmail())) {
            log.warn("Intento de registro con email duplicado: {}", request.getEmail());
            throw new ReglaNegocioException("El email ya se encuentra registrado");
        }

        Carrera carrera = carreraRepository.findById(request.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + request.getCarreraId()));

        Estudiante estudiante = new Estudiante();
        estudiante.setCodigo(request.getCodigo());
        estudiante.setDni(request.getDni());
        estudiante.setNombres(request.getNombres());
        estudiante.setApellidos(request.getApellidos());
        estudiante.setEmail(request.getEmail());
        estudiante.setEstado(request.getEstado() != null ? request.getEstado() : true);
        estudiante.setCarrera(carrera);

        Estudiante guardado = estudianteRepository.save(estudiante);
        log.info("Estudiante registrado exitosamente con ID: {}", guardado.getId());

        return estudianteMapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public EstudianteResponseDTO update(Long id, EstudianteRequestDTO request) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));

        if (estudianteRepository.existsByCodigoAndIdNot(request.getCodigo(), id)) {
            log.warn("Intento de actualización con código duplicado: {}", request.getCodigo());
            throw new ReglaNegocioException("El código ya se encuentra registrado por otro estudiante");
        }

        if (estudianteRepository.existsByDniAndIdNot(request.getDni(), id)) {
            log.warn("Intento de actualización con DNI duplicado: {}", request.getDni());
            throw new ReglaNegocioException("El DNI ya se encuentra registrado por otro estudiante");
        }

        if (estudianteRepository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
            log.warn("Intento de actualización con email duplicado: {}", request.getEmail());
            throw new ReglaNegocioException("El email ya se encuentra registrado por otro estudiante");
        }

        Carrera carrera = carreraRepository.findById(request.getCarreraId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Carrera no encontrada con ID: " + request.getCarreraId()));

        estudiante.setCodigo(request.getCodigo());
        estudiante.setDni(request.getDni());
        estudiante.setNombres(request.getNombres());
        estudiante.setApellidos(request.getApellidos());
        estudiante.setEmail(request.getEmail());
        estudiante.setEstado(request.getEstado() != null ? request.getEstado() : true);
        estudiante.setCarrera(carrera);

        Estudiante actualizado = estudianteRepository.save(estudiante);
        log.info("Estudiante actualizado exitosamente con ID: {}", actualizado.getId());

        return estudianteMapper.toResponse(actualizado);
    }

    @Override
    public EstudianteResponseDTO read(Long id) {
        return estudianteRepository.findById(id)
                .map(estudianteMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));
    }

    @Override
    public List<EstudianteResponseDTO> readAll() {
        return estudianteRepository.findAll().stream()
                .map(estudianteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));

        estudianteRepository.delete(estudiante);
        log.info("Estudiante eliminado exitosamente con ID: {}", id);
    }

    @Override
    public List<MatriculaResponseDTO> obtenerHistorialMatriculas(Long estudianteId, String periodo) {
        if (!estudianteRepository.existsById(estudianteId)) {
            log.warn("Consulta de historial fallida: estudiante no encontrado con ID: {}", estudianteId);
            throw new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + estudianteId);
        }

        String periodoFiltro = (periodo != null && !periodo.trim().isEmpty()) ? periodo.trim() : null;
        log.info("Consultando historial de matrículas para estudiante ID: {} y periodo: '{}'", estudianteId, periodoFiltro);

        return matriculaRepository.findByEstudianteIdAndPeriodoOrderByFechaDesc(estudianteId, periodoFiltro)
                .stream()
                .map(matriculaMapper::toResponse)
                .toList();
    }
}

