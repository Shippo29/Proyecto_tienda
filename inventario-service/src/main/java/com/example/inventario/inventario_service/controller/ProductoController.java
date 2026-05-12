package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.service.ProductoService;
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
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Endpoints para gestión de inventario")
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar productos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos listados correctamente")
    })
    @GetMapping
    public List<Producto> listarProductos() {
        log.debug("ProductoController - GET /api/v1/productos");
        return productoService.listarProductos();
    }

    @Operation(summary = "Obtener producto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        log.debug("ProductoController - GET /api/v1/productos/{}", id);
        Optional<Producto> producto = productoService.obtenerProductoPorId(id);
        return producto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Producto> guardarProducto(@Valid @RequestBody Producto producto) {
        log.debug("ProductoController - POST /api/v1/productos body: nombre={} stock={} precio={}", producto.getNombre(), producto.getStock(), producto.getPrecio());
        Producto saved = productoService.guardarProducto(producto);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Actualizar producto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        log.debug("ProductoController - PUT /api/v1/productos/{} body: nombre={} stock={} precio={}", id, producto.getNombre(), producto.getStock(), producto.getPrecio());
        Producto updated = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar producto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        log.debug("ProductoController - DELETE /api/v1/productos/{}", id);
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
