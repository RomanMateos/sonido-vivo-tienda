package cl.duoc.productos.service;

import cl.duoc.productos.dto.*;
import cl.duoc.productos.exception.BusinessException;
import cl.duoc.productos.exception.ResourceNotFoundException;
import cl.duoc.productos.model.Categoria;
import cl.duoc.productos.model.Marca;
import cl.duoc.productos.model.Producto;
import cl.duoc.productos.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final MarcaService marcaService;

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos(Long categoriaId, Long marcaId, Boolean enOferta, String busqueda) {
        List<Producto> productos;

        if (categoriaId != null) {
            productos = productoRepository.findByCategoriaId(categoriaId);
        } else if (marcaId != null) {
            productos = productoRepository.findByMarcaId(marcaId);
        } else if (Boolean.TRUE.equals(enOferta)) {
            productos = productoRepository.findByEnOfertaTrue();
        } else if (busqueda != null && !busqueda.isBlank()) {
            productos = productoRepository.findByNombreContainingIgnoreCase(busqueda.trim());
        } else {
            productos = productoRepository.findAll();
        }

        return productos.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        Categoria categoria = categoriaService.buscarEntidad(request.getCategoriaId());
        Marca marca = marcaService.buscarEntidad(request.getMarcaId());

        Producto producto = Producto.builder()
                .nombre(request.getNombre().trim())
                .categoria(categoria)
                .marca(marca)
                .precio(request.getPrecio())
                .descripcion(request.getDescripcion().trim())
                .imagen(request.getImagen().trim())
                .enOferta(Boolean.TRUE.equals(request.getEnOferta()))
                .stock(request.getStock())
                .build();

        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        Categoria categoria = categoriaService.buscarEntidad(request.getCategoriaId());
        Marca marca = marcaService.buscarEntidad(request.getMarcaId());

        producto.setNombre(request.getNombre().trim());
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setPrecio(request.getPrecio());
        producto.setDescripcion(request.getDescripcion().trim());
        producto.setImagen(request.getImagen().trim());
        producto.setEnOferta(Boolean.TRUE.equals(request.getEnOferta()));
        producto.setStock(request.getStock());

        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizarInventarioManual(Long id, ActualizarStockRequest request) {
        Producto producto = productoRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        producto.setStock(request.getStock());
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse procesarMovimientoStock(Long id, MovimientoStockRequest request) {
        Producto producto = productoRepository.findWithDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (request.getOperacion() == TipoOperacionStock.DESCONTAR) {
            if (producto.getStock() < request.getCantidad()) {
                throw new BusinessException("Stock insuficiente para el producto '" + producto.getNombre()
                        + "'. Disponible: " + producto.getStock() + ", solicitado: " + request.getCantidad());
            }
            producto.setStock(producto.getStock() - request.getCantidad());
        } else {
            producto.setStock(producto.getStock() + request.getCantidad());
        }

        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        productoRepository.delete(producto);
    }

    private ProductoResponse toResponse(Producto p) {
        return ProductoResponse.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .categoriaId(p.getCategoria().getId())
                .categoriaNombre(p.getCategoria().getNombre())
                .marcaId(p.getMarca().getId())
                .marcaNombre(p.getMarca().getNombre())
                .precio(p.getPrecio())
                .descripcion(p.getDescripcion())
                .imagen(p.getImagen())
                .enOferta(p.getEnOferta())
                .stock(p.getStock())
                .build();
    }
}