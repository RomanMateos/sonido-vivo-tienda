package cl.duoc.ventas.dto;

import cl.duoc.ventas.model.EstadoVenta;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarEstadoVentaRequest {

    @NotNull(message = "El nuevo estado de la venta es obligatorio")
    private EstadoVenta estado;
}
