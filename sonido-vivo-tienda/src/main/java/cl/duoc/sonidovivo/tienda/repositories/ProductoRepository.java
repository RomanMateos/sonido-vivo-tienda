package cl.duoc.sonidovivo.tienda.repositories;

import cl.duoc.sonidovivo.tienda.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}