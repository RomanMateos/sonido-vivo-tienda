package cl.duoc.usuarios.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RunValidator implements ConstraintValidator<RunValido, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        String runLimpio = value.trim().toUpperCase();
        if (!runLimpio.matches("^[0-9]{6,8}[0-9K]$")) {
            return false;
        }

        String cuerpo = runLimpio.substring(0, runLimpio.length() - 1);
        char dvIngresado = runLimpio.charAt(runLimpio.length() - 1);

        int suma = 0;
        int multiplicador = 2;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(cuerpo.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int resto = 11 - (suma % 11);
        char dvEsperado = switch (resto) {
            case 11 -> '0';
            case 10 -> 'K';
            default -> (char) ('0' + resto);
        };

        return dvIngresado == dvEsperado;
    }
}