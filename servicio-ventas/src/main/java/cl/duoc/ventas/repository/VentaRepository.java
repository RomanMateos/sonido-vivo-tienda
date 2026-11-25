package cl.duoc.ventas.repository;

import cl.duoc.ventas.model.EstadoVenta;
import cl.duoc.ventas.model.Venta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @EntityGraph(attributePaths = {"detalles"})
    Optional<Venta> findWithDetallesById(Long id);

    @EntityGraph(attributePaths = {"detalles"})
    List<Venta> findAllByOrderByFechaDesc();

    @EntityGraph(attributePaths = {"detalles"})
    List<Venta> findByCompradorEmailOrderByFechaDesc(String compradorEmail);

    @EntityGraph(attributePaths = {"detalles"})
    List<Venta> findByEstadoOrderByFechaDesc(EstadoVenta estado);
}
