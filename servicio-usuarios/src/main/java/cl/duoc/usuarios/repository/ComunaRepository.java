package cl.duoc.usuarios.repository;

import cl.duoc.usuarios.model.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComunaRepository extends JpaRepository<Comuna, Long> {

    List<Comuna> findByRegionIdOrderByNombreAsc(Long regionId);
}