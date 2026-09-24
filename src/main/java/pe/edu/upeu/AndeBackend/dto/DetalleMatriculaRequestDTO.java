package pe.edu.upeu.AndeBackend.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleMatriculaRequestDTO {
    @NotNull(message = "El curso es obligatorio")
    @Positive(message = "El identificador del curso debe ser válido")
    private Long cursoId;
}
