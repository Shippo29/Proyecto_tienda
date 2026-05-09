package com.example.pedidos.pedidos_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final PedidoService pedidoService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(PedidoService pedidoService, ObjectMapper objectMapper) {
        this.pedidoService = pedidoService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "inventario-events", groupId = "pedidos-group")
    public void consumeInventarioEvent(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            String nombreProducto = jsonNode.get("nombre").asText();
            System.out.println("[Pedidos] Producto actualizado en inventario: " + nombreProducto);
            // Aquí podrías actualizar algo en pedidos si es necesario
        } catch (Exception e) {
            System.err.println("[Pedidos] Error procesando evento de inventario: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "envios-events", groupId = "pedidos-group")
    public void consumeEnviosEvent(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            Long pedidoId = jsonNode.get("pedidoId").asLong();
            String estado = jsonNode.get("estado").asText();
            System.out.println("[Pedidos] Envío actualizado para pedido " + pedidoId + ": " + estado);
            // Aquí podrías actualizar el estado del pedido basado en el envío
        } catch (Exception e) {
            System.err.println("[Pedidos] Error procesando evento de envíos: " + e.getMessage());
        }
    }
}