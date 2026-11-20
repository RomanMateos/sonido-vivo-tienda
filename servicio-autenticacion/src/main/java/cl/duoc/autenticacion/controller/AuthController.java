package cl.duoc.autenticacion.controller;

import cl.duoc.autenticacion.dto.AuthResponse;
import cl.duoc.autenticacion.dto.LoginRequest;
import cl.duoc.autenticacion.dto.UsuarioAuthRequest;
import cl.duoc.autenticacion.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/sincronizar")
    public ResponseEntity> sincronizar(@Valid @RequestBody UsuarioAuthRequest request) {
        authService.sincronizarUsuario(request);
        return ResponseEntity.ok(Map.of("mensaje", "Credenciales sincronizadas correctamente"));
    }

    @GetMapping("/perfil")
    public ResponseEntity> perfil(Authentication authentication) {
        String rol = authentication.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("");

        return ResponseEntity.ok(Map.of(
                "email", authentication.getName(),
                "rol", rol
        ));
    }
}