package cl.duoc.reportes.controller;

import cl.duoc.reportes.dto.GenerarReporteRequest;
import cl.duoc.reportes.dto.ReporteResponse;
import cl.duoc.reportes.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @PostMapping("/generar")
    public ResponseEntity<ReporteResponse> generarReporte(
            @Valid @RequestBody(required = false) GenerarReporteRequest request,
            Authentication authentication
    ) {
        ReporteResponse response = reporteService.generarReporte(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReporteResponse>> listarReportes() {
        return ResponseEntity.ok(reporteService.listarReportes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminarReporte(id);
        return ResponseEntity.noContent().build();
    }
}