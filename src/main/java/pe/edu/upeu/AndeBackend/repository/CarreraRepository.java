package pe.edu.upeu.AndeBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AndeBackend.entity.Carrera;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    boolean existsByNombreIgnoreCase(String nombreCarrera);
    boolean existsByNombreIgnoreCaseAndIdCarreraNot(String nombreCarrera, Long idCarrera);

}
