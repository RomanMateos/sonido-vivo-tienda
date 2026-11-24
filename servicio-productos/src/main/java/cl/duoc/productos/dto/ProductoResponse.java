package cl.duoc.productos.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoResponse {
    private Long id;
    private String nombre;
    private Long categoriaId;
    private String categoriaNombre;
    private Long marcaId;
    private String marcaNombre;
    private Integer precio;
    private String descripcion;
    private String imagen;
    private Boolean enOferta;
    private Integer stock;
}
