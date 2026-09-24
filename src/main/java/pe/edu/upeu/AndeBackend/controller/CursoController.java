package pe.edu.upeu.AndeBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AndeBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.AndeBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.AndeBackend.dto.CursoRequestDTO;
import pe.edu.upeu.AndeBackend.dto.CursoResponseDTO;
import pe.edu.upeu.AndeBackend.service.service.CursoService;

import java.util.List;

@RestController
@RequestMapping({"/api/v1/cursos", "/api/v1/curso"})
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> getAllCarreras() {
        return ResponseEntity.ok(cursoService.readAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CursoResponseDTO>> buscar(
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "carreraId", required = false) Long carreraId,
            @RequestParam(name = "ciclo", required = false) Integer ciclo,
            @RequestParam(name = "conVacantes", required = false) Boolean conVacantes,
            @RequestParam(name = "modalidad", required = false) pe.edu.upeu.AndeBackend.enums.ModalidadCurso modalidad,
            @RequestParam(name = "orden", required = false, defaultValue = "nombre") String orden,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") String dir) {
        return ResponseEntity.ok(cursoService.buscar(nombre, carreraId, ciclo, conVacantes, modalidad, orden, dir));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> getCarreraById(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.read(id));
    }


    @PostMapping
    public ResponseEntity<CursoResponseDTO> create(@Valid @RequestBody CursoRequestDTO r){
        CursoResponseDTO c = cursoService.create(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CursoRequestDTO r){
        CursoResponseDTO c = cursoService.update(id, r);
        return ResponseEntity.ok(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
