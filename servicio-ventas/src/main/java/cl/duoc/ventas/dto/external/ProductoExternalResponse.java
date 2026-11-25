package cl.duoc.ventas.dto.external;

import lombok.Data;

@Data
public class ProductoExternalResponse {
    private String id;
    private String nombre;
    private Integer precio;
    private String imagen;
    private Integer stock;
}