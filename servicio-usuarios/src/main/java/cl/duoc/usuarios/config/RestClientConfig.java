package cl.duoc.usuarios.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient authRestClient(
            RestClient.Builder builder,
            @Value("${app.auth.service-url}") String authServiceUrl) {
        return builder
                .baseUrl(authServiceUrl)
                .build();
    }
}