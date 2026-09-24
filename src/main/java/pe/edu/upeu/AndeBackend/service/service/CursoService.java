package pe.edu.upeu.AndeBackend.service.service;

import pe.edu.upeu.AndeBackend.dto.CursoRequestDTO;
import pe.edu.upeu.AndeBackend.dto.CursoResponseDTO;
import pe.edu.upeu.AndeBackend.enums.ModalidadCurso;
import pe.edu.upeu.AndeBackend.service.generic.CrudService;

import java.util.List;

public interface CursoService extends CrudService<CursoRequestDTO, CursoResponseDTO, Long> {
    List<CursoResponseDTO> buscar(
            String nombre,
            Long carreraId,
            Integer ciclo,
            Boolean conVacantes,
            ModalidadCurso modalidad,
            String orden,
            String dir
    );
}

