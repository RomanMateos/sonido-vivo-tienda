package cl.duoc.usuarios.controller;

import cl.duoc.usuarios.model.Comuna;
import cl.duoc.usuarios.model.Region;
import cl.duoc.usuarios.service.UbicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionController {

    private final UbicacionService ubicacionService;

    @GetMapping("/regiones")
    public ResponseEntity< List<Region> > listarRegiones() {
        return ResponseEntity.ok(ubicacionService.listarRegiones());
    }

    @GetMapping("/regiones/{regionId}/comunas")
    public ResponseEntity< List<Comuna> > listarComunasPorRegion(@PathVariable Long regionId) {
        return ResponseEntity.ok(ubicacionService.listarComunasPorRegion(regionId));
    }
}
