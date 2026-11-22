package cl.duoc.productos.service;

import cl.duoc.productos.dto.MarcaRequest;
import cl.duoc.productos.dto.MarcaResponse;
import cl.duoc.productos.exception.BusinessException;
import cl.duoc.productos.exception.ResourceNotFoundException;
import cl.duoc.productos.model.Marca;
import cl.duoc.productos.repository.MarcaRepository;
import cl.duoc.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarcaService {

    private final MarcaRepository marcaRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<MarcaResponse> listarTodas() {
        return marcaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MarcaResponse obtenerPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public MarcaResponse crear(MarcaRequest request) {
        String nombreLimpio = request.getNombre().trim();
        if (marcaRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new BusinessException("Ya existe una marca con el nombre: " + nombreLimpio);
        }
        Marca marca = Marca.builder().nombre(nombreLimpio).build();
        return toResponse(marcaRepository.save(marca));
    }

    @Transactional
    public MarcaResponse actualizar(Long id, MarcaRequest request) {
        Marca marca = buscarEntidad(id);
        String nombreLimpio = request.getNombre().trim();
        if (marcaRepository.existsByNombreIgnoreCaseAndIdNot(nombreLimpio, id)) {
            throw new BusinessException("Ya existe otra marca con el nombre: " + nombreLimpio);
        }
        marca.setNombre(nombreLimpio);
        return toResponse(marcaRepository.save(marca));
    }

    @Transactional
    public void eliminar(Long id) {
        Marca marca = buscarEntidad(id);
        if (productoRepository.existsByMarcaId(id)) {
            throw new BusinessException("No se puede eliminar la marca porque tiene productos asociados");
        }
        marcaRepository.delete(marca);
    }

    public Marca buscarEntidad(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca no encontrada con ID: " + id));
    }

    private MarcaResponse toResponse(Marca marca) {
        return MarcaResponse.builder()
                .id(marca.getId())
                .nombre(marca.getNombre())
                .build();
    }
}