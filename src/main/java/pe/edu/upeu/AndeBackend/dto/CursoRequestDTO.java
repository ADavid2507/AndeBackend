package pe.edu.upeu.AndeBackend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CursoRequestDTO {
    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^[A-Z]{2}\\d{3}$", message = "El código debe tener dos letras mayúsculas y tres dígitos")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "Los créditos deben ser como mínimo 1")
    @Max(value = 6, message = "Los créditos no deben superar 6")
    private Integer creditos;

    @NotNull(message = "El ciclo es obligatorio")
    @Min(value = 1, message = "El ciclo debe ser como mínimo 1")
    @Max(value = 10, message = "El ciclo no debe superar 10")
    private Integer ciclo;

    @NotNull(message = "Las vacantes son obligatorias")
    @PositiveOrZero(message = "Las vacantes deben ser mayores o iguales a 0")
    private Integer vacantes;

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;

    @NotNull(message = "La carrera es obligatoria")
    @Positive(message = "El id de la carrera debe ser mayor a 0")
    private Long carreraId;
}
