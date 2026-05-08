package com.example.pedidos.pedidos_service.repository;

import com.example.pedidos.pedidos_service.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}