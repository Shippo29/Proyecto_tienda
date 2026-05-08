package com.example.pedidos.pedidos_service.service;

import com.example.pedidos.pedidos_service.model.Pedido;


import java.util.List;
import java.util.Optional;

public interface PedidoService {

    List<Pedido> listarPedidos();

    Pedido guardarPedido(Pedido pedido);

    Optional<Pedido> obtenerPedidoPorId(Long id);

    Pedido actualizarPedido(Long id, Pedido pedido);

    void eliminarPedido(Long id);
}