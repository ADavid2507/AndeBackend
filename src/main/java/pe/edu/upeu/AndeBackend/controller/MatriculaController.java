package pe.edu.upeu.AndeBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AndeBackend.dto.MatriculaRequestDTO;
import pe.edu.upeu.AndeBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.AndeBackend.service.service.MatriculaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> registrar(@Valid @RequestBody MatriculaRequestDTO request) {
        MatriculaResponseDTO nuevaMatricula = matriculaService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMatricula);
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(matriculaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<MatriculaResponseDTO> anular(@PathVariable Long id) {
        return ResponseEntity.ok(matriculaService.anular(id));
    }

    @DeleteMapping("/{id}/cursos/{cursoId}")
    public ResponseEntity<MatriculaResponseDTO> retirarCurso(
            @PathVariable Long id,
            @PathVariable Long cursoId) {
        return ResponseEntity.ok(matriculaService.retirarCurso(id, cursoId));
    }
}

