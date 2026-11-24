package cl.duoc.ventas.repository;

import cl.duoc.ventas.model.Carrito;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    @EntityGraph(attributePaths = {"items"})
    Optional<Carrito> findWithItemsByUsuarioEmail(String usuarioEmail);
}