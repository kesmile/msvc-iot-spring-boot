package me.kelvipadilla.msvc_device_registration_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/hello")
    public String saludo() {
        return "Hola desde mi servicio cliente!";
    }
}
