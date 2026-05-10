package com.example.pedidos.pedidos_service.controller;

import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.service.PedidoService;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Slf4j
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        log.debug("PedidoController - GET /pedidos");
        return pedidoService.listarPedidos();
    }

    @PostMapping
    public Pedido guardarPedido(@RequestBody Pedido pedido) {
        log.debug("PedidoController - POST /pedidos body: cliente={} producto={} cantidad={} total={}", pedido.getCliente(), pedido.getProducto(), pedido.getCantidad(), pedido.getTotal());
        return pedidoService.guardarPedido(pedido);
    }
}