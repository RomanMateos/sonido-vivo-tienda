package cl.duoc.reportes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotDetalleDto {
    private Map<String, Integer> usuariosPorRol;
    private List<ProductoStockAlertDto> productosBajoStock;
    private List<ProductoStockAlertDto> productosSinStock;
}
