package me.kelvipadilla.telemetry_ingestion_service.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import me.kelvipadilla.telemetry_ingestion_service.dtos.DeviceResponse;

@FeignClient(name = "msvc-device-registration-service")
public interface DeviceClientService {

    // @GetMapping("/saludo/{nombre}")
    // String obtenerSaludo(@PathVariable("nombre") String nombre);

    @GetMapping("/devices/{id}")
    ResponseEntity<DeviceResponse> getDevice(@PathVariable("id") Integer id);
}
