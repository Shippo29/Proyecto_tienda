package com.example.pedidos.pedidos_service.controller;

import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.service.KafkaProducerService;
import com.example.pedidos.pedidos_service.service.PedidoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;
    private final KafkaProducerService kafkaProducerService;

    public PedidoController(PedidoService pedidoService, KafkaProducerService kafkaProducerService) {
        this.pedidoService = pedidoService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoService.listarPedidos();
    }

    @PostMapping
    public Pedido guardarPedido(@RequestBody Pedido pedido) {
        Pedido pedidoGuardado = pedidoService.guardarPedido(pedido);
        kafkaProducerService.send("pedidos-events", pedidoGuardado);
        return pedidoGuardado;
    }
}