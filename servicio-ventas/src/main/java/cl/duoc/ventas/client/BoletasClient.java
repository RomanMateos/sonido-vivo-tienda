package cl.duoc.ventas.client;

import cl.duoc.ventas.dto.external.CrearBoletaExternalRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class BoletasClient {

    private final RestClient boletasRestClient;
    private final boolean boletasEnabled;

    public BoletasClient(@Qualifier("boletasRestClient") RestClient boletasRestClient,
                         @Value("${app.services.boletas.enabled:false}") boolean boletasEnabled) {
        this.boletasRestClient = boletasRestClient;
        this.boletasEnabled = boletasEnabled;
    }

    public void emitirBoleta(CrearBoletaExternalRequest request, String authHeader) {
        if (!boletasEnabled) {
            log.info("servicio-boletas deshabilitado temporalmente. Omitiendo emisión para venta ID {}", request.getVentaId());
            return;
        }
        boletasRestClient.post()
                .uri("/api/boletas")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void anularBoletaPorVenta(Long ventaId, String authHeader) {
        if (!boletasEnabled) {
            log.info("servicio-boletas deshabilitado temporalmente. Omitiendo anulación de boleta para venta ID {}", ventaId);
            return;
        }
        boletasRestClient.patch()
                .uri("/api/boletas/venta/{ventaId}/anular", ventaId)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .toBodilessEntity();
    }
}
