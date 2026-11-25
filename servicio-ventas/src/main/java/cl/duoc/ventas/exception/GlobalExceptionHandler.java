package cl.duoc.ventas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity< Map<String, Object> >  handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity< Map<String, Object> >  handleBusiness(BusinessException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity< Map<String, Object> >  handleRestClientResponse(RestClientResponseException ex) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }
        String mensaje = "Error al comunicarse con servicio externo (" + status.value() + "): " + ex.getResponseBodyAsString();
        return buildResponse(status, mensaje);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity< Map<String, Object> >  handleResourceAccess(ResourceAccessException ex) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE,
                "No fue posible conectar con uno de los microservicios dependientes. Verifica que estén activos.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity< Map<String, Object> >  handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de validación");
        body.put("detalles", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity< Map<String, Object> >  handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta operación");
    }

    private ResponseEntity< Map<String, Object> >  buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("mensaje", message);
        return ResponseEntity.status(status).body(body);
    }
}
