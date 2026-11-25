package cl.duoc.sonidovivo.tienda.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado: token ausente o invalido")
                )
            )
            .authorizeHttpRequests(auth -> auth
                // 1. Swagger publico
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // 2. Catalogo publico (lectura GET para todos)
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/categorias/**", "/api/v1/marcas/**").permitAll()
                // 3. Escritura (POST, PUT, DELETE) solo para ADMINISTRADOR
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**", "/api/v1/categorias/**", "/api/v1/marcas/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**", "/api/v1/categorias/**", "/api/v1/marcas/**").hasRole("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**", "/api/v1/categorias/**", "/api/v1/marcas/**").hasRole("ADMINISTRADOR")
                // 4. Cualquier otra peticion requiere estar autenticado
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}