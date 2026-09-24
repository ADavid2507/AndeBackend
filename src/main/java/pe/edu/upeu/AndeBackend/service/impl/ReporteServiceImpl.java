package pe.edu.upeu.AndeBackend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AndeBackend.dto.MatriculadosPorCursoDTO;
import pe.edu.upeu.AndeBackend.dto.RecaudacionPorCarreraDTO;
import pe.edu.upeu.AndeBackend.repository.MatriculaRepository;
import pe.edu.upeu.AndeBackend.service.service.ReporteService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final MatriculaRepository matriculaRepository;

    @Override
    public List<MatriculadosPorCursoDTO> obtenerReporteMatriculadosPorCurso(String periodo, Long carreraId) {
        log.info("Generando reporte de matriculados por curso para el periodo: '{}' y carrera ID: '{}'", periodo, carreraId);
        String periodoFiltro = (periodo != null && !periodo.trim().isEmpty()) ? periodo.trim() : null;
        return matriculaRepository.obtenerReporteMatriculadosPorCurso(periodoFiltro, carreraId);
    }

    @Override
    public List<RecaudacionPorCarreraDTO> obtenerReporteRecaudacionPorCarrera(String periodo) {
        log.info("Generando reporte de recaudación por carrera para el periodo: '{}'", periodo);
        String periodoFiltro = (periodo != null && !periodo.trim().isEmpty()) ? periodo.trim() : null;
        return matriculaRepository.obtenerReporteRecaudacionPorCarrera(periodoFiltro);
    }
}

