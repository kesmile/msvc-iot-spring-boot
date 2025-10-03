package me.kelvipadilla.telemetry_ingestion_service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record TelemetryData(
    @NotNull
    @DecimalMin(value = "-50.0", message = "Temperature must be above -50°C")
    @DecimalMax(value = "100.0", message = "Temperature must be below 100°C")
    Double temperature,
    
    @NotNull
    @DecimalMin(value = "0.0", message = "Humidity must be above 0%")
    @DecimalMax(value = "100.0", message = "Humidity must be below 100%")
    Double humidity
)  {}
