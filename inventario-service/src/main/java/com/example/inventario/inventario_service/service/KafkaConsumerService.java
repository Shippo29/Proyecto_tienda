package com.example.inventario.inventario_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ProductoService productoService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(ProductoService productoService, ObjectMapper objectMapper) {
        this.productoService = productoService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "pedidos-events", groupId = "inventario-group")
    public void consumePedidoEvent(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String productoNombre = jsonNode.get("producto").asText();
            int cantidad = jsonNode.get("cantidad").asInt();

            // Buscar producto por nombre y reducir stock
            productoService.listarProductos().stream()
                .filter(p -> p.getNombre().equals(productoNombre))
                .findFirst()
                .ifPresent(producto -> {
                    if (producto.getStock() >= cantidad) {
                        producto.setStock(producto.getStock() - cantidad);
                        productoService.guardarProducto(producto);
                        System.out.println("[Inventario] Stock reducido para " + productoNombre + ": -" + cantidad);
                    } else {
                        System.out.println("[Inventario] Stock insuficiente para " + productoNombre);
                    }
                });
        } catch (Exception e) {
            System.err.println("[Inventario] Error procesando evento de pedido: " + e.getMessage());
        }
    }
}