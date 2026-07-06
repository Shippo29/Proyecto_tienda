package com.example.envios.envios_service.controller;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.service.EnvioService;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/envios")
@Slf4j
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    public List<Envio> obtenerEnvios() {
        log.debug("EnvioController - GET /envios");
        return envioService.obtenerEnvios();
    }

    @PostMapping
    public Envio guardarEnvio(@RequestBody Envio envio) {
        log.debug("EnvioController - POST /envios body: pedidoId={} direccion={} estado={} ", envio.getPedidoId(), envio.getDireccion(), envio.getEstado());
        return envioService.guardarEnvio(envio);
    }
}
