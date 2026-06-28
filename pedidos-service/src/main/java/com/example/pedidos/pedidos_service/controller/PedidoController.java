package com.example.pedidos.pedidos_service.controller;

import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.service.PedidoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Crear un nuevo pedido")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Pedido creado")})
    public ResponseEntity<Pedido> guardarPedido(@Valid @RequestBody Pedido pedido) {
        log.debug("PedidoController - POST /pedidos body: cliente={} producto={} cantidad={} total={}", 
            pedido.getCliente(), pedido.getProducto(), pedido.getCantidad(), pedido.getTotal());
        Pedido saved = pedidoService.guardarPedido(pedido);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    
}