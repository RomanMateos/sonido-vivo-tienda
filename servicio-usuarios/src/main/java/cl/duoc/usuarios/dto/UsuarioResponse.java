package cl.duoc.usuarios.dto;

import cl.duoc.usuarios.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private Long id;
    private String run;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String direccion;
    private Long comunaId;
    private String comunaNombre;
    private Long regionId;
    private String regionNombre;
    private Rol rol;
    private Boolean activo;
    private LocalDateTime fechaRegistro;
}