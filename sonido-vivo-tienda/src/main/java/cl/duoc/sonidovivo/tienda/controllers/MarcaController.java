package cl.duoc.sonidovivo.tienda.controllers;

import cl.duoc.sonidovivo.tienda.entities.Marca;
import cl.duoc.sonidovivo.tienda.services.MarcaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/marcas")
public class MarcaController {
    private final MarcaService service;

    public MarcaController(MarcaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Marca> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Marca buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Marca> crear(@Valid @RequestBody Marca marca) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(marca));
    }

    @PutMapping("/{id}")
    public Marca actualizar(@PathVariable Long id, @Valid @RequestBody Marca marca) {
        return service.actualizar(id, marca);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}