package cl.duoc.sonidovivo.tienda.repositories;

import cl.duoc.sonidovivo.tienda.entities.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
}