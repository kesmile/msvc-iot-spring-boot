package me.kelvipadilla.data_processing_service.repositories;

import java.time.Instant;

import org.springframework.data.repository.CrudRepository;

import me.kelvipadilla.data_processing_service.models.TelemetryData;

public interface TelemetryRepository extends CrudRepository<TelemetryData, Instant> {}
