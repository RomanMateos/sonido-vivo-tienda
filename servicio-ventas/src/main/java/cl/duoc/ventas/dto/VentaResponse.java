package cl.duoc.ventas.dto;

import cl.duoc.ventas.model.EstadoVenta;
import cl.duoc.ventas.model.TipoDespacho;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class VentaResponse {
    private Long id;
    private String compradorNombre;
    private String compradorApellidos;
    private String compradorEmail;
    private String compradorRegion;
    private String compradorComuna;
    private String compradorDireccion;
    private LocalDateTime fecha;
    private TipoDespacho tipoDespacho;
    private String lugarDespacho;
    private Integer total;
    private EstadoVenta estado;
    private List<DetalleVentaResponse> detalles;
}