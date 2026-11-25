package cl.duoc.usuarios.service;

import cl.duoc.usuarios.model.Comuna;
import cl.duoc.usuarios.model.Region;
import cl.duoc.usuarios.repository.ComunaRepository;
import cl.duoc.usuarios.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UbicacionService {

    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;

    @Transactional(readOnly = true)
    public List<Region> listarRegiones() {
        return regionRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public List<Comuna> listarComunasPorRegion(Long regionId) {
        if (!regionRepository.existsById(regionId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La región indicada no existe");
        }
        return comunaRepository.findByRegionIdOrderByNombreAsc(regionId);
    }
}
