package com.example.pedidos.pedidos_service.controller;

import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.repository.PedidoRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")

public class PedidoController {

    private final PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @PostMapping
    public Pedido guardarPedido(@RequestBody Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
}