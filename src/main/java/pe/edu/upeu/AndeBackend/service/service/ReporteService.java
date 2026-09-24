package pe.edu.upeu.AndeBackend.service.service;

import pe.edu.upeu.AndeBackend.dto.MatriculadosPorCursoDTO;
import pe.edu.upeu.AndeBackend.dto.RecaudacionPorCarreraDTO;

import java.util.List;

public interface ReporteService {
    List<MatriculadosPorCursoDTO> obtenerReporteMatriculadosPorCurso(String periodo, Long carreraId);
    List<RecaudacionPorCarreraDTO> obtenerReporteRecaudacionPorCarrera(String periodo);
}

