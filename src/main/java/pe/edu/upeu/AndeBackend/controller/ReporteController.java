package pe.edu.upeu.AndeBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.AndeBackend.dto.MatriculadosPorCursoDTO;
import pe.edu.upeu.AndeBackend.service.service.ReporteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/matriculados-por-curso")
    public ResponseEntity<List<MatriculadosPorCursoDTO>> obtenerReporteMatriculadosPorCurso(
            @RequestParam(name = "periodo", required = false) String periodo,
            @RequestParam(name = "carreraId", required = false) Long carreraId) {
        List<MatriculadosPorCursoDTO> reporte = reporteService.obtenerReporteMatriculadosPorCurso(periodo, carreraId);
        return ResponseEntity.ok(reporte);
    }
}
