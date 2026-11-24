package cl.duoc.reportes.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioExternalDto {
    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String rol;
}