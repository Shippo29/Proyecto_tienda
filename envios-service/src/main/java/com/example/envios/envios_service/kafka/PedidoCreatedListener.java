package com.example.envios.envios_service.kafka;

import com.example.envios.envios_service.events.PedidoCreadoEvent;
import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.service.EnvioService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoCreatedListener {

    private final EnvioService envioService;

    @KafkaListener(
        topics = "pedidos.created",
        groupId = "envios-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    @CircuitBreaker(name = "kafka-envios", fallbackMethod = "fallbackCrearEnvio")
    @Retry(name = "kafka-envios")
    public void handlePedidoCreado(PedidoCreadoEvent event) {
        log.debug("EnviosService - Full event payload: {}", event);
        log.info("EnviosService - Received PedidoCreadoEvent: pedidoId={} producto={} cantidad={} cliente={}",
            event.getPedidoId(), event.getProducto(), event.getCantidad(), event.getCliente());

        Envio envio = new Envio();
        envio.setPedidoId(event.getPedidoId());
        envio.setDireccion("Por definir");
        envio.setEstado("CREADO");

        log.info("EnviosService - Creating Envio for pedidoId={}", event.getPedidoId());
        Envio saved = envioService.guardarEnvio(envio);
        log.info("EnviosService - Envio saved: id={} pedidoId={}", saved.getId(), saved.getPedidoId());
    }

    public void fallbackCrearEnvio(PedidoCreadoEvent event, Throwable ex) {
        log.error("EnviosService - [CIRCUIT BREAKER] Fallback al crear envío " +
            "pedidoId={} cliente={} causa={}",
            event.getPedidoId(), event.getCliente(), ex.getMessage());
    }
}