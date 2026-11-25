package cl.duoc.boletas.repository;

import cl.duoc.boletas.model.Boleta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    boolean existsByVentaId(Long ventaId);

    @EntityGraph(attributePaths = {"detalles"})
    Optional<Boleta> findWithDetallesById(Long id);

    @EntityGraph(attributePaths = {"detalles"})
    Optional<Boleta> findWithDetallesByVentaId(Long ventaId);

    @EntityGraph(attributePaths = {"detalles"})
    List<Boleta> findByCompradorEmailOrderByFechaEmisionDesc(String compradorEmail);

    @EntityGraph(attributePaths = {"detalles"})
    List<Boleta> findAllByOrderByFechaEmisionDesc();
}
