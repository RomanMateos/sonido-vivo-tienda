package cl.duoc.usuarios.dto;

import cl.duoc.usuarios.model.Rol;
import cl.duoc.usuarios.validation.RunValido;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioAdminRequest {

    @NotBlank(message = "El RUN es obligatorio")
    @Size(min = 7, max = 9, message = "El RUN debe tener entre 7 y 9 caracteres sin puntos ni guion")
    @Pattern(regexp = "^[0-9]{6,8}[0-9Kk]$", message = "El RUN debe ingresarse sin puntos ni guion")
    @RunValido
    private String run;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
    private String apellidos;

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

    @Size(max = 30, message = "El teléfono no puede superar los 30 caracteres")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 300, message = "La dirección no puede superar los 300 caracteres")
    private String direccion;

    private Long regionId;

    @NotNull(message = "La comuna es obligatoria")
    private Long comunaId;

    @NotNull(message = "El rol es obligatorio")
    private Rol rol;

    private Boolean activo = true;
}