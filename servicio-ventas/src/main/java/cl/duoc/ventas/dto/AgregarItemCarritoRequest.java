package cl.duoc.ventas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AgregarItemCarritoRequest {

    @NotBlank(message = "El ID del producto es obligatorio")
    @Size(min = 3, max = 5, message = "El ID del producto debe tener entre 3 y 5 caracteres")
    private String productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}
