package com.example.envios.envios_service.service.impl;

import com.example.envios.envios_service.events.EnvioActualizadoEvent;
import com.example.envios.envios_service.events.EnvioCreadoEvent;
import com.example.envios.envios_service.exception.BadRequestException;
import com.example.envios.envios_service.exception.ResourceNotFoundException;
import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.repository.EnvioRepository;
import com.example.envios.envios_service.service.EnvioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository envioRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final Set<String> VALID_STATUSES = Set.of("CREADO", "EN_TRANSITO", "ENTREGADO", "CANCELADO");

    public EnvioServiceImpl(EnvioRepository envioRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.envioRepository = envioRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Envio> obtenerEnvios() {
        return envioRepository.findAll();
    }

    @Override
    public java.util.Optional<Envio> obtenerEnvioPorId(Long id) {
        return envioRepository.findById(id);
    }

    @Override
    public Envio guardarEnvio(Envio envio) {
        log.info("EnvioService - Saving Envio for pedidoId={}", envio.getPedidoId());

        if (envio.getEstado() != null && !VALID_STATUSES.contains(envio.getEstado())) {
            throw new BadRequestException("Estado de envío inválido: " + envio.getEstado());
        }

        Envio saved = envioRepository.save(envio);
        log.info("EnvioService - Saved Envio id={} for pedidoId={}", saved.getId(), saved.getPedidoId());

        EnvioCreadoEvent event = EnvioCreadoEvent.builder()
                .envioId(saved.getId())
                .pedidoId(saved.getPedidoId())
                .direccion(saved.getDireccion())
                .estado(saved.getEstado())
                .createdAt(java.time.Instant.now())
                .build();

        CompletableFuture<SendResult<String, Object>> sendFuture = kafkaTemplate.send("envios.created", String.valueOf(saved.getId()), event);
        sendFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("EnviosService - Failed to publish EnvioCreadoEvent for envioId={} cause={}", saved.getId(), ex.getMessage(), ex);
            } else {
                log.info("EnviosService - EnvioCreadoEvent published envioId={} topic={} partition={} offset={}", saved.getId(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });

        return saved;
    }

    @Override
    public Envio actualizarEnvio(Long id, Envio envio) {
        Envio existing = envioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado con id " + id));

        if (envio.getDireccion() != null) existing.setDireccion(envio.getDireccion());
        if (envio.getEstado() != null) {
            if (!VALID_STATUSES.contains(envio.getEstado())) {
                throw new BadRequestException("Estado de envío inválido: " + envio.getEstado());
            }
            existing.setEstado(envio.getEstado());
        }

        Envio saved = envioRepository.save(existing);

        EnvioActualizadoEvent updatedEvent = EnvioActualizadoEvent.builder()
                .envioId(saved.getId())
                .pedidoId(saved.getPedidoId())
                .direccion(saved.getDireccion())
                .estado(saved.getEstado())
                .updatedAt(java.time.Instant.now())
                .build();

        CompletableFuture<SendResult<String, Object>> sendFuture = kafkaTemplate.send("envios.updated", String.valueOf(saved.getId()), updatedEvent);
        sendFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("EnviosService - Failed to publish EnvioActualizadoEvent for envioId={} cause={}", saved.getId(), ex.getMessage(), ex);
            } else {
                log.info("EnviosService - EnvioActualizadoEvent published envioId={}", saved.getId());
            }
        });

        return saved;
    }

    @Override
    public void eliminarEnvio(Long id) {
        if (!envioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Envio no encontrado con id " + id);
        }
        envioRepository.deleteById(id);
    }
}