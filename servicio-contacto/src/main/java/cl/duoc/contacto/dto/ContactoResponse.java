package cl.duoc.contacto.dto;

import cl.duoc.contacto.model.EstadoContacto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContactoResponse {
    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private LocalDateTime fecha;
    private String mensaje;
    private EstadoContacto estado;
}