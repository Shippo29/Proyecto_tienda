package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.service.KafkaProducerService;
import com.example.inventario.inventario_service.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;
    private final KafkaProducerService kafkaProducerService;

    public ProductoController(ProductoService productoService, KafkaProducerService kafkaProducerService) {
        this.productoService = productoService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @PostMapping
    public Producto guardarProducto(@RequestBody Producto producto) {
        Producto productoGuardado = productoService.guardarProducto(producto);
        kafkaProducerService.send("inventario-events", productoGuardado);
        return productoGuardado;
    }
}