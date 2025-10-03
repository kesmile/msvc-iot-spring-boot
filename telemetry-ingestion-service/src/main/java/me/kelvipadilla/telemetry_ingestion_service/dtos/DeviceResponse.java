package me.kelvipadilla.telemetry_ingestion_service.dtos;

public record DeviceResponse(
    Integer id,
    String name,
    String description,
    String status,
    String type
) {}
