package pe.edu.upeu.AndeBackend.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.AndeBackend.entity.Curso;
import pe.edu.upeu.AndeBackend.enums.ModalidadCurso;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNombreIgnoreCase(String nombreCurso);
    boolean existsByNombreIgnoreCaseAndIdCursoNot(String nombreCurso, Long idCurso);

    boolean existsByCodigoIgnoreCase(String codigoCurso);
    boolean existsByCodigoIgnoreCaseAndIdCursoNot(String codigoCurso, Long idCurso);

    @Query("""
        SELECT c FROM Curso c
        WHERE (:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
          AND (:carreraId IS NULL OR c.carrera.idCarrera = :carreraId)
          AND (:ciclo IS NULL OR c.ciclo = :ciclo)
          AND (:conVacantes IS NULL OR (:conVacantes = true AND c.vacantes > 0) OR (:conVacantes = false AND c.vacantes = 0))
          AND (:modalidad IS NULL OR c.modalidad = :modalidad)
    """)
    List<Curso> buscarCursos(
            @Param("nombre") String nombre,
            @Param("carreraId") Long carreraId,
            @Param("ciclo") Integer ciclo,
            @Param("conVacantes") Boolean conVacantes,
            @Param("modalidad") ModalidadCurso modalidad,
            Sort sort
    );
}

