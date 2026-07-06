package com.example.pedidos.pedidos_service.config;

import com.example.pedidos.pedidos_service.events.PedidoCreadoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class KafkaProducerConfigTest {

    private KafkaProducerConfig kafkaProducerConfig;

    @BeforeEach
    void setUp() {
        kafkaProducerConfig = new KafkaProducerConfig();
        ReflectionTestUtils.setField(kafkaProducerConfig, "bootstrapServers", "localhost:9092");
    }

    @Test
    void producerFactory_debeCrearseCorrectamente() {
        ProducerFactory<String, PedidoCreadoEvent> factory = kafkaProducerConfig.producerFactory();

        assertNotNull(factory);
    }

    @Test
    void kafkaTemplate_debeCrearseConSuProducerFactory() {
        KafkaTemplate<String, PedidoCreadoEvent> template = kafkaProducerConfig.kafkaTemplate();

        assertNotNull(template);
        assertNotNull(template.getProducerFactory());
    }
}
