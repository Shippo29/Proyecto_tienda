package com.example.envios.envios_service.controller;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.service.EnvioService;
import com.example.envios.envios_service.service.KafkaProducerService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final EnvioService envioService;
    private final KafkaProducerService kafkaProducerService;

    public EnvioController(EnvioService envioService, KafkaProducerService kafkaProducerService) {
        this.envioService = envioService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping
    public List<Envio> obtenerEnvios() {
        return envioService.obtenerEnvios();
    }

    @PostMapping
    public Envio guardarEnvio(@RequestBody Envio envio) {
        Envio envioGuardado = envioService.guardarEnvio(envio);
        kafkaProducerService.send("envios-events", envioGuardado);
        return envioGuardado;
    }
}