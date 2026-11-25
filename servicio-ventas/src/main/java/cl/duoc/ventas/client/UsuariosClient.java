package cl.duoc.ventas.client;

import cl.duoc.ventas.dto.external.UsuarioPerfilExternalResponse;
import cl.duoc.ventas.exception.BusinessException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UsuariosClient {

    private final RestClient usuariosRestClient;

    public UsuariosClient(@Qualifier("usuariosRestClient") RestClient usuariosRestClient) {
        this.usuariosRestClient = usuariosRestClient;
    }

    public UsuarioPerfilExternalResponse obtenerPerfil(String authHeader) {
        UsuarioPerfilExternalResponse perfil = usuariosRestClient.get()
                .uri("/api/usuarios/perfil")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .body(UsuarioPerfilExternalResponse.class);

        if (perfil == null) {
            throw new BusinessException("No se pudo obtener el perfil del comprador desde servicio-usuarios");
        }
        return perfil;
    }
}