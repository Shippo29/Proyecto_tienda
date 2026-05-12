package com.example.envios.envios_service.controller;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.service.EnvioService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    public List<Envio> obtenerEnvios() {
        return envioService.obtenerEnvios();
    }

    @PostMapping
    public Envio guardarEnvio(@RequestBody Envio envio) {
        return envioService.guardarEnvio(envio);
    }
}