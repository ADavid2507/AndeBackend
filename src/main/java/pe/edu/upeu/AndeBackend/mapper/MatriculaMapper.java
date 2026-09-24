package pe.edu.upeu.AndeBackend.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AndeBackend.dto.DetalleMatriculaResponseDTO;
import pe.edu.upeu.AndeBackend.dto.MatriculaResponseDTO;
import pe.edu.upeu.AndeBackend.entity.DetalleMatricula;
import pe.edu.upeu.AndeBackend.entity.Matricula;

import java.util.ArrayList;
import java.util.List;

@Component
public class MatriculaMapper {

    public MatriculaResponseDTO toResponse(Matricula matricula) {
        if (matricula == null) {
            return null;
        }

        MatriculaResponseDTO response = new MatriculaResponseDTO();
        response.setId(matricula.getId());
        response.setFecha(matricula.getFecha());
        response.setPeriodo(matricula.getPeriodo());

        if (matricula.getEstudiante() != null) {
            response.setEstudianteId(matricula.getEstudiante().getId());
            response.setEstudianteCodigo(matricula.getEstudiante().getCodigo());
            String nombreCompleto = (matricula.getEstudiante().getNombres() != null ? matricula.getEstudiante().getNombres() : "")
                    + (matricula.getEstudiante().getApellidos() != null ? " " + matricula.getEstudiante().getApellidos() : "");
            response.setEstudianteNombre(nombreCompleto.trim());
        }

        if (matricula.getEstado() != null) {
            response.setEstado(matricula.getEstado().name());
        }

        response.setTotalCreditos(matricula.getTotalCreditos());
        response.setMontoTotal(matricula.getMontoTotal());

        List<DetalleMatriculaResponseDTO> detallesDTO = new ArrayList<>();
        if (matricula.getDetalles() != null) {
            for (DetalleMatricula detalle : matricula.getDetalles()) {
                DetalleMatriculaResponseDTO dDto = new DetalleMatriculaResponseDTO();
                dDto.setId(detalle.getId());
                if (detalle.getCurso() != null) {
                    dDto.setCursoId(detalle.getCurso().getIdCurso());
                    dDto.setCursoCodigo(detalle.getCurso().getCodigo());
                    dDto.setCursoNombre(detalle.getCurso().getNombre());
                }
                dDto.setCreditos(detalle.getCreditos());
                dDto.setCosto(detalle.getCosto());
                detallesDTO.add(dDto);
            }
        }
        response.setDetalles(detallesDTO);

        response.setFechaCreacion(matricula.getFechaCreacion());
        response.setFechaModificacion(matricula.getFechaModificacion());

        return response;
    }
}
