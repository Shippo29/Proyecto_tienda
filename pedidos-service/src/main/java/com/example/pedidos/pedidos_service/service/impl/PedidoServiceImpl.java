package com.example.pedidos.pedidos_service.service.impl;

import com.example.pedidos.pedidos_service.events.PedidoCreadoEvent;
import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.repository.PedidoRepository;
import com.example.pedidos.pedidos_service.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository repository;
    private final KafkaTemplate<String, PedidoCreadoEvent> kafkaTemplate;
    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    public PedidoServiceImpl(PedidoRepository repository, KafkaTemplate<String, PedidoCreadoEvent> kafkaTemplate) {
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

        ListenableFuture<SendResult<String, PedidoCreadoEvent>> future = (ListenableFuture<SendResult<String, PedidoCreadoEvent>>) kafkaTemplate.send("pedidos.created", String.valueOf(saved.getId()), event);
        future.addCallback(new ListenableFutureCallback<SendResult<String, PedidoCreadoEvent>>() {
            @Override
            public void onSuccess(SendResult<String, PedidoCreadoEvent> result) {
                log.info("PedidosService - Published PedidoCreadoEvent for pedidoId={} topic={} partition={} offset={}", saved.getId(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("PedidosService - Failed to publish PedidoCreadoEvent for pedidoId={} cause={}", saved.getId(), ex.getMessage(), ex);
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

        Pedido pedidoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedidoExistente.setCliente(pedido.getCliente());
        pedidoExistente.setTotal(pedido.getTotal());

        return repository.save(pedidoExistente);
    }

    @Override
    public void eliminarPedido(Long id) {
        repository.deleteById(id);
    }
}