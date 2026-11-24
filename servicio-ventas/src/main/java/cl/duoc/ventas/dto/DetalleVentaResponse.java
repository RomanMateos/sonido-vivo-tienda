package cl.duoc.ventas.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetalleVentaResponse {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Integer precioUnitario;
    private Integer cantidad;
    private Integer subtotal;
}