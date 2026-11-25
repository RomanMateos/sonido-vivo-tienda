package cl.duoc.contacto.service;

import cl.duoc.contacto.dto.ActualizarEstadoContactoRequest;
import cl.duoc.contacto.dto.ContactoRequest;
import cl.duoc.contacto.dto.ContactoResponse;
import cl.duoc.contacto.exception.ResourceNotFoundException;
import cl.duoc.contacto.model.Contacto;
import cl.duoc.contacto.model.EstadoContacto;
import cl.duoc.contacto.repository.ContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactoService {

    private final ContactoRepository contactoRepository;

    @Transactional
    public ContactoResponse registrarMensaje(ContactoRequest request) {
        Contacto contacto = Contacto.builder()
                .nombre(request.getNombre().trim())
                .apellidos(request.getApellidos().trim())
                .email(request.getEmail().trim())
                .mensaje(request.getMensaje().trim())
                .estado(EstadoContacto.PENDIENTE)
                .build();

        return toResponse(contactoRepository.save(contacto));
    }

    @Transactional(readOnly = true)
    public List<ContactoResponse> listarMensajes(EstadoContacto estado) {
        List<Contacto> mensajes = (estado != null)
                ? contactoRepository.findByEstadoOrderByFechaDesc(estado)
                : contactoRepository.findAllByOrderByFechaDesc();

        return mensajes.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContactoResponse obtenerPorId(Long id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje de contacto no encontrado con ID: " + id));
        return toResponse(contacto);
    }

    @Transactional
    public ContactoResponse actualizarEstado(Long id, ActualizarEstadoContactoRequest request) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mensaje de contacto no encontrado con ID: " + id));

        contacto.setEstado(request.getEstado());
        return toResponse(contactoRepository.save(contacto));
    }

    @Transactional
    public void eliminarMensaje(Long id) {
        if (!contactoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mensaje de contacto no encontrado con ID: " + id);
        }
        contactoRepository.deleteById(id);
    }

    private ContactoResponse toResponse(Contacto contacto) {
        return ContactoResponse.builder()
                .id(contacto.getId())
                .nombre(contacto.getNombre())
                .apellidos(contacto.getApellidos())
                .email(contacto.getEmail())
                .fecha(contacto.getFecha())
                .mensaje(contacto.getMensaje())
                .estado(contacto.getEstado())
                .build();
    }
}