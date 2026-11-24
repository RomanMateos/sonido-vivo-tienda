package cl.duoc.ventas.controller;

import cl.duoc.ventas.dto.ActualizarCantidadCarritoRequest;
import cl.duoc.ventas.dto.AgregarItemCarritoRequest;
import cl.duoc.ventas.dto.CarritoResponse;
import cl.duoc.ventas.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    public ResponseEntity<CarritoResponse> obtenerCarrito(Authentication authentication) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(authentication.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoResponse> agregarItem(Authentication authentication,
                                                       @Valid @RequestBody AgregarItemCarritoRequest request) {
        return ResponseEntity.ok(carritoService.agregarItem(authentication.getName(), request));
    }

    @PutMapping("/items/{productoId}")
    public ResponseEntity<CarritoResponse> actualizarCantidad(Authentication authentication,
                                                              @PathVariable Long productoId,
                                                              @Valid @RequestBody ActualizarCantidadCarritoRequest request) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(authentication.getName(), productoId, request));
    }

    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<CarritoResponse> eliminarItem(Authentication authentication,
                                                        @PathVariable Long productoId) {
        return ResponseEntity.ok(carritoService.eliminarItem(authentication.getName(), productoId));
    }

    @DeleteMapping
    public ResponseEntity<CarritoResponse> vaciarCarrito(Authentication authentication) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(authentication.getName()));
    }
}
