package cl.duoc.reportes.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestInterceptor((request, body, execution) -> {
                    ServletRequestAttributes attributes =
                            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attributes != null) {
                        HttpServletRequest currentRequest = attributes.getRequest();
                        String authHeader = currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
                        if (authHeader != null && !authHeader.isBlank()) {
                            request.getHeaders().set(HttpHeaders.AUTHORIZATION, authHeader);
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
