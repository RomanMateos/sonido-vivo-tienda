package cl.duoc.ventas.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearBoletaExternalRequest {
    private Long ventaId;
    private String compradorNombreCompleto;
    private String compradorEmail;
    private String metodoPago;
    private Integer montoTotal;
    private List<DetalleBoletaExternalRequest> items;
}
