package cl.duoc.productos.controller;

import cl.duoc.productos.dto.MarcaRequest;
import cl.duoc.productos.dto.MarcaResponse;
import cl.duoc.productos.service.MarcaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
public class MarcaController {

    private final MarcaService marcaService;

    @GetMapping
    public ResponseEntity<List<MarcaResponse>> listarTodas() {
        return ResponseEntity.ok(marcaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MarcaResponse> crear(@Valid @RequestBody MarcaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marcaService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<MarcaResponse> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody MarcaRequest request) {
        return ResponseEntity.ok(marcaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        marcaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}