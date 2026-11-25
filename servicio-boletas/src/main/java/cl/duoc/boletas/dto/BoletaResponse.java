package cl.duoc.boletas.dto;

import cl.duoc.boletas.model.EstadoBoleta;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BoletaResponse {
    private Long folio;
    private Long ventaId;
    private LocalDateTime fechaEmision;
    private String compradorNombreCompleto;
    private String compradorEmail;
    private String metodoPago;
    private Integer montoNeto;
    private Integer iva;
    private Integer montoTotal;
    private EstadoBoleta estado;
    private List<DetalleBoletaResponse> detalles;
}
