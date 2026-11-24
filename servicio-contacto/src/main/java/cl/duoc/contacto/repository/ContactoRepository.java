package cl.duoc.contacto.repository;

import cl.duoc.contacto.model.Contacto;
import cl.duoc.contacto.model.EstadoContacto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    List<Contacto> findAllByOrderByFechaDesc();

    List<Contacto> findByEstadoOrderByFechaDesc(EstadoContacto estado);
}
