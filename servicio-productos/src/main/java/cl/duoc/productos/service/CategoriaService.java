package cl.duoc.productos.service;

import cl.duoc.productos.dto.CategoriaRequest;
import cl.duoc.productos.dto.CategoriaResponse;
import cl.duoc.productos.exception.BusinessException;
import cl.duoc.productos.exception.ResourceNotFoundException;
import cl.duoc.productos.model.Categoria;
import cl.duoc.productos.repository.CategoriaRepository;
import cl.duoc.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorId(Long id) {
        return toResponse(buscarEntidad(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        String nombreLimpio = request.getNombre().trim();
        if (categoriaRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new BusinessException("Ya existe una categoría con el nombre: " + nombreLimpio);
        }
        Categoria categoria = Categoria.builder().nombre(nombreLimpio).build();
        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarEntidad(id);
        String nombreLimpio = request.getNombre().trim();
        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(nombreLimpio, id)) {
            throw new BusinessException("Ya existe otra categoría con el nombre: " + nombreLimpio);
        }
        categoria.setNombre(nombreLimpio);
        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscarEntidad(id);
        if (productoRepository.existsByCategoriaId(id)) {
            throw new BusinessException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        categoriaRepository.delete(categoria);
    }

    public Categoria buscarEntidad(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .build();
    }
}