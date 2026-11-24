package cl.duoc.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BlogRequest {

    @NotBlank(message = "El título del artículo es obligatorio")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String titulo;

    @NotBlank(message = "El resumen del artículo es obligatorio")
    @Size(max = 500, message = "El resumen no puede superar los 500 caracteres")
    private String resumen;

    @NotBlank(message = "El primer párrafo es obligatorio")
    private String parrafo1;

    @NotBlank(message = "El primer subtítulo es obligatorio")
    @Size(max = 200, message = "El primer subtítulo no puede superar los 200 caracteres")
    private String subtitulo1;

    @NotBlank(message = "El segundo párrafo es obligatorio")
    private String parrafo2;

    @NotBlank(message = "El segundo subtítulo es obligatorio")
    @Size(max = 200, message = "El segundo subtítulo no puede superar los 200 caracteres")
    private String subtitulo2;

    @NotBlank(message = "El tercer párrafo es obligatorio")
    private String parrafo3;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @Size(max = 500, message = "La URL de la imagen no puede superar los 500 caracteres")
    private String imagen;

    @NotBlank(message = "La cita destacada es obligatoria")
    @Size(max = 500, message = "La cita no puede superar los 500 caracteres")
    private String cita;
}
