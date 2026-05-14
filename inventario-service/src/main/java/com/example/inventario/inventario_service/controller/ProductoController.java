package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.service.ProductoService;
import com.example.inventario.inventario_service.exception.ResourceNotFoundException;
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
@RequestMapping("/productos")
@Slf4j
@Tag(name = "Productos", description = "Operaciones CRUD sobre productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos")
    public ResponseEntity<List<Producto>> listarProductos() {
        log.debug("ProductoController - GET /productos");
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Producto encontrado"), @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear producto")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Producto creado")})
    public ResponseEntity<Producto> guardarProducto(@RequestBody Producto producto) {
        log.debug("ProductoController - POST /productos body: nombre={} stock={} precio={}", producto.getNombre(), producto.getStock(), producto.getPrecio());
        Producto saved = productoService.guardarProducto(producto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Producto actualizado"), @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto updated = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Producto eliminado"), @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}