package me.kelvipadilla.telemetry_ingestion_service.controllers;

import java.time.Instant;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import me.kelvipadilla.telemetry_ingestion_service.dtos.CustomMessage;
import me.kelvipadilla.telemetry_ingestion_service.dtos.DeviceResponse;
import me.kelvipadilla.telemetry_ingestion_service.dtos.TelemetryData;
import me.kelvipadilla.telemetry_ingestion_service.dtos.TelemetryMessage;
import me.kelvipadilla.telemetry_ingestion_service.services.DeviceClientService;
import me.kelvipadilla.telemetry_ingestion_service.services.MessageProducerService;

@RestController
@RequestMapping("/telemetry")
public class TelemetryController {

    private final DeviceClientService deviceClientService;
    private final MessageProducerService messageProducer;

    public TelemetryController(DeviceClientService deviceClientService, MessageProducerService messageProducer) {
        this.deviceClientService = deviceClientService;
        this.messageProducer = messageProducer;
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "devices", fallbackMethod = "fallbackGetDevice")
    public ResponseEntity<Void> get(@PathVariable Integer id) {
        ResponseEntity<DeviceResponse> resp = deviceClientService.getDevice(id);
        if (resp.getStatusCode().is2xxSuccessful()) {
            DeviceResponse device = resp.getBody();
            
            if (device.status().equals("Activo")) {
                CustomMessage message = new CustomMessage(device.id().toString(), "Prueba");
                messageProducer.sendMessage("test-topic", message);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }

        if (resp.getStatusCode().value() == 404) {
            return ResponseEntity.notFound().build();
        }

        throw new RuntimeException("Unexpected status: " + resp.getStatusCode());
    }


    @PostMapping("/{deviceId}")
    //@CircuitBreaker(name = "devices", fallbackMethod = "fallbackProcessTelemetry")
    public ResponseEntity<String> processTelemetry(
            @PathVariable Integer deviceId, 
            @Valid @RequestBody TelemetryData telemetryData) {
        
        ResponseEntity<DeviceResponse> resp = deviceClientService.getDevice(deviceId);
        
        if (resp.getStatusCode().is2xxSuccessful()) {
            DeviceResponse device = resp.getBody();
            
            if (device.status().equals("Activo")) {
                Instant timestamp = Instant.now();
                
                TelemetryMessage telemetryMessage = new TelemetryMessage(
                    timestamp,
                    deviceId,
                    telemetryData.temperature(),
                    telemetryData.humidity()
                );
                
                messageProducer.sendMessage("telemetry-topic", telemetryMessage);
                return ResponseEntity.ok("Telemetry data processed successfully");
            } else {
                return ResponseEntity.badRequest().body("Device is not active");
            }
        }

        if (resp.getStatusCode().value() == 404) {
            return ResponseEntity.notFound().build();
        }

        throw new RuntimeException("Unexpected status: " + resp.getStatusCode());
    }

    public ResponseEntity<String> fallbackProcessTelemetry(Integer deviceId, TelemetryData telemetryData, Throwable t) {
        return ResponseEntity.status(503).body("Telemetry service temporarily unavailable");
    }

    public Optional<DeviceResponse> fallbackGetDevice(Integer id, Throwable t) {
        return Optional.empty();
    }
}
