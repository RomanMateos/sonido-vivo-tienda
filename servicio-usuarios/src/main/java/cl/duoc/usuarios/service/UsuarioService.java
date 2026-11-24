package cl.duoc.usuarios.service;

import cl.duoc.usuarios.dto.RegistroClienteRequest;
import cl.duoc.usuarios.dto.UsuarioAdminRequest;
import cl.duoc.usuarios.dto.UsuarioAuthSyncRequest;
import cl.duoc.usuarios.dto.UsuarioResponse;
import cl.duoc.usuarios.model.Comuna;
import cl.duoc.usuarios.model.RegistroUsuario;
import cl.duoc.usuarios.model.Rol;
import cl.duoc.usuarios.repository.ComunaRepository;
import cl.duoc.usuarios.repository.RegistroUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final RegistroUsuarioRepository usuarioRepository;
    private final ComunaRepository comunaRepository;
    private final RestClient authRestClient;

    @Transactional
    public UsuarioResponse registrarCliente(RegistroClienteRequest request) {
        String runNormalizado = request.getRun().trim().toUpperCase();
        String emailNormalizado = request.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByRun(runNormalizado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El RUN ya se encuentra registrado");
        }
        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya se encuentra registrado");
        }

        Comuna comuna = comunaRepository.findById(request.getComunaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La comuna seleccionada no existe"));

        RegistroUsuario nuevo = RegistroUsuario.builder()
                .run(runNormalizado)
                .nombre(request.getNombre().trim())
                .apellidos(request.getApellidos().trim())
                .email(emailNormalizado)
                .telefono(request.getTelefono() != null ? request.getTelefono().trim() : null)
                .direccion(request.getDireccion().trim())
                .comuna(comuna)
                .rol(Rol.CLIENTE)
                .activo(true)
                .build();

        RegistroUsuario guardado = usuarioRepository.save(nuevo);
        sincronizarConAuth(guardado.getEmail(), request.getContrasena(), guardado.getRol(), guardado.getActivo());

        return mapearAResponse(guardado);
    }

    @Transactional
    public UsuarioResponse crearUsuarioAdmin(UsuarioAdminRequest request) {
        if (request.getContrasena() == null || request.getContrasena().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria al crear un usuario");
        }

        String runNormalizado = request.getRun().trim().toUpperCase();
        String emailNormalizado = request.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByRun(runNormalizado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El RUN ya se encuentra registrado");
        }
        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya se encuentra registrado");
        }

        Comuna comuna = comunaRepository.findById(request.getComunaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La comuna seleccionada no existe"));

        boolean estadoActivo = request.getActivo() != null ? request.getActivo() : true;

        RegistroUsuario nuevo = RegistroUsuario.builder()
                .run(runNormalizado)
                .nombre(request.getNombre().trim())
                .apellidos(request.getApellidos().trim())
                .email(emailNormalizado)
                .telefono(request.getTelefono() != null ? request.getTelefono().trim() : null)
                .direccion(request.getDireccion().trim())
                .comuna(comuna)
                .rol(request.getRol())
                .activo(estadoActivo)
                .build();

        RegistroUsuario guardado = usuarioRepository.save(nuevo);
        sincronizarConAuth(guardado.getEmail(), request.getContrasena(), guardado.getRol(), guardado.getActivo());

        return mapearAResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        RegistroUsuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return mapearAResponse(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorEmail(String email) {
        RegistroUsuario usuario = usuarioRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de usuario no encontrado"));
        return mapearAResponse(usuario);
    }

    @Transactional
    public UsuarioResponse actualizarUsuario(Long id, UsuarioAdminRequest request) {
        RegistroUsuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        String runNormalizado = request.getRun().trim().toUpperCase();
        String emailNormalizado = request.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByRunAndIdNot(runNormalizado, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El RUN ya pertenece a otro usuario");
        }
        if (usuarioRepository.existsByEmailAndIdNot(emailNormalizado, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya pertenece a otro usuario");
        }

        Comuna comuna = comunaRepository.findById(request.getComunaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La comuna seleccionada no existe"));

        existente.setRun(runNormalizado);
        existente.setNombre(request.getNombre().trim());
        existente.setApellidos(request.getApellidos().trim());
        existente.setEmail(emailNormalizado);
        existente.setTelefono(request.getTelefono() != null ? request.getTelefono().trim() : null);
        existente.setDireccion(request.getDireccion().trim());
        existente.setComuna(comuna);
        existente.setRol(request.getRol());
        if (request.getActivo() != null) {
            existente.setActivo(request.getActivo());
        }

        RegistroUsuario actualizado = usuarioRepository.save(existente);
        sincronizarConAuth(
                actualizado.getEmail(),
                request.getContrasena(),
                actualizado.getRol(),
                actualizado.getActivo()
        );

        return mapearAResponse(actualizado);
    }

    @Transactional
    public UsuarioResponse cambiarEstado(Long id, boolean activo) {
        RegistroUsuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        existente.setActivo(activo);
        RegistroUsuario actualizado = usuarioRepository.save(existente);
        sincronizarConAuth(actualizado.getEmail(), null, actualizado.getRol(), actualizado.getActivo());

        return mapearAResponse(actualizado);
    }

    private void sincronizarConAuth(String email, String contrasena, Rol rol, Boolean activo) {
        UsuarioAuthSyncRequest syncPayload = UsuarioAuthSyncRequest.builder()
                .email(email)
                .contrasena(contrasena)
                .rol(rol)
                .activo(activo)
                .build();

        try {
            authRestClient.post()
                    .uri("/api/auth/sincronizar")
                    .body(syncPayload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No se pudo sincronizar las credenciales con el servicio de autenticación"
            );
        }
    }

    private UsuarioResponse mapearAResponse(RegistroUsuario u) {
        return UsuarioResponse.builder()
                .id(u.getId())
                .run(u.getRun())
                .nombre(u.getNombre())
                .apellidos(u.getApellidos())
                .email(u.getEmail())
                .telefono(u.getTelefono())
                .direccion(u.getDireccion())
                .comunaId(u.getComuna().getId())
                .comunaNombre(u.getComuna().getNombre())
                .regionId(u.getComuna().getRegion().getId())
                .regionNombre(u.getComuna().getRegion().getNombre())
                .rol(u.getRol())
                .activo(u.getActivo())
                .fechaRegistro(u.getFechaRegistro())
                .build();
    }
}
