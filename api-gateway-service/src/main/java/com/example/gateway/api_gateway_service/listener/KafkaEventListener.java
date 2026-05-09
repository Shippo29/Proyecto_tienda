package com.example.gateway.api_gateway_service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventListener {

    @KafkaListener(topics = {"pedidos-events", "inventario-events", "envios-events"}, groupId = "gateway-events")
    public void onMessage(String message) {
        System.out.println("[Gateway Kafka Event] " + message);
    }
}
