package pe.edu.upeu.AndeBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AndeBackend.entity.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    boolean existsByNombreIgnoreCase(String nombreCurso);
    boolean existsByNombreIgnoreCaseAndIdCursoNot(String nombreCurso, Long idCurso);

    boolean existsByCodigoIgnoreCase(String codigoCurso);
    boolean existsByCodigoIgnoreCaseAndIdCursoNot(String codigoCurso, Long idCurso);
}
