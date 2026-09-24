package pe.edu.upeu.AndeBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

public class HealthController {
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health(){
        Map<String, String> response =
                Map.of("STATUS", "UP",
                        "mesagge", "BackEnd funcionando correctamente");

        return ResponseEntity.ok(response);
    }
}
