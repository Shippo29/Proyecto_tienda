package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.service.ProductoService;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RestController
@RequestMapping("/productos")
@Slf4j
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        log.debug("ProductoController - GET /productos");
        return productoService.listarProductos();
    }

    @PostMapping
    public Producto guardarProducto(@RequestBody Producto producto) {
        log.debug("ProductoController - POST /productos body: nombre={} stock={} precio={}", producto.getNombre(), producto.getStock(), producto.getPrecio());
        return productoService.guardarProducto(producto);
    }
}