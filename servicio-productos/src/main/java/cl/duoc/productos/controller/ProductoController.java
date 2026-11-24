package cl.duoc.productos.controller;

import cl.duoc.productos.dto.*;
import cl.duoc.productos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long marcaId,
            @RequestParam(required = false) Boolean enOferta,
            @RequestParam(required = false) String busqueda
    ) {
        return ResponseEntity.ok(productoService.listarTodos(categoriaId, marcaId, enOferta, busqueda));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @PatchMapping("/{id}/inventario")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR')")
    public ResponseEntity<ProductoResponse> actualizarInventario(@PathVariable Long id,
                                                                 @Valid @RequestBody ActualizarStockRequest request) {
        return ResponseEntity.ok(productoService.actualizarInventarioManual(id, request));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductoResponse> procesarMovimientoStock(@PathVariable Long id,
                                                                    @Valid @RequestBody MovimientoStockRequest request) {
        return ResponseEntity.ok(productoService.procesarMovimientoStock(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
