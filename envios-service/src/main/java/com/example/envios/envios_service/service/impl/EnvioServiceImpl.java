package com.example.envios.envios_service.service.impl;

import com.example.envios.envios_service.exception.ResourceNotFoundException;
import com.example.envios.envios_service.events.EnvioActualizadoEvent;
import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.repository.EnvioRepository;
import com.example.envios.envios_service.service.EnvioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;
    private final KafkaTemplate<String, EnvioActualizadoEvent> kafkaTemplate;

    public EnvioServiceImpl(EnvioRepository envioRepository, KafkaTemplate<String, EnvioActualizadoEvent> kafkaTemplate) {
        this.envioRepository = envioRepository;
        this.kafkaTemplate = kafkaTemplate;
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

        // Publicar evento
        EnvioActualizadoEvent event = EnvioActualizadoEvent.builder()
                .envioId(saved.getId())
                .pedidoId(saved.getPedidoId())
                .direccion(saved.getDireccion())
                .estado(saved.getEstado())
                .updatedAt(Instant.now())
                .build();
        kafkaTemplate.send("envios-events", String.valueOf(saved.getId()), event);

        return saved;
    }

    @Override
    public Optional<Envio> obtenerEnvioPorId(Long id) {
        return envioRepository.findById(id);
    }

    @Override
    public Envio actualizarEnvio(Long id, Envio envio) {
        Envio envioExistente = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado"));
        envioExistente.setPedidoId(envio.getPedidoId());
        envioExistente.setDireccion(envio.getDireccion());
        envioExistente.setEstado(envio.getEstado());
        Envio updated = envioRepository.save(envioExistente);

        EnvioActualizadoEvent event = EnvioActualizadoEvent.builder()
                .envioId(updated.getId())
                .pedidoId(updated.getPedidoId())
                .direccion(updated.getDireccion())
                .estado(updated.getEstado())
                .updatedAt(Instant.now())
                .build();
        kafkaTemplate.send("envios-events", String.valueOf(updated.getId()), event);

        return updated;
    }

    @Override
    public void eliminarEnvio(Long id) {
        if (!envioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Envio no encontrado");
        }
        envioRepository.deleteById(id);
        log.info("EnvioService - Envio deleted: id={}", id);
    }
}