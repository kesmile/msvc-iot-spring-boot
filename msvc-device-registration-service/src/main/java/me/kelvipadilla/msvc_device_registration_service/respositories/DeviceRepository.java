package me.kelvipadilla.msvc_device_registration_service.respositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.kelvipadilla.msvc_device_registration_service.models.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {}
