package cl.duoc.usuarios.repository;

import cl.duoc.usuarios.model.RegistroUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistroUsuarioRepository extends JpaRepository<RegistroUsuario, Long> {

    Optional<RegistroUsuario> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRun(String run);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByRunAndIdNot(String run, Long id);
}
