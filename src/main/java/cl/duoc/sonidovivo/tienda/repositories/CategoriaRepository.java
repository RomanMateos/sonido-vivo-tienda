package cl.duoc.sonidovivo.tienda.repositories;

import cl.duoc.sonidovivo.tienda.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}