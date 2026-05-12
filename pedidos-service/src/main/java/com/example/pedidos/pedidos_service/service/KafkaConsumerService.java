package com.example.pedidos.pedidos_service.service;

import com.example.pedidos.pedidos_service.events.EnvioActualizadoEvent;
import com.example.pedidos.pedidos_service.events.StockReservationResultEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

    @KafkaListener(topics = "inventario.stock.result", groupId = "pedidos-group")
    public void consumeInventarioStockResult(String message) {
        try {
            StockReservationResultEvent event = objectMapper.readValue(message, StockReservationResultEvent.class);
            log.info("[Pedidos] Inventario result for pedidoId={} producto={} status={} availableStock={}", event.getPedidoId(), event.getProducto(), event.getStatus(), event.getAvailableStock());
            String estado = switch (event.getStatus()) {
                case "CONFIRMADO" -> "LISTO_PARA_ENVIO";
                case "STOCK_INSUFICIENTE", "PRODUCTO_NO_ENCONTRADO" -> "RECHAZADO_POR_INVENTARIO";
                default -> "PENDIENTE";
            };
            pedidoService.actualizarEstadoPedido(event.getPedidoId(), estado, null);
        } catch (Exception e) {
            log.error("[Pedidos] Error procesando evento de stock: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "envios-events", groupId = "pedidos-group")
    public void consumeEnviosEvent(String message) {
        try {
            EnvioActualizadoEvent event = objectMapper.readValue(message, EnvioActualizadoEvent.class);
            log.info("[Pedidos] Envío actualizado para pedidoId={} estado={}", event.getPedidoId(), event.getEstado());
            String estado = switch (event.getEstado()) {
                case "CREADO" -> "PREPARANDO_ENVIO";
                case "ENVIADO" -> "EN_CAMINO";
                case "ENTREGADO" -> "ENTREGADO";
                case "CANCELADO" -> "CANCELADO";
                default -> "EN_PROCESO";
            };
            pedidoService.actualizarEstadoPedido(event.getPedidoId(), estado, event.getDireccion());
        } catch (Exception e) {
            log.error("[Pedidos] Error procesando evento de envíos: {}", e.getMessage(), e);
        }
    }
}