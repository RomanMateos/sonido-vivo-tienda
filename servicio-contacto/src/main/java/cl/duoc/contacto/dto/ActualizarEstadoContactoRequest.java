package cl.duoc.contacto.dto;

import cl.duoc.contacto.model.EstadoContacto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarEstadoContactoRequest {

    @NotNull(message = "El estado es obligatorio (PENDIENTE, LEIDO o RESPONDIDO)")
    private EstadoContacto estado;
}
