package cl.duoc.ventas.client;

import cl.duoc.ventas.dto.external.MovimientoStockExternalRequest;
import cl.duoc.ventas.dto.external.ProductoExternalResponse;
import cl.duoc.ventas.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class ProductosClient {

    private final RestClient productosRestClient;

    public ProductosClient(@Qualifier("productosRestClient") RestClient productosRestClient) {
        this.productosRestClient = productosRestClient;
    }

    public ProductoExternalResponse obtenerProducto(String productoId) {
        String idLimpio = productoId.trim().toUpperCase();
        try {
            ProductoExternalResponse producto = productosRestClient.get()
                    .uri("/api/productos/{id}", idLimpio)
                    .retrieve()
                    .body(ProductoExternalResponse.class);

            if (producto == null) {
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + idLimpio);
            }
            return producto;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + idLimpio);
        }
    }

    public void procesarMovimientoStock(String productoId, String operacion, Integer cantidad, String authHeader) {
        String idLimpio = productoId.trim().toUpperCase();
        productosRestClient.patch()
                .uri("/api/productos/{id}/stock", idLimpio)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MovimientoStockExternalRequest(operacion, cantidad))
                .retrieve()
                .toBodilessEntity();
    }
}
