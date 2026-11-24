package cl.duoc.ventas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comprador_nombre", nullable = false, length = 100)
    private String compradorNombre;

    @Column(name = "comprador_apellidos", nullable = false, length = 100)
    private String compradorApellidos;

    @Column(name = "comprador_email", nullable = false, length = 120)
    private String compradorEmail;

    @Column(name = "comprador_region", nullable = false, length = 100)
    private String compradorRegion;

    @Column(name = "comprador_comuna", nullable = false, length = 100)
    private String compradorComuna;

    @Column(name = "comprador_direccion", nullable = false, length = 200)
    private String compradorDireccion;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_despacho", nullable = false, length = 30)
    private TipoDespacho tipoDespacho;

    @Column(name = "lugar_despacho", nullable = false, length = 250)
    private String lugarDespacho;

    @Column(nullable = false)
    private Integer total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoVenta estado;

    @Builder.Default
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }
}
