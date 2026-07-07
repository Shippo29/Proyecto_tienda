package com.example.envios.envios_service.config;

import com.example.envios.envios_service.events.PedidoCreadoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class KafkaConsumerConfigTest {

    private KafkaConsumerConfig kafkaConsumerConfig;

    @BeforeEach
    void setUp() {
        kafkaConsumerConfig = new KafkaConsumerConfig();
        ReflectionTestUtils.setField(kafkaConsumerConfig, "bootstrapServers", "localhost:9092");
        ReflectionTestUtils.setField(kafkaConsumerConfig, "groupId", "envios-group");
    }

    @Test
    void consumerFactory_debeCrearseCorrectamente() {
        ConsumerFactory<String, PedidoCreadoEvent> factory = kafkaConsumerConfig.consumerFactory();

        assertNotNull(factory);
    }

    @Test
    void kafkaListenerContainerFactory_debeCrearseConSuConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PedidoCreadoEvent> factory =
                kafkaConsumerConfig.kafkaListenerContainerFactory();

        assertNotNull(factory);
        assertNotNull(factory.getConsumerFactory());
    }
}
