package pe.edu.upeu.AndeBackend.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AndeBackend.dto.EstudianteResponseDTO;
import pe.edu.upeu.AndeBackend.entity.Estudiante;

@Component
public class EstudianteMapper {

    public EstudianteResponseDTO toResponse(Estudiante estudiante) {
        if (estudiante == null) {
            return null;
        }

        EstudianteResponseDTO response = new EstudianteResponseDTO();
        response.setId(estudiante.getId());
        response.setCodigo(estudiante.getCodigo());
        response.setDni(estudiante.getDni());
        response.setNombres(estudiante.getNombres());
        response.setApellidos(estudiante.getApellidos());
        response.setEmail(estudiante.getEmail());
        response.setEstado(estudiante.getEstado());

        if (estudiante.getCarrera() != null) {
            response.setCarreraId(estudiante.getCarrera().getIdCarrera());
            response.setCarreraNombre(estudiante.getCarrera().getNombre());
        }

        response.setFechaCreacion(estudiante.getFechaCreacion());
        response.setFechaModificacion(estudiante.getFechaModificacion());

        return response;
    }
}
