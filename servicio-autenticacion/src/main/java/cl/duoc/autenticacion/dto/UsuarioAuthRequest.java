package cl.duoc.autenticacion.dto;

import cl.duoc.autenticacion.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioAuthRequest {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    @Pattern(
            regexp = "^(?i)[A-Za-z0-9._%+-]+@(duoc\\.cl|profesor\\.duoc\\.cl|gmail\\.com)$",
            message = "Solo se permiten correos @duoc.cl, @profesor.duoc.cl o @gmail.com"
    )
    private String email;

    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String contrasena;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    private Boolean activo = true;
}
