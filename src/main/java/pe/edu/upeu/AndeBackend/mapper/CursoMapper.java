package pe.edu.upeu.AndeBackend.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AndeBackend.dto.CursoRequestDTO;
import pe.edu.upeu.AndeBackend.dto.CursoResponseDTO;
import pe.edu.upeu.AndeBackend.entity.Carrera;
import pe.edu.upeu.AndeBackend.entity.Curso;

@Component
public class CursoMapper {
    public Curso toEntity(CursoRequestDTO r, Carrera carrera){
        Curso curso = new Curso();
        actualizarEntidad(r, curso, carrera);
        return curso;
    }

    public void actualizarEntidad(CursoRequestDTO r, Curso c, Carrera carrera) {
        c.setCodigo(r.getCodigo().trim());
        c.setNombre(r.getNombre().trim());
        c.setCreditos(r.getCreditos());
        c.setCiclo(r.getCiclo());
        c.setVacantes(r.getVacantes());
        c.setEstado(r.getEstado());
        c.setCarrera(carrera);
    }
    public CursoResponseDTO toResponse(Curso c) {
        return new CursoResponseDTO(
                c.getIdCurso(),
                c.getCodigo(),
                c.getNombre(),
                c.getCreditos(),
                c.getCiclo(),
                c.getVacantes(),
                c.getEstado(),
                c.getCarrera().getIdCarrera(),
                c.getCarrera().getNombre(),
                c.getFechaCreacion(),
                c.getFechaModificacion()

        );
    }
}
