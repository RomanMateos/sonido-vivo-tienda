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
    public ProductoResponse obtenerPorId(String id) {
        String idNormalizado = id.trim().toUpperCase();
        Producto producto = productoRepository.findWithDetailsById(idNormalizado)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + idNormalizado));
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        String idNormalizado = request.getId().trim().toUpperCase();
        if (productoRepository.existsById(idNormalizado)) {
            throw new BusinessException("Ya existe un producto registrado con el ID: " + idNormalizado);
        }

        Categoria categoria = categoriaService.buscarEntidad(request.getCategoriaId());
        Marca marca = marcaService.buscarEntidad(request.getMarcaId());

        String descripcionLimpia = (request.getDescripcion() != null && !request.getDescripcion().isBlank())
                ? request.getDescripcion().trim()
                : null;
        String imagenLimpia = (request.getImagen() != null && !request.getImagen().isBlank())
                ? request.getImagen().trim()
                : null;

        Producto producto = Producto.builder()
                .id(idNormalizado)
                .nombre(request.getNombre().trim())
                .categoria(categoria)
                .marca(marca)
                .precio(request.getPrecio())
                .descripcion(descripcionLimpia)
                .imagen(imagenLimpia)
                .enOferta(Boolean.TRUE.equals(request.getEnOferta()))
                .stock(request.getStock())
                .build();

        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(String id, ProductoRequest request) {
        String idNormalizado = id.trim().toUpperCase();
        Producto producto = productoRepository.findWithDetailsById(idNormalizado)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + idNormalizado));

        Categoria categoria = categoriaService.buscarEntidad(request.getCategoriaId());
        Marca marca = marcaService.buscarEntidad(request.getMarcaId());

        String descripcionLimpia = (request.getDescripcion() != null && !request.getDescripcion().isBlank())
                ? request.getDescripcion().trim()
                : null;
        String imagenLimpia = (request.getImagen() != null && !request.getImagen().isBlank())
                ? request.getImagen().trim()
                : null;

        producto.setNombre(request.getNombre().trim());
        producto.setCategoria(categoria);
        producto.setMarca(marca);
        producto.setPrecio(request.getPrecio());
        producto.setDescripcion(descripcionLimpia);
        producto.setImagen(imagenLimpia);
        producto.setEnOferta(Boolean.TRUE.equals(request.getEnOferta()));
        producto.setStock(request.getStock());

        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizarInventarioManual(String id, ActualizarStockRequest request) {
        String idNormalizado = id.trim().toUpperCase();
        Producto producto = productoRepository.findWithDetailsById(idNormalizado)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + idNormalizado));

        producto.setStock(request.getStock());
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse procesarMovimientoStock(String id, MovimientoStockRequest request) {
        String idNormalizado = id.trim().toUpperCase();
        Producto producto = productoRepository.findWithDetailsById(idNormalizado)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + idNormalizado));

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
    public void eliminar(String id) {
        String idNormalizado = id.trim().toUpperCase();
        Producto producto = productoRepository.findById(idNormalizado)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + idNormalizado));
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