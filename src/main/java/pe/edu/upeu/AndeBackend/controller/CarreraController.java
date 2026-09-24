package pe.edu.upeu.AndeBackend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.AndeBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.AndeBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.AndeBackend.entity.Carrera;
import pe.edu.upeu.AndeBackend.service.service.CarreraService;

@RestController
@RequestMapping("/api/v1/carrera")
public class CarreraController {
    private final CarreraService carreraService;

    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity<Iterable<CarreraResponseDTO>> getAllCarreras() {
        return ResponseEntity.ok(carreraService.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> getCarreraById(Long id) {
        return ResponseEntity.ok(carreraService.read(id));
    }

    @PostMapping
    public ResponseEntity<CarreraResponseDTO> create(@Valid @RequestBody CarreraRequestDTO r){
        CarreraResponseDTO c = carreraService.create(r);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarreraResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CarreraRequestDTO r){
        CarreraResponseDTO c = carreraService.update(id, r);
        return ResponseEntity.ok(c);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        carreraService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
