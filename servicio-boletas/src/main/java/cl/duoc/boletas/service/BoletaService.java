package cl.duoc.boletas.service;

import cl.duoc.boletas.dto.BoletaResponse;
import cl.duoc.boletas.dto.CrearBoletaRequest;
import cl.duoc.boletas.dto.DetalleBoletaRequest;
import cl.duoc.boletas.dto.DetalleBoletaResponse;
import cl.duoc.boletas.exception.BusinessException;
import cl.duoc.boletas.exception.ResourceNotFoundException;
import cl.duoc.boletas.model.Boleta;
import cl.duoc.boletas.model.DetalleBoleta;
import cl.duoc.boletas.model.EstadoBoleta;
import cl.duoc.boletas.repository.BoletaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoletaService {

    private static final double FACTOR_IVA = 1.19;

    private final BoletaRepository boletaRepository;

    @Transactional
    public BoletaResponse emitirBoleta(CrearBoletaRequest request) {
        if (boletaRepository.existsByVentaId(request.getVentaId())) {
            throw new BusinessException("Ya existe una boleta emitida para la venta ID: " + request.getVentaId());
        }

        int montoTotal = request.getMontoTotal();
        int montoNeto = (int) Math.round(montoTotal / FACTOR_IVA);
        int iva = montoTotal - montoNeto;

        Boleta boleta = Boleta.builder()
                .ventaId(request.getVentaId())
                .compradorNombreCompleto(request.getCompradorNombreCompleto().trim())
                .compradorEmail(request.getCompradorEmail().trim())
                .metodoPago(request.getMetodoPago().trim())
                .montoNeto(montoNeto)
                .iva(iva)
                .montoTotal(montoTotal)
                .estado(EstadoBoleta.EMITIDA)
                .build();

        for (DetalleBoletaRequest item : request.getItems()) {
            DetalleBoleta detalle = DetalleBoleta.builder()
                    .productoId(item.getProductoId())
                    .nombreProducto(item.getNombreProducto().trim())
                    .precioUnitario(item.getPrecioUnitario())
                    .cantidad(item.getCantidad())
                    .subtotal(item.getSubtotal())
                    .build();
            boleta.agregarDetalle(detalle);
        }

        return toResponse(boletaRepository.save(boleta));
    }

    @Transactional(readOnly = true)
    public BoletaResponse obtenerPorFolio(Long folio, String email, boolean esStaff) {
        Boleta boleta = boletaRepository.findWithDetallesById(folio)
                .orElseThrow(() -> new ResourceNotFoundException("Boleta no encontrada con folio: " + folio));

        validarPropietarioOStaff(boleta, email, esStaff);
        return toResponse(boleta);
    }

    @Transactional(readOnly = true)
    public BoletaResponse obtenerPorVentaId(Long ventaId, String email, boolean esStaff) {
        Boleta boleta = boletaRepository.findWithDetallesByVentaId(ventaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe boleta asociada a la venta ID: " + ventaId));

        validarPropietarioOStaff(boleta, email, esStaff);
        return toResponse(boleta);
    }

    @Transactional(readOnly = true)
    public List<BoletaResponse> listarMisBoletas(String email) {
        return boletaRepository.findByCompradorEmailOrderByFechaEmisionDesc(email).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BoletaResponse> listarTodas() {
        return boletaRepository.findAllByOrderByFechaEmisionDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public BoletaResponse anularPorVentaId(Long ventaId) {
        Boleta boleta = boletaRepository.findWithDetallesByVentaId(ventaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe boleta asociada a la venta ID: " + ventaId));

        if (boleta.getEstado() == EstadoBoleta.ANULADA) {
            throw new BusinessException("La boleta folio " + boleta.getId() + " ya se encuentra anulada");
        }

        boleta.setEstado(EstadoBoleta.ANULADA);
        return toResponse(boletaRepository.save(boleta));
    }

    private void validarPropietarioOStaff(Boleta boleta, String email, boolean esStaff) {
        if (!esStaff && !boleta.getCompradorEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("No se poseen permisos para consultar esta boleta");
        }
    }

    private BoletaResponse toResponse(Boleta boleta) {
        List<DetalleBoletaResponse> detallesDto = boleta.getDetalles().stream()
                .map(d -> DetalleBoletaResponse.builder()
                        .id(d.getId())
                        .productoId(d.getProductoId())
                        .nombreProducto(d.getNombreProducto())
                        .precioUnitario(d.getPrecioUnitario())
                        .cantidad(d.getCantidad())
                        .subtotal(d.getSubtotal())
                        .build())
                .toList();

        return BoletaResponse.builder()
                .folio(boleta.getId())
                .ventaId(boleta.getVentaId())
                .fechaEmision(boleta.getFechaEmision())
                .compradorNombreCompleto(boleta.getCompradorNombreCompleto())
                .compradorEmail(boleta.getCompradorEmail())
                .metodoPago(boleta.getMetodoPago())
                .montoNeto(boleta.getMontoNeto())
                .iva(boleta.getIva())
                .montoTotal(boleta.getMontoTotal())
                .estado(boleta.getEstado())
                .detalles(detallesDto)
                .build();
    }
}