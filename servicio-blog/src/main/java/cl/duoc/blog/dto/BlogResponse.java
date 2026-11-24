package cl.duoc.blog.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BlogResponse {
    private Long id;
    private String titulo;
    private LocalDateTime fecha;
    private String resumen;
    private String parrafo1;
    private String subtitulo1;
    private String parrafo2;
    private String subtitulo2;
    private String parrafo3;
    private String imagen;
    private String cita;
}