package cl.duoc.usuarios.config;

import cl.duoc.usuarios.model.Comuna;
import cl.duoc.usuarios.model.Region;
import cl.duoc.usuarios.model.RegistroUsuario;
import cl.duoc.usuarios.model.Rol;
import cl.duoc.usuarios.repository.ComunaRepository;
import cl.duoc.usuarios.repository.RegionRepository;
import cl.duoc.usuarios.repository.RegistroUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;
    private final RegistroUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (regionRepository.count() == 0) {
            Region rm = regionRepository.save(Region.builder().nombre("Región Metropolitana de Santiago").build());
            Region valparaiso = regionRepository.save(Region.builder().nombre("Región de Valparaíso").build());
            Region biobio = regionRepository.save(Region.builder().nombre("Región del Biobío").build());

            comunaRepository.saveAll(List.of(
                    Comuna.builder().nombre("Santiago").region(rm).build(),
                    Comuna.builder().nombre("Providencia").region(rm).build(),
                    Comuna.builder().nombre("Las Condes").region(rm).build(),
                    Comuna.builder().nombre("Ñuñoa").region(rm).build(),
                    Comuna.builder().nombre("Maipú").region(rm).build(),
                    Comuna.builder().nombre("La Florida").region(rm).build(),
                    Comuna.builder().nombre("Puente Alto").region(rm).build(),
                    Comuna.builder().nombre("San Joaquín").region(rm).build(),
                    Comuna.builder().nombre("Valparaíso").region(valparaiso).build(),
                    Comuna.builder().nombre("Viña del Mar").region(valparaiso).build(),
                    Comuna.builder().nombre("Quilpué").region(valparaiso).build(),
                    Comuna.builder().nombre("Concepción").region(biobio).build(),
                    Comuna.builder().nombre("Talcahuano").region(biobio).build()
            ));
        }

        String adminEmail = "admin@sonidovivo.cl";
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            Comuna comunaBase = comunaRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow();

            usuarioRepository.save(RegistroUsuario.builder()
                    .run("11111111-1")
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
