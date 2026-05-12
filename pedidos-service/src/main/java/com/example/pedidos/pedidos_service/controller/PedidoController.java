package com.example.pedidos.pedidos_service.controller;

import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gestión de pedidos")
@Slf4j
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Listar todos los pedidos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedidos listados correctamente")
    })
    @GetMapping
    public List<Pedido> listarPedidos() {
        log.debug("PedidoController - GET /api/v1/pedidos");
        return pedidoService.listarPedidos();
    }

    @Operation(summary = "Obtener pedido por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        log.debug("PedidoController - GET /api/v1/pedidos/{}", id);
        Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(id);
        return pedido.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Pedido> guardarPedido(@Valid @RequestBody Pedido pedido) {
        log.debug("PedidoController - POST /api/v1/pedidos body: cliente={} producto={} cantidad={} total={}", pedido.getCliente(), pedido.getProducto(), pedido.getCantidad(), pedido.getTotal());
        Pedido saved = pedidoService.guardarPedido(pedido);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Actualizar un pedido existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @Valid @RequestBody Pedido pedido) {
        log.debug("PedidoController - PUT /api/v1/pedidos/{} body: cliente={} producto={} cantidad={} total={}", id, pedido.getCliente(), pedido.getProducto(), pedido.getCantidad(), pedido.getTotal());
        Pedido updated = pedidoService.actualizarPedido(id, pedido);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar un pedido")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        log.debug("PedidoController - DELETE /api/v1/pedidos/{}", id);
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}