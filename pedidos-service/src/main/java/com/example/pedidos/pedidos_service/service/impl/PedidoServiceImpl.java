package com.example.pedidos.pedidos_service.service.impl;

import com.example.pedidos.pedidos_service.events.PedidoCreadoEvent;
import com.example.pedidos.pedidos_service.events.PedidoActualizadoEvent;
import com.example.pedidos.pedidos_service.exception.ResourceNotFoundException;
import com.example.pedidos.pedidos_service.exception.BadRequestException;
import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.repository.PedidoRepository;
import com.example.pedidos.pedidos_service.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    public PedidoServiceImpl(PedidoRepository repository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Pedido> listarPedidos() {
        return repository.findAll();
    }

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        log.info("PedidosService - Saving Pedido: cliente={}, producto={}, cantidad={}, total={}", pedido.getCliente(), pedido.getProducto(), pedido.getCantidad(), pedido.getTotal());

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

        log.info("PedidosService - Publishing PedidoCreadoEvent to topic='pedidos.created' key={}", saved.getId());
        log.debug("PedidosService - Event payload: {}", event);

        CompletableFuture<SendResult<String, Object>> sendFuture = kafkaTemplate.send("pedidos.created", String.valueOf(saved.getId()), event);

        sendFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("PedidosService - Failed to publish PedidoCreadoEvent for pedidoId={} cause={}", saved.getId(), ex.getMessage(), ex);
            } else {
                log.info("PedidosService - Published PedidoCreadoEvent for pedidoId={} topic={} partition={} offset={}", saved.getId(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });

        return saved;
    }

    @Override
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Pedido actualizarPedido(Long id, Pedido pedido) {

        if (id == null) {
            throw new BadRequestException("El ID no puede ser nulo");
        }

        Pedido pedidoExistente = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id " + id));

        // Actualizar todos los campos relevantes
        pedidoExistente.setCliente(pedido.getCliente());
        pedidoExistente.setProducto(pedido.getProducto());
        pedidoExistente.setCantidad(pedido.getCantidad());
        pedidoExistente.setTotal(pedido.getTotal());

        Pedido saved = repository.save(pedidoExistente);

        // Publicar evento de pedido actualizado
        PedidoActualizadoEvent updatedEvent = PedidoActualizadoEvent.builder()
                .pedidoId(saved.getId())
                .cliente(saved.getCliente())
                .producto(saved.getProducto())
                .cantidad(saved.getCantidad())
                .total(saved.getTotal())
                .updatedAt(java.time.Instant.now())
                .build();

        CompletableFuture<SendResult<String, Object>> sendFuture = kafkaTemplate.send("pedidos.updated", String.valueOf(saved.getId()), updatedEvent);

        sendFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("PedidosService - Failed to publish PedidoActualizadoEvent for pedidoId={} cause={}", saved.getId(), ex.getMessage(), ex);
            } else {
                log.info("PedidosService - Published PedidoActualizadoEvent for pedidoId={}", saved.getId());
            }
        });

        return saved;
    }

    @Override
    public void eliminarPedido(Long id) {
        if (id == null) {
            throw new BadRequestException("El ID no puede ser nulo");
        }
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pedido no encontrado con id " + id);
        }
        repository.deleteById(id);
    }
}