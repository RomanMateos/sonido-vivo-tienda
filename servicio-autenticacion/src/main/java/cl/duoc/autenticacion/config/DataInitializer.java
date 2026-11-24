package cl.duoc.autenticacion.config;

import cl.duoc.autenticacion.model.Rol;
import cl.duoc.autenticacion.model.Usuario;
import cl.duoc.autenticacion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@sonidovivo.cl";

        if (!usuarioRepository.existsByEmail(adminEmail)) {
            Usuario admin = Usuario.builder()
                    .email(adminEmail)
                    .contrasena(passwordEncoder.encode("Admin1234!"))
                    .rol(Rol.ADMINISTRADOR)
                    .activo(true)
                    .build();

            usuarioRepository.save(admin);
        }
    }
}