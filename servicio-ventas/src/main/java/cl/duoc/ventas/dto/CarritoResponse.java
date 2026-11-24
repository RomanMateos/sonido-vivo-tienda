package cl.duoc.ventas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CarritoResponse {
    private Long id;
    private String usuarioEmail;
    private LocalDateTime fechaActualizacion;
    private Integer totalItems;
    private Integer total;
    private List<ItemCarritoResponse> items;
}