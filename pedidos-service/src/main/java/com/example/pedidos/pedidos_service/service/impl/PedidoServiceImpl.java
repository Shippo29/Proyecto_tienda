package com.example.pedidos.pedidos_service.service.impl;

import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.repository.PedidoRepository;
import com.example.pedidos.pedidos_service.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Override
    public List<Pedido> listarPedidos() {
        return repository.findAll();
    }

    @Override
    public Pedido guardarPedido(Pedido pedido) {
        return repository.save(pedido);
    }

    @Override
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Pedido actualizarPedido(Long id, Pedido pedido) {

        Pedido pedidoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedidoExistente.setCliente(pedido.getCliente());
        pedidoExistente.setTotal(pedido.getTotal());

        return repository.save(pedidoExistente);
    }

    @Override
    public void eliminarPedido(Long id) {
        repository.deleteById(id);
    }
}