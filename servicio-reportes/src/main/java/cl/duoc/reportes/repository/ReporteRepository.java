package cl.duoc.reportes.repository;

import cl.duoc.reportes.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    List<Reporte> findAllByOrderByFechaEmisionDesc();
}
