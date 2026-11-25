package cl.duoc.blog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 500)
    private String resumen;

    @Column(name = "parrafo_1", nullable = false, columnDefinition = "TEXT")
    private String parrafo1;

    @Column(name = "subtitulo_1", nullable = false, length = 200)
    private String subtitulo1;

    @Column(name = "parrafo_2", nullable = false, columnDefinition = "TEXT")
    private String parrafo2;

    @Column(name = "subtitulo_2", nullable = false, length = 200)
    private String subtitulo2;

    @Column(name = "parrafo_3", nullable = false, columnDefinition = "TEXT")
    private String parrafo3;

    @Column(nullable = false, length = 500)
    private String imagen;

    @Column(nullable = false, length = 500)
    private String cita;

    @PrePersist
    protected void onCreate() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}