package cl.duoc.sonidovivo.tienda.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // 1. Si no hay token o no empieza con "Bearer ", dejamos pasar la petición 
        // (SecurityConfig decidirá más adelante si la bloquea o no)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer el token quitando los primeros 7 caracteres ("Bearer ")
        final String jwt = authHeader.substring(7);

        // 3. Validar el token usando nuestra clave secreta compartida
        if (jwtService.isTokenValid(jwt)) {
            String email = jwtService.extractUsername(jwt);
            String rol = jwtService.extractRol(jwt);

            // 4. Spring Security requiere el prefijo "ROLE_" para manejar autorizaciones
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + rol);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    Collections.singletonList(authority)
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // 5. Inyectamos el usuario en el contexto de seguridad de esta petición
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 6. Continuar con el resto de la cadena de filtros
        filterChain.doFilter(request, response);
    }
}