package cl.duoc.ventas.dto.external;

import lombok.Data;

@Data
public class UsuarioPerfilExternalResponse {
    private Long id;
    private String run;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private String comunaNombre;
    private String regionNombre;
    private String rol;
    private Boolean activo;
}
