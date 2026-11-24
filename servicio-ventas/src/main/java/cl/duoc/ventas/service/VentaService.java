package cl.duoc.ventas.service;

import cl.duoc.ventas.client.BoletasClient;
import cl.duoc.ventas.client.ProductosClient;
import cl.duoc.ventas.client.UsuariosClient;
import cl.duoc.ventas.dto.ActualizarEstadoVentaRequest;
import cl.duoc.ventas.dto.CrearVentaRequest;
import cl.duoc.ventas.dto.DetalleVentaResponse;
import cl.duoc.ventas.dto.VentaResponse;
import cl.duoc.ventas.dto.external.CrearBoletaExternalRequest;
import cl.duoc.ventas.dto.external.DetalleBoletaExternalRequest;
import cl.duoc.ventas.dto.external.ProductoExternalResponse;
import cl.duoc.ventas.dto.external.UsuarioPerfilExternalResponse;
import cl.duoc.ventas.exception.BusinessException;
import cl.duoc.ventas.exception.ResourceNotFoundException;
import cl.duoc.ventas.model.*;
import cl.duoc.ventas.repository.CarritoRepository;
import cl.duoc.ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoService carritoService;
    private final UsuariosClient usuariosClient;
    private final ProductosClient productosClient;
    private final BoletasClient boletasClient;

    @Transactional
    public VentaResponse procesarCheckout(String email, String authHeader, CrearVentaRequest request) {
        Carrito carrito = carritoService.obtenerOCrearEntidad(email);
        if (carrito.getItems().isEmpty()) {
            throw new BusinessException("No puedes finalizar la compra porque tu carrito está vacío");
        }

        UsuarioPerfilExternalResponse perfil = usuariosClient.obtenerPerfil(authHeader);

        String region = Objects.requireNonNullElse(perfil.getRegionNombre(), "Región Metropolitana");
        String comuna = Objects.requireNonNullElse(perfil.getComunaNombre(), "Santiago");
        String direccion = Objects.requireNonNullElse(perfil.getDireccion(), "Dirección no registrada");

        String lugarDespacho = resolverLugarDespacho(request, direccion, comuna, region);

        Venta venta = Venta.builder()
                .compradorNombre(perfil.getNombre())
                .compradorApellidos(perfil.getApellidos())
                .compradorEmail(perfil.getEmail())
                .compradorRegion(region)
                .compradorComuna(comuna)
                .compradorDireccion(direccion)
                .tipoDespacho(request.getTipoDespacho())
                .lugarDespacho(lugarDespacho)
                .total(0)
                .estado(EstadoVenta.PAGADA)
                .build();

        List<DetalleVenta> itemsDescontados = new ArrayList<>();
        int totalVenta = 0;

        try {
            for (DetalleCarrito itemCarrito : carrito.getItems()) {
                ProductoExternalResponse productoActual = productosClient.obtenerProducto(itemCarrito.getProductoId());

                if (productoActual.getStock() < itemCarrito.getCantidad()) {
                    throw new BusinessException("Stock insuficiente para '" + productoActual.getNombre()
                            + "'. Disponible: " + productoActual.getStock() + ", solicitado: " + itemCarrito.getCantidad());
                }

                productosClient.procesarMovimientoStock(
                        productoActual.getId(),
                        "DESCONTAR",
                        itemCarrito.getCantidad(),
                        authHeader
                );

                int subtotal = productoActual.getPrecio() * itemCarrito.getCantidad();
                DetalleVenta detalle = DetalleVenta.builder()
                        .productoId(productoActual.getId())
                        .nombreProducto(productoActual.getNombre())
                        .precioUnitario(productoActual.getPrecio())
                        .cantidad(itemCarrito.getCantidad())
                        .subtotal(subtotal)
                        .build();

                venta.agregarDetalle(detalle);
                itemsDescontados.add(detalle);
                totalVenta += subtotal;
            }

            venta.setTotal(totalVenta);
            Venta ventaGuardada = ventaRepository.save(venta);

            carrito.limpiarItems();
            carritoRepository.save(carrito);

            emitirBoletaAsociada(ventaGuardada, request.getMetodoPago(), authHeader);

            return toResponse(ventaGuardada);

        } catch (Exception ex) {
            revertirDescuentosStock(itemsDescontados, authHeader);
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> listarMisCompras(String email) {
        return ventaRepository.findByCompradorEmailOrderByFechaDesc(email).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaResponse obtenerPorId(Long id, String email, boolean esStaff) {
        Venta venta = ventaRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));

        if (!esStaff && !venta.getCompradorEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("No tienes permisos para ver esta orden de compra");
        }

        return toResponse(venta);
    }

    @Transactional(readOnly = true)
    public List<VentaResponse> listarTodas(EstadoVenta estado) {
        List<Venta> ventas = (estado != null)
                ? ventaRepository.findByEstadoOrderByFechaDesc(estado)
                : ventaRepository.findAllByOrderByFechaDesc();

        return ventas.stream().map(this::toResponse).toList();
    }

    @Transactional
    public VentaResponse actualizarEstado(Long id, ActualizarEstadoVentaRequest request) {
        Venta venta = ventaRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));

        if (venta.getEstado() == EstadoVenta.ANULADA) {
            throw new BusinessException("No se puede cambiar el estado de una venta anulada");
        }
        if (request.getEstado() == EstadoVenta.ANULADA) {
            throw new BusinessException("Para anular una venta y reponer inventario usa el endpoint PATCH /api/ventas/{id}/anular");
        }

        venta.setEstado(request.getEstado());
        return toResponse(ventaRepository.save(venta));
    }

    @Transactional
    public VentaResponse anularVenta(Long id, String authHeader) {
        Venta venta = ventaRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));

        if (venta.getEstado() == EstadoVenta.ANULADA) {
            throw new BusinessException("La venta ya se encuentra anulada");
        }

        for (DetalleVenta detalle : venta.getDetalles()) {
            productosClient.procesarMovimientoStock(
                    detalle.getProductoId(),
                    "REPONER",
                    detalle.getCantidad(),
                    authHeader
            );
        }

        venta.setEstado(EstadoVenta.ANULADA);
        Venta ventaAnulada = ventaRepository.save(venta);

        boletasClient.anularBoletaPorVenta(ventaAnulada.getId(), authHeader);

        return toResponse(ventaAnulada);
    }

    private String resolverLugarDespacho(CrearVentaRequest request, String direccion, String comuna, String region) {
        if (request.getLugarDespacho() != null && !request.getLugarDespacho().isBlank()) {
            return request.getLugarDespacho().trim();
        }
        if (request.getTipoDespacho() == TipoDespacho.DOMICILIO) {
            return direccion + ", " + comuna + ", " + region;
        }
        return "Retiro en Tienda Central Sonido Vivo - Santiago";
    }

    private void revertirDescuentosStock(List<DetalleVenta> itemsDescontados, String authHeader) {
        for (DetalleVenta item : itemsDescontados) {
            try {
                productosClient.procesarMovimientoStock(
                        item.getProductoId(),
                        "REPONER",
                        item.getCantidad(),
                        authHeader
                );
            } catch (Exception compensationEx) {
                log.error("Error al reponer stock del producto {} durante rollback de venta: {}",
                        item.getProductoId(), compensationEx.getMessage());
            }
        }
    }

    private void emitirBoletaAsociada(Venta venta, String metodoPago, String authHeader) {
        List<DetalleBoletaExternalRequest> itemsBoleta = venta.getDetalles().stream()
                .map(d -> DetalleBoletaExternalRequest.builder()
                        .productoId(d.getProductoId())
                        .nombreProducto(d.getNombreProducto())
                        .precioUnitario(d.getPrecioUnitario())
                        .cantidad(d.getCantidad())
                        .subtotal(d.getSubtotal())
                        .build())
                .toList();

        CrearBoletaExternalRequest boletaReq = CrearBoletaExternalRequest.builder()
                .ventaId(venta.getId())
                .compradorNombreCompleto(venta.getCompradorNombre() + " " + venta.getCompradorApellidos())
                .compradorEmail(venta.getCompradorEmail())
                .metodoPago(metodoPago.trim())
                .montoTotal(venta.getTotal())
                .items(itemsBoleta)
                .build();

        boletasClient.emitirBoleta(boletaReq, authHeader);
    }

    private VentaResponse toResponse(Venta venta) {
        List<DetalleVentaResponse> detallesDto = venta.getDetalles().stream()
                .map(d -> DetalleVentaResponse.builder()
                        .id(d.getId())
                        .productoId(d.getProductoId())
                        .nombreProducto(d.getNombreProducto())
                        .precioUnitario(d.getPrecioUnitario())
                        .cantidad(d.getCantidad())
                        .subtotal(d.getSubtotal())
                        .build())
                .toList();

        return VentaResponse.builder()
                .id(venta.getId())
                .compradorNombre(venta.getCompradorNombre())
                .compradorApellidos(venta.getCompradorApellidos())
                .compradorEmail(venta.getCompradorEmail())
                .compradorRegion(venta.getCompradorRegion())
                .compradorComuna(venta.getCompradorComuna())
                .compradorDireccion(venta.getCompradorDireccion())
                .fecha(venta.getFecha())
                .tipoDespacho(venta.getTipoDespacho())
                .lugarDespacho(venta.getLugarDespacho())
                .total(venta.getTotal())
                .estado(venta.getEstado())
                .detalles(detallesDto)
                .build();
    }
}