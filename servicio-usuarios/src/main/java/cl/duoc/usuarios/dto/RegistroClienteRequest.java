package cl.duoc.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroClienteRequest {

    @NotBlank(message = "El RUN es obligatorio")
    @Size(max = 20)
    private String run;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 120)
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 120)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String contrasena;

    @Size(max = 30)
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200)
    private String direccion;

    @NotNull(message = "La comuna es obligatoria")
    private Long comunaId;
}
