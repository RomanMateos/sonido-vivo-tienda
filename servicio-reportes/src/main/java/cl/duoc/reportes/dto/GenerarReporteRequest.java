package cl.duoc.reportes.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GenerarReporteRequest {

    @Size(max = 150, message = "El título del reporte no puede superar los 150 caracteres")
    private String titulo;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;
}