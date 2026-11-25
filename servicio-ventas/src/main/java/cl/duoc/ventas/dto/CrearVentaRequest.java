package cl.duoc.ventas.dto;

import cl.duoc.ventas.model.TipoDespacho;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearVentaRequest {

    @NotNull(message = "El tipo de despacho es obligatorio (DOMICILIO o RETIRO_TIENDA)")
    private TipoDespacho tipoDespacho;

    @Size(max = 250, message = "El lugar de despacho no puede superar los 250 caracteres")
    private String lugarDespacho;

    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 40, message = "El método de pago no puede superar los 40 caracteres")
    private String metodoPago;
}