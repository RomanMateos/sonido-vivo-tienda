package cl.duoc.productos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimientoStockRequest {

    @NotNull(message = "La operación (DESCONTAR o REPONER) es obligatoria")
    private TipoOperacionStock operacion;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
