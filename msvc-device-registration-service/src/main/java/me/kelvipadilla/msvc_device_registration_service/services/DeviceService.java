package me.kelvipadilla.msvc_device_registration_service.services;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import me.kelvipadilla.msvc_device_registration_service.respositories.DeviceRepository;
import me.kelvipadilla.msvc_device_registration_service.dtos.DeviceDto;
import me.kelvipadilla.msvc_device_registration_service.models.Device;

@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Page<Device> findAll(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }

    public Optional<Device> findById(Integer id) {
        return deviceRepository.findById(id);
    }

    public Device create(DeviceDto device) {
        Device newDevice = new Device();
        newDevice.setName(device.getName());
        newDevice.setStatus(device.getStatus());
        newDevice.setDescription(device.getDescription());
        newDevice.setType(device.getType());

        return deviceRepository.save(newDevice);
    }

    public Optional<Device> update(Integer id, DeviceDto deviceDto) {
        return deviceRepository.findById(id).map(device -> {
            device.setName(deviceDto.getName());
            device.setStatus(deviceDto.getStatus());
            device.setDescription(deviceDto.getDescription());
            device.setType(deviceDto.getType());

            return deviceRepository.save(device);
        });
    }

    public boolean delete(Integer id) {
       return deviceRepository.findById(id)
                .map(device -> {
                    deviceRepository.delete(device);
                    return true;
                }).orElse(false);
    }
}
