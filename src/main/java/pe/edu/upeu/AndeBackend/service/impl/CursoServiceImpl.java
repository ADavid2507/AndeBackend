package pe.edu.upeu.AndeBackend.service.impl;

import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.edu.upeu.AndeBackend.dto.CursoRequestDTO;
import pe.edu.upeu.AndeBackend.dto.CursoResponseDTO;
import pe.edu.upeu.AndeBackend.entity.Carrera;
import pe.edu.upeu.AndeBackend.entity.Curso;
import pe.edu.upeu.AndeBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AndeBackend.exception.ReglaNegocioException;
import pe.edu.upeu.AndeBackend.mapper.CursoMapper;
import pe.edu.upeu.AndeBackend.repository.CarreraRepository;
import pe.edu.upeu.AndeBackend.repository.CursoRepository;
import pe.edu.upeu.AndeBackend.service.service.CursoService;

import java.util.List;

@Service
public class CursoServiceImpl implements CursoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CursoServiceImpl.class);

    private final CursoRepository cursoRepository;
    private final CarreraRepository carreraRepository;
    private final CursoMapper cursoMapper;

    public CursoServiceImpl(CursoRepository cursoRepository, CarreraRepository carreraRepository, CursoMapper cursoMapper) {
        this.cursoRepository = cursoRepository;
        this.carreraRepository = carreraRepository;
        this.cursoMapper = cursoMapper;
    }


    @Override
    @Transactional
    public CursoResponseDTO create(CursoRequestDTO request) {
        LOGGER.info("Creando un nuevo curso");

        String nombre = request.getNombre().trim();
        String codigo = request.getCodigo().trim();
        Carrera carrera = carreraRepository.findById(request.getCarreraId()).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe una carrera con el id: " + request.getCarreraId() + "")
        );
        if (cursoRepository.existsByNombreIgnoreCase(nombre)){
            throw new ReglaNegocioException("Ya existe un curso con el nombre: " + nombre + "");
        }
        if (cursoRepository.existsByCodigoIgnoreCase(codigo)){
            throw new ReglaNegocioException("Ya existe un curso con el codigo: " + codigo + "");
        }

        Curso curso = cursoMapper.toEntity(request, carrera);

        Curso cursoGuardado = cursoRepository.save(curso);

        LOGGER.info("Curso creado: " + cursoGuardado.getIdCurso() + " con nombre: " + cursoGuardado.getNombre() + "");
        return cursoMapper.toResponse(cursoGuardado);
    }

    @Override
    @Transactional
    public CursoResponseDTO update(Long aLong, CursoRequestDTO request) {
        LOGGER.info("Actualizando un curso");

        Curso curso = cursoRepository.findById(aLong).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe un curso con el id: " + aLong + "")
        );
        String nombre = request.getNombre().trim();
        String codigo = request.getCodigo().trim();
        Carrera carrera = carreraRepository.findById(request.getCarreraId()).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe una carrera con el id: " + request.getCarreraId() + "")
        );

        if (cursoRepository.existsByNombreIgnoreCaseAndIdCursoNot(nombre, aLong)){
            throw new ReglaNegocioException("Ya existe un curso con el nombre: " + nombre + "");
        }
        if (cursoRepository.existsByCodigoIgnoreCaseAndIdCursoNot(codigo, aLong)){
            throw new ReglaNegocioException("Ya existe un curso con el codigo: " + codigo + "");
        }
        cursoMapper.actualizarEntidad(request, curso, carrera);
        Curso cursoActualizado = cursoRepository.save(curso);
        LOGGER.info("Curso actualizado: " + cursoActualizado.getIdCurso() + " con nombre: " + cursoActualizado.getNombre() + "");
        return cursoMapper.toResponse(cursoActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponseDTO read(Long aLong) {
        LOGGER.info("Buscando un curso por id");
        Curso curso = cursoRepository.findById(aLong).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe un curso con el id: " + aLong + "")
        );
        LOGGER.info("Curso encontrado: " + curso.getIdCurso() + " con nombre: " + curso.getNombre() + "");
        return cursoMapper.toResponse(curso);

    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponseDTO> readAll() {
        return cursoRepository.findAll().stream().map(cursoMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void delete(Long aLong) {
        LOGGER.info("Eliminando un curso");
        Curso curso = cursoRepository.findById(aLong).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe un curso con el id: " + aLong + "")
        );
        cursoRepository.delete(curso);
        LOGGER.info("Curso eliminado: " + curso.getIdCurso() + " con nombre: " + curso.getNombre() + "");
    }
}
