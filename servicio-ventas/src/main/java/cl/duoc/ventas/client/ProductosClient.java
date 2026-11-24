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

    public ProductoExternalResponse obtenerProducto(Long productoId) {
        try {
            ProductoExternalResponse producto = productosRestClient.get()
                    .uri("/api/productos/{id}", productoId)
                    .retrieve()
                    .body(ProductoExternalResponse.class);

            if (producto == null) {
                throw new ResourceNotFoundException("Producto no encontrado con ID: " + productoId);
            }
            return producto;
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + productoId);
        }
    }

    public void procesarMovimientoStock(Long productoId, String operacion, Integer cantidad, String authHeader) {
        productosRestClient.patch()
                .uri("/api/productos/{id}/stock", productoId)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new MovimientoStockExternalRequest(operacion, cantidad))
                .retrieve()
                .toBodilessEntity();
    }
}
