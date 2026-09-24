package pe.edu.upeu.AndeBackend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AndeBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.AndeBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.AndeBackend.entity.Carrera;
import pe.edu.upeu.AndeBackend.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AndeBackend.exception.ReglaNegocioException;
import pe.edu.upeu.AndeBackend.mapper.CarreraMapper;
import pe.edu.upeu.AndeBackend.repository.CarreraRepository;
import pe.edu.upeu.AndeBackend.service.service.CarreraService;

import java.util.List;

@Service
public class CarreraServiceImpl implements CarreraService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CarreraServiceImpl.class);


    private final CarreraRepository carreraRepository;
    private final CarreraMapper carreraMapper;

    public CarreraServiceImpl(CarreraRepository carreraRepository, CarreraMapper carreraMapper) {
        this.carreraRepository = carreraRepository;
        this.carreraMapper = carreraMapper;
    }

    @Override
    @Transactional
    public CarreraResponseDTO create(CarreraRequestDTO request) {
        LOGGER.info("Creando una nueva carrera");

        String nombre = request.getNombre().trim();
        if (carreraRepository.existsByNombreIgnoreCase(nombre)) {
            throw new ReglaNegocioException("Ya existe una carrera con el nombre: " + nombre + "");
        }
        Carrera carrera = carreraMapper.toEntity(request);
        Carrera carreraGuardada = carreraRepository.save(carrera);

        LOGGER.info("Carrear creada: " + carreraGuardada.getIdCarrera() + " con nombre: " + carreraGuardada.getNombre() + "");
        return carreraMapper.toResponse(carreraGuardada);
    }

    @Override
    @Transactional
    public CarreraResponseDTO update(Long aLong, CarreraRequestDTO request) {
        LOGGER.info("Actualizando una carrera");

        Carrera carrera = carreraRepository.findById(aLong).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe una carrera con el id: " + aLong + "")
        );

        String nombre = request.getNombre().trim();

        if (carreraRepository.existsByNombreIgnoreCaseAndIdCarreraNot(nombre, aLong)){
            throw new ReglaNegocioException("Ya existe una carrera con el nombre: " + nombre + "");
        }
        carreraMapper.actualizarEntidad(request, carrera);

        Carrera carreraActualizada = carreraRepository.save(carrera);

        LOGGER.info("Carrera actualizada: " + carreraActualizada.getIdCarrera() + " con nombre: " + carreraActualizada.getNombre() + "");
        return carreraMapper.toResponse(carreraActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public CarreraResponseDTO read(Long aLong) {
        LOGGER.info("Buscando una carrera por id");
        Carrera carrera = carreraRepository.findById(aLong).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe una carrera con el id: " + aLong + "")
        );
        LOGGER.info("Carrera encontrada: " + carrera.getIdCarrera() + " con nombre: " + carrera.getNombre() + "");
        return carreraMapper.toResponse(carrera);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> readAll() {
        LOGGER.info("Buscando todas las carreras");
        return carreraRepository.findAll().stream().map(carreraMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void delete(Long aLong) {
        LOGGER.info("Eliminando una carrera");
        Carrera carrera = carreraRepository.findById(aLong).orElseThrow(
                () -> new RecursoNoEncontradoException("No existe una carrera con el id: " + aLong + "")
        );

        carreraRepository.delete(carrera);
        LOGGER.info("Carrera eliminada: " + carrera.getIdCarrera() + " con nombre: " + carrera.getNombre() + "");
    }
}
