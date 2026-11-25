package cl.duoc.ventas.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoStockExternalRequest {
    private String operacion;
    private Integer cantidad;
}
