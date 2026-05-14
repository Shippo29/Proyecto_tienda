package com.example.pedidos.pedidos_service.controller;

import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.service.PedidoService;
import com.example.pedidos.pedidos_service.exception.ResourceNotFoundException;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@Slf4j
@Tag(name = "Pedidos", description = "Operaciones CRUD sobre pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @Operation(summary = "Listar pedidos")
    public ResponseEntity<List<Pedido>> listarPedidos() {
        log.debug("PedidoController - GET /pedidos");
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pedido por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable Long id) {
        return pedidoService.obtenerPedidoPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo pedido")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Pedido creado")})
    public ResponseEntity<Pedido> guardarPedido(@RequestBody Pedido pedido) {
        log.debug("PedidoController - POST /pedidos body: cliente={} producto={} cantidad={} total={}", pedido.getCliente(), pedido.getProducto(), pedido.getCantidad(), pedido.getTotal());
        Pedido saved = pedidoService.guardarPedido(pedido);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pedido existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido actualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido updated = pedidoService.actualizarPedido(id, pedido);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido eliminado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}