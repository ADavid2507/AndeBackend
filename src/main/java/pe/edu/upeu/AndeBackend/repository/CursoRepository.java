package pe.edu.upeu.AndeBackend.repository;

import org.springframework.data.repository.CrudRepository;
import pe.edu.upeu.AndeBackend.entity.Curso;

public interface CursoRepository extends CrudRepository<Curso, Long> {
    boolean existsByNombreCursoIgnoreCase(String nombreCurso);
    boolean existsByNombreCursoIgnoreCaseAndIdCursoNot(String nombreCurso, Long idCurso);

    boolean existsByCodigoCursoIgnoreCase(String codigoCurso);
    boolean existsByCodigoCursoIgnoreCaseAndIdCursoNot(String codigoCurso, Long idCurso);
}
