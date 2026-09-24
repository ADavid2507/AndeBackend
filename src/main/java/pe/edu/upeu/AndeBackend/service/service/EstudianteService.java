package pe.edu.upeu.AndeBackend.service.service;

import pe.edu.upeu.AndeBackend.dto.EstudianteRequestDTO;
import pe.edu.upeu.AndeBackend.dto.EstudianteResponseDTO;
import pe.edu.upeu.AndeBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.AndeBackend.service.generic.CrudService;

import java.util.List;

public interface EstudianteService extends CrudService<EstudianteRequestDTO, EstudianteResponseDTO, Long> {
    List<MatriculaResponseDTO> obtenerHistorialMatriculas(Long estudianteId, String periodo);
}


