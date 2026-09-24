package pe.edu.upeu.AndeBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AndeBackend.entity.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante,Long> {
    boolean existsByCodigo(String codigo);
    boolean existsByDni(String dni);
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByCodigoAndIdNot(String codigo, Long id);
    boolean existsByDniAndIdNot(String dni, Long id);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
