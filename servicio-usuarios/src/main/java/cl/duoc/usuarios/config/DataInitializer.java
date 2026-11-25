package cl.duoc.usuarios.config;

import cl.duoc.usuarios.model.Comuna;
import cl.duoc.usuarios.model.RegistroUsuario;
import cl.duoc.usuarios.model.Rol;
import cl.duoc.usuarios.repository.ComunaRepository;
import cl.duoc.usuarios.repository.RegistroUsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ComunaRepository comunaRepository;
    private final RegistroUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void run(String... args) {
        String adminEmail = "admin@duoc.cl";
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            Comuna comunaBase = comunaRepository.findAll().stream()
                    .findFirst()
                    .orElse(null);

            if (comunaBase == null) {
                log.warn("No existen comunas cargadas en BD; se omite creación automática de perfil admin@duoc.cl");
                return;
            }

            usuarioRepository.save(RegistroUsuario.builder()
                    .run("111111111")
                    .nombre("Administrador")
                    .apellidos("Sonido Vivo")
                    .email(adminEmail)
                    .telefono("+56912345678")
                    .direccion("Av. Vicuña Mackenna 4917")
                    .comuna(comunaBase)
                    .rol(Rol.ADMINISTRADOR)
                    .activo(true)
                    .build());
        }
    }
}