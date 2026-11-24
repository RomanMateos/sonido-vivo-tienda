package cl.duoc.productos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "marca_id", nullable = false)
    private Marca marca;

    @Column(nullable = false)
    private Integer precio;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false, length = 500)
    private String imagen;

    @Builder.Default
    @Column(name = "en_oferta", nullable = false)
    private Boolean enOferta = false;

    @Builder.Default
    @Column(nullable = false)
    private Integer stock = 0;
}
