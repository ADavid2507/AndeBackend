package pe.edu.upeu.AndeBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cursos")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idCurso;

    @Column(nullable = false, unique = true, length = 5)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false)
    private Integer creditos;

    @Column(nullable = false)
    private Integer ciclo;

    @Column(nullable = false)
    private Integer vacantes;

    @Column(nullable = false)
    private Boolean estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = true;
        }
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}
