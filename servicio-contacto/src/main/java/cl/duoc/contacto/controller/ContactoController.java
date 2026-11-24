package cl.duoc.contacto.controller;

import cl.duoc.contacto.dto.ActualizarEstadoContactoRequest;
import cl.duoc.contacto.dto.ContactoRequest;
import cl.duoc.contacto.dto.ContactoResponse;
import cl.duoc.contacto.model.EstadoContacto;
import cl.duoc.contacto.service.ContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacto")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService contactoService;

    @PostMapping
    public ResponseEntity<ContactoResponse> registrarMensaje(@Valid @RequestBody ContactoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contactoService.registrarMensaje(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR')")
    public ResponseEntity<List<ContactoResponse>> listarMensajes(@RequestParam(required = false) EstadoContacto estado) {
        return ResponseEntity.ok(contactoService.listarMensajes(estado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR')")
    public ResponseEntity<ContactoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(contactoService.obtenerPorId(id));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR')")
    public ResponseEntity<ContactoResponse> actualizarEstado(@PathVariable Long id,
                                                             @Valid @RequestBody ActualizarEstadoContactoRequest request) {
        return ResponseEntity.ok(contactoService.actualizarEstado(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long id) {
        contactoService.eliminarMensaje(id);
        return ResponseEntity.noContent().build();
    }
}