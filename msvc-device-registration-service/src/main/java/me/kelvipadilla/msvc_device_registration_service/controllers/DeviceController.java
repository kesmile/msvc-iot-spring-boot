package me.kelvipadilla.msvc_device_registration_service.controllers;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import me.kelvipadilla.msvc_device_registration_service.dtos.DeviceDto;
import me.kelvipadilla.msvc_device_registration_service.models.Device;
import me.kelvipadilla.msvc_device_registration_service.services.DeviceService;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    @CircuitBreaker(name = "devices", fallbackMethod = "fallbackCreateDevice")
    public ResponseEntity<Device> createDevice(@Valid @RequestBody DeviceDto deviceDto) {
        Device createdClient = deviceService.create(deviceDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @GetMapping
    @CircuitBreaker(name = "devices", fallbackMethod = "fallbackGetAllDevices")
    public Page<Device> getAllDevices(Pageable pageable) {
        return deviceService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @CircuitBreaker(name = "devices", fallbackMethod = "fallbackGetDeviceById")
    public ResponseEntity<Device> getDeviceById(@PathVariable Integer id) {
        return deviceService.findById(id)
            .map(device -> ResponseEntity.ok(device))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @CircuitBreaker(name = "devices", fallbackMethod = "fallbackUpdateDevice")
    public ResponseEntity<Device> updateDevice(@PathVariable Integer id, @Valid @RequestBody DeviceDto deviceDto) {
        return deviceService.update(id, deviceDto)
            .map(device -> ResponseEntity.ok(device))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "devices", fallbackMethod = "fallbackDeleteDevice")
    public ResponseEntity<Void> deleteDevice(@PathVariable Integer id) {
        boolean deleted = deviceService.delete(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<String> fallbackGetDeviceById(@PathVariable Integer id, Throwable t) {
        return new ResponseEntity<>("El servicio de consulta no está disponible en este momento. Por favor, intente más tarde.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<String> fallbackUpdateDevice(@PathVariable Integer id, @Valid @RequestBody DeviceDto deviceDto, Throwable t) {
        return new ResponseEntity<>("El servicio de actualización no está disponible en este momento. Por favor, intente más tarde.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<String> fallbackDeleteDevice(@PathVariable Integer id, Throwable t) {
        return new ResponseEntity<>("El servicio de eliminación no está disponible en este momento. Por favor, intente más tarde.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<String> fallbackCreateDevice(@Valid @RequestBody DeviceDto deviceDto, Throwable t) {
        return new ResponseEntity<>("El servicio de creación no está disponible en este momento. Por favor, intente más tarde.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public Page<Device> fallbackGetAllDevices(Pageable pageable, Throwable t) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }
}
