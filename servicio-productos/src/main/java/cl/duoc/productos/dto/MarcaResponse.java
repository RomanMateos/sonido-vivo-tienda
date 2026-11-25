package cl.duoc.productos.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MarcaResponse {
    private Long id;
    private String nombre;
}
