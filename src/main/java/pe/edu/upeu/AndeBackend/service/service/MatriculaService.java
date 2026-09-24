package pe.edu.upeu.AndeBackend.service.service;

import pe.edu.upeu.AndeBackend.dto.MatriculaRequestDTO;
import pe.edu.upeu.AndeBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.AndeBackend.service.generic.CrudService;

import java.util.List;

public interface MatriculaService{

    MatriculaResponseDTO registrar(MatriculaRequestDTO request);

    MatriculaResponseDTO anular(Long id);

    MatriculaResponseDTO obtenerPorId(Long id);

    List<MatriculaResponseDTO> listarTodas();
}
