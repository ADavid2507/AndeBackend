package pe.edu.upeu.AndeBackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AndeBackend.dto.EstudianteRequestDTO;
import pe.edu.upeu.AndeBackend.dto.EstudianteResponseDTO;
import pe.edu.upeu.AndeBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.AndeBackend.service.service.EstudianteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @PostMapping
    public ResponseEntity<EstudianteResponseDTO> registrar(@Valid @RequestBody EstudianteRequestDTO request) {
        EstudianteResponseDTO nuevoEstudiante = estudianteService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstudiante);
    }

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(estudianteService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estudianteService.read(id));
    }

    @GetMapping("/{id}/matriculas")
    public ResponseEntity<List<MatriculaResponseDTO>> obtenerHistorialMatriculas(
            @PathVariable Long id,
            @RequestParam(name = "periodo", required = false) String periodo) {
        return ResponseEntity.ok(estudianteService.obtenerHistorialMatriculas(id, periodo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstudianteRequestDTO request) {
        return ResponseEntity.ok(estudianteService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estudianteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

