package cl.duoc.autenticacion.service;

import cl.duoc.autenticacion.dto.AuthResponse;
import cl.duoc.autenticacion.dto.LoginRequest;
import cl.duoc.autenticacion.dto.UsuarioAuthRequest;
import cl.duoc.autenticacion.model.Usuario;
import cl.duoc.autenticacion.repository.UsuarioRepository;
import cl.duoc.autenticacion.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "La cuenta se encuentra desactivada");
        }

        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String token = jwtService.generarToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .build();
    }

    @Transactional
    public void sincronizarUsuario(UsuarioAuthRequest request) {
        String emailNormalizado = request.getEmail().trim().toLowerCase();

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseGet(() -> {
                    if (request.getContrasena() == null || request.getContrasena().isBlank()) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "La contraseña es obligatoria para crear un usuario nuevo");
                    }
                    return Usuario.builder()
                            .email(emailNormalizado)
                            .build();
                });

        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        usuario.setRol(request.getRol());
        if (request.getActivo() != null) {
            usuario.setActivo(request.getActivo());
        } else if (usuario.getActivo() == null) {
            usuario.setActivo(true);
        }

        usuarioRepository.save(usuario);
    }
}
