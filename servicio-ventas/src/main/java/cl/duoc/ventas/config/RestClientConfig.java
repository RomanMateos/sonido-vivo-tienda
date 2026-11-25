package cl.duoc.ventas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean("usuariosRestClient")
    public RestClient usuariosRestClient(RestClient.Builder builder,
                                         @Value("${app.services.usuarios.url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean("productosRestClient")
    public RestClient productosRestClient(RestClient.Builder builder,
                                          @Value("${app.services.productos.url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }

    @Bean("boletasRestClient")
    public RestClient boletasRestClient(RestClient.Builder builder,
                                        @Value("${app.services.boletas.url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}