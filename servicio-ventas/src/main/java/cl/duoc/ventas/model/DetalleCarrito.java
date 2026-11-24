package cl.duoc.ventas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "detalle_carrito",
        uniqueConstraints = @UniqueConstraint(name = "uk_carrito_producto", columnNames = {"carrito_id", "producto_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "carrito_id", nullable = false)
    private Carrito carrito;

    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    @Column(name = "nombre_producto", nullable = false, length = 150)
    private String nombreProducto;

    @Column(name = "precio_unitario", nullable = false)
    private Integer precioUnitario;

    @Column(nullable = false, length = 500)
    private String imagen;

    @Column(nullable = false)
    private Integer cantidad;
}