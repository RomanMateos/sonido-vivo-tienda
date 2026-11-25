package cl.duoc.boletas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CrearBoletaRequest {

    @NotNull(message = "El ID de la venta asociada es obligatorio")
    private Long ventaId;

    @NotBlank(message = "El nombre completo del comprador es obligatorio")
    private String compradorNombreCompleto;

    @NotBlank(message = "El correo del comprador es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    private String compradorEmail;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;

    @NotNull(message = "El monto total es obligatorio")
    @Min(value = 0, message = "El monto total no puede ser negativo")
    private Integer montoTotal;

    @NotEmpty(message = "La boleta debe contener al menos un ítem")
    @Valid
    private List<DetalleBoletaRequest> items;
}
