package pe.edu.upeu.AndeBackend.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AndeBackend.dto.CarreraRequestDTO;
import pe.edu.upeu.AndeBackend.dto.CarreraResponseDTO;
import pe.edu.upeu.AndeBackend.entity.Carrera;

@Component
public class CarreraMapper {
    public Carrera toEntity(CarreraRequestDTO r){
        Carrera carrera = new Carrera();
        actualizarEntidad(r, carrera);
        return carrera;
    }

    public void actualizarEntidad(CarreraRequestDTO r, Carrera c){
        c.setNombre(r.getNombre().trim());
        c.setDescripcion(r.getDescripcion().trim());
        c.setEstado(r.getEstado());
    }

    public CarreraResponseDTO toResponse(Carrera c){
        return new CarreraResponseDTO(
                c.getIdCarrera(),
                c.getNombre(),
                c.getDescripcion(),
                c.getEstado(),
                c.getFechaCreacion(),
                c.getFechaModificacion()
        );
    }
}
