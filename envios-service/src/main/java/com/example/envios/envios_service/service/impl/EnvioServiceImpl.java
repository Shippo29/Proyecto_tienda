package com.example.envios.envios_service.service.impl;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.repository.EnvioRepository;
import com.example.envios.envios_service.service.EnvioService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;

    public EnvioServiceImpl(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    public List<Envio> obtenerEnvios() {
        return envioRepository.findAll();
    }

    @Override
    public Envio guardarEnvio(Envio envio) {
        return envioRepository.save(envio);
    }
}