package cl.duoc.boletas.controller;

import cl.duoc.boletas.dto.BoletaResponse;
import cl.duoc.boletas.dto.CrearBoletaRequest;
import cl.duoc.boletas.service.BoletaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletas")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaService boletaService;

    @PostMapping
    public ResponseEntity<BoletaResponse> emitirBoleta(@Valid @RequestBody CrearBoletaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boletaService.emitirBoleta(request));
    }

    @GetMapping("/mis-boletas")
    public ResponseEntity<List<BoletaResponse>> listarMisBoletas(Authentication authentication) {
        return ResponseEntity.ok(boletaService.listarMisBoletas(authentication.getName()));
    }

    @GetMapping("/{folio}")
    public ResponseEntity<BoletaResponse> obtenerPorFolio(Authentication authentication, @PathVariable Long folio) {
        return ResponseEntity.ok(boletaService.obtenerPorFolio(folio, authentication.getName(), esStaff(authentication)));
    }

    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<BoletaResponse> obtenerPorVentaId(Authentication authentication, @PathVariable Long ventaId) {
        return ResponseEntity.ok(boletaService.obtenerPorVentaId(ventaId, authentication.getName(), esStaff(authentication)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR')")
    public ResponseEntity<List<BoletaResponse>> listarTodas() {
        return ResponseEntity.ok(boletaService.listarTodas());
    }

    @PatchMapping("/venta/{ventaId}/anular")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<BoletaResponse> anularPorVentaId(@PathVariable Long ventaId) {
        return ResponseEntity.ok(boletaService.anularPorVentaId(ventaId));
    }

    private boolean esStaff(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR")
                        || a.getAuthority().equals("ROLE_TRABAJADOR"));
    }
}