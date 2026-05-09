package com.example.envios.envios_service.service;

import com.example.envios.envios_service.model.Envio;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final EnvioService envioService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(EnvioService envioService, ObjectMapper objectMapper) {
        this.envioService = envioService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "pedidos-events", groupId = "envios-group")
    public void consumePedidoEvent(String message) {
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            Long pedidoId = jsonNode.get("id").asLong();
            String cliente = jsonNode.get("cliente").asText();

            // Crear envío automáticamente para el pedido
            Envio envio = new Envio();
            envio.setPedidoId(pedidoId);
            envio.setDireccion("Dirección por defecto para " + cliente); // Podría mejorarse
            envio.setEstado("Pendiente");

            envioService.guardarEnvio(envio);
            System.out.println("[Envíos] Envío creado para pedido ID: " + pedidoId);
        } catch (Exception e) {
            System.err.println("[Envíos] Error procesando evento de pedido: " + e.getMessage());
        }
    }
}