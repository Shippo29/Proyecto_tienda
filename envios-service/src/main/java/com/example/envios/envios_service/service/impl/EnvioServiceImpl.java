package com.example.envios.envios_service.service.impl;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.repository.EnvioRepository;
import com.example.envios.envios_service.service.EnvioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
        log.info("EnvioService - Saving Envio for pedidoId={}", envio.getPedidoId());
        Envio saved = envioRepository.save(envio);
        log.info("EnvioService - Saved Envio id={} for pedidoId={}", saved.getId(), saved.getPedidoId());
        return saved;
    }

    @Override
    public Envio actualizarEnvio(Long id, Envio envio) {
    Envio existente = envioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Envío no encontrado con id " + id));

    log.info("EnvioService - Actualizando envio id={} de estado={} a estado={}",
            id, existente.getEstado(), envio.getEstado());

    if (envio.getEstado() != null)        existente.setEstado(envio.getEstado());
    if (envio.getDireccion() != null)      existente.setDireccion(envio.getDireccion());
    if (envio.getTransportista() != null)  existente.setTransportista(envio.getTransportista());
    if (envio.getRutaEstimada() != null)   existente.setRutaEstimada(envio.getRutaEstimada());

    return envioRepository.save(existente);
}
}