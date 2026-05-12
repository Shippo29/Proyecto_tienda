package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.repository.ProductoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")

public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @PostMapping
    public Producto guardarProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }
}