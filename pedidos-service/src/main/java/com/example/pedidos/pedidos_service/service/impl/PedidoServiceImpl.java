package com.example.pedidos.pedidos_service.service.impl;

import com.example.pedidos.pedidos_service.events.PedidoCreadoEvent;
import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.repository.PedidoRepository;
import com.example.pedidos.pedidos_service.service.PedidoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final KafkaTemplate<String, PedidoCreadoEvent> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    public PedidoServiceImpl(PedidoRepository repository,
                KafkaTemplate<String, PedidoCreadoEvent> kafkaTemplate) {
        this.repository    = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Pedido> listarPedidos() {
        return repository.findAll();
    }

    @Override
    @CircuitBreaker(name = "kafka-pedidos", fallbackMethod = "fallbackPublicarEvento")
    @Retry(name = "kafka-pedidos")
    public Pedido guardarPedido(Pedido pedido) {
        log.info("PedidosService - Saving Pedido: cliente={} producto={} cantidad={} total={}",
            pedido.getCliente(), pedido.getProducto(), pedido.getCantidad(), pedido.getTotal());

        Pedido saved = repository.save(pedido);
        log.info("PedidosService - Pedido saved: id={}", saved.getId());

        PedidoCreadoEvent event = PedidoCreadoEvent.builder()
            .pedidoId(saved.getId())
            .cliente(saved.getCliente())
            .producto(saved.getProducto())
            .cantidad(saved.getCantidad())
            .total(saved.getTotal())
            .createdAt(Instant.now())
            .build();

        log.info("PedidosService - Publishing PedidoCreadoEvent topic='pedidos.created' key={}", saved.getId());

        kafkaTemplate.send("pedidos.created", String.valueOf(saved.getId()), event)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("PedidosService - Published event pedidoId={} partition={} offset={}",
                        saved.getId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("PedidosService - Failed to publish event pedidoId={} cause={}",
                        saved.getId(), ex.getMessage(), ex);
                }
            });

        return saved;
    }

    public Pedido fallbackPublicarEvento(Pedido pedido, Throwable ex) {
        log.error("PedidosService - [CIRCUIT BREAKER] Fallback al publicar evento Kafka " +
            "cliente={} producto={} causa={}", pedido.getCliente(), pedido.getProducto(), ex.getMessage());
        return repository.findAll().stream()
            .filter(p -> p.getCliente().equals(pedido.getCliente())
                    && p.getProducto().equals(pedido.getProducto()))
            .reduce((first, second) -> second)
            .orElse(pedido);
    }

    @Override
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Pedido actualizarPedido(Long id, Pedido pedido) {
        Pedido existente = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        existente.setCliente(pedido.getCliente());
        existente.setTotal(pedido.getTotal());
        return repository.save(existente);
    }

    @Override
    public void eliminarPedido(Long id) {
        repository.deleteById(id);
    }
}