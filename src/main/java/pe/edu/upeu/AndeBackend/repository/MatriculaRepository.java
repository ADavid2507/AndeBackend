package pe.edu.upeu.AndeBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AndeBackend.entity.Matricula;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.AndeBackend.dto.MatriculadosPorCursoDTO;
import pe.edu.upeu.AndeBackend.enums.EstadoMatricula;
import java.util.List;
@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    boolean existsByEstudianteIdAndPeriodoAndEstado(Long estudianteId, String periodo, EstadoMatricula estado);

    @Query("""
        SELECT d.curso.codigo AS codigo,
               d.curso.nombre AS curso,
               COUNT(d.id) AS matriculados,
               SUM(d.costo) AS montoRecaudado
        FROM Matricula m
        JOIN m.detalles d
        WHERE m.estado = pe.edu.upeu.AndeBackend.enums.EstadoMatricula.REGISTRADA
          AND (:periodo IS NULL OR m.periodo = :periodo)
          AND (:carreraId IS NULL OR d.curso.carrera.idCarrera = :carreraId)
        GROUP BY d.curso.codigo, d.curso.nombre
        ORDER BY d.curso.codigo ASC
    """)
    List<MatriculadosPorCursoDTO> obtenerReporteMatriculadosPorCurso(
            @Param("periodo") String periodo,
            @Param("carreraId") Long carreraId
    );
}
