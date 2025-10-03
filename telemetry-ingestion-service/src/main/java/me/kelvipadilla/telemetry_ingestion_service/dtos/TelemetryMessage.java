package me.kelvipadilla.telemetry_ingestion_service.dtos;

import java.time.Instant;

public record TelemetryMessage(
    Instant time,
    Integer deviceId,
    Double temperature,
    Double humidity
) {

}
