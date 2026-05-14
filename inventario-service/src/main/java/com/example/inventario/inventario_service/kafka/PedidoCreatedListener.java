package com.example.inventario.inventario_service.kafka;

import com.example.inventario.inventario_service.events.PedidoCreadoEvent;
import com.example.inventario.inventario_service.repository.ProductoRepository;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoCreatedListener {

    private final ProductoRepository productoRepository;

    @KafkaListener(topics = "pedidos.created", groupId = "inventario-group", containerFactory = "kafkaListenerContainerFactory")
    public void handlePedidoCreado(PedidoCreadoEvent event) {
    log.info("InventarioService - Received PedidoCreadoEvent: pedidoId={} producto={}", event.getPedidoId(), event.getProducto());

    productoRepository.findByNombreIgnoreCase(event.getProducto())
        .ifPresentOrElse(p -> {
            if (p.getStock() < event.getCantidad()) {
                log.warn("Stock insuficiente para producto={}", p.getNombre());
            } else {
                p.setStock(p.getStock() - event.getCantidad());
                productoRepository.save(p);
                log.info("Stock actualizado para producto={} newStock={}", p.getNombre(), p.getStock());
            }
        }, () -> log.warn("Producto no encontrado: {}", event.getProducto()));
    }
}