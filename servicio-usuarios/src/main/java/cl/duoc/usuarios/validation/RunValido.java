package cl.duoc.usuarios.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RunValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RunValido {
    String message() default "El RUN ingresado no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}