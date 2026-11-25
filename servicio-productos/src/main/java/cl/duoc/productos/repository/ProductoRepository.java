package cl.duoc.productos.repository;

import cl.duoc.productos.model.Producto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, String> {

    @Override
    @EntityGraph(attributePaths = {"categoria", "marca"})
    List<Producto> findAll();

    @EntityGraph(attributePaths = {"categoria", "marca"})
    Optional<Producto> findWithDetailsById(String id);

    @EntityGraph(attributePaths = {"categoria", "marca"})
    List<Producto> findByCategoriaId(Long categoriaId);

    @EntityGraph(attributePaths = {"categoria", "marca"})
    List<Producto> findByMarcaId(Long marcaId);

    @EntityGraph(attributePaths = {"categoria", "marca"})
    List<Producto> findByEnOfertaTrue();

    @EntityGraph(attributePaths = {"categoria", "marca"})
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    boolean existsByCategoriaId(Long categoriaId);

    boolean existsByMarcaId(Long marcaId);
}