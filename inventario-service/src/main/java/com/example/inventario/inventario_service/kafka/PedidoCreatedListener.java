package com.example.inventario.inventario_service.kafka;

import com.example.inventario.inventario_service.events.PedidoCreadoEvent;
import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.repository.ProductoRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoCreatedListener {

    private final ProductoRepository productoRepository;

    @KafkaListener(topics = "pedidos.created", groupId = "inventario-group", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void handlePedidoCreado(PedidoCreadoEvent event) {
        log.debug("InventarioService - Full event payload: {}", event);
        log.info("InventarioService - Received PedidoCreadoEvent: pedidoId={} producto={} cantidad={}", event.getPedidoId(), event.getProducto(), event.getCantidad());

        productoRepository.findAll().stream()
            .filter(p -> p.getNombre().equalsIgnoreCase(event.getProducto()))
            .findFirst()
            .ifPresentOrElse(p -> {
                log.info("InventarioService - Found product id={} nombre={} stock={} for pedidoId={}", p.getId(), p.getNombre(), p.getStock(), event.getPedidoId());
                int newStock = p.getStock() - event.getCantidad();
                log.info("InventarioService - Decrementing stock for productoId={} by {} -> newStock={}", p.getId(), event.getCantidad(), newStock);
                p.setStock(newStock);
                Producto saved = productoRepository.save(p);
                log.info("InventarioService - Product updated id={} newStock={}", saved.getId(), saved.getStock());
            }, () -> {
                log.warn("InventarioService - Producto not found: nombre={} for pedidoId={}", event.getProducto(), event.getPedidoId());
            });
    }
}
