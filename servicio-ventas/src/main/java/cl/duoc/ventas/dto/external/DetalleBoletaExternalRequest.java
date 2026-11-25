package cl.duoc.ventas.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleBoletaExternalRequest {
    private String productoId;
    private String nombreProducto;
    private Integer precioUnitario;
    private Integer cantidad;
    private Integer subtotal;
}