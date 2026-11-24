package cl.duoc.blog.controller;

import cl.duoc.blog.dto.BlogRequest;
import cl.duoc.blog.dto.BlogResponse;
import cl.duoc.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<List<BlogResponse>> listarArticulos(@RequestParam(required = false) String titulo) {
        return ResponseEntity.ok(blogService.listarArticulos(titulo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR')")
    public ResponseEntity<BlogResponse> crearArticulo(@Valid @RequestBody BlogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.crearArticulo(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'TRABAJADOR')")
    public ResponseEntity<BlogResponse> actualizarArticulo(@PathVariable Long id,
                                                           @Valid @RequestBody BlogRequest request) {
        return ResponseEntity.ok(blogService.actualizarArticulo(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarArticulo(@PathVariable Long id) {
        blogService.eliminarArticulo(id);
        return ResponseEntity.noContent().build();
    }
}
