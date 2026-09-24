package pe.edu.upeu.AndeBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaRequestDTO {
    @NotNull(message = "El estudiante es obligatorio")
    @Positive(message = "El identificador del estudiante debe ser válido")
    private Long estudianteId;
    @NotBlank(message = "El periodo es obligatorio")
    @Pattern(
            regexp = "^\\d{4}-[12]$",
            message = "El periodo debe tener el formato YYYY-1 o YYYY-2 (ej. 2026-2)"
    )
    private String periodo;
    @NotEmpty(message = "La matrícula debe contener al menos un curso")
    @Valid
    private List<DetalleMatriculaRequestDTO> detalles;
}
