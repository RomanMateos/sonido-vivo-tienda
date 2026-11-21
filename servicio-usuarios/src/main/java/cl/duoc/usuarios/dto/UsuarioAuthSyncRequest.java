package cl.duoc.usuarios.dto;

import cl.duoc.usuarios.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioAuthSyncRequest {

    private String email;
    private String contrasena;
    private Rol rol;
    private Boolean activo;
}