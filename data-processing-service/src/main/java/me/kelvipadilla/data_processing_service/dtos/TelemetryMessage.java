package me.kelvipadilla.data_processing_service.dtos;

import java.time.Instant;

public record TelemetryMessage(
    Instant time,
    Integer deviceId,
    Double temperature,
    Double humidity
) {}
