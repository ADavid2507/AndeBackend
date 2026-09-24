package pe.edu.upeu.AndeBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CursoResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private Integer creditos;
    private Integer ciclo;
    private Integer vacantes;
    private Boolean estado;
    private pe.edu.upeu.AndeBackend.enums.ModalidadCurso modalidad;
    private Long carreraId;
    private String carreraNombre;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}

