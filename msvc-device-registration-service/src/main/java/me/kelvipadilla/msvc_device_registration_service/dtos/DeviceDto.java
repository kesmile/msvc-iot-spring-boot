package me.kelvipadilla.msvc_device_registration_service.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeviceDto {
    private Integer id;
    private String name;
    private String description;
    private String status;
    private String type;
}
