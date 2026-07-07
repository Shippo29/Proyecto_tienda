package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.dto.ProductoDTO;
import com.example.inventario.inventario_service.dto.ProductoRequestDTO;
import com.example.inventario.inventario_service.model.Bodega;
import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.repository.BodegaRepository;
import com.example.inventario.inventario_service.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@Slf4j
public class ProductoController {

    private final ProductoService productoService;
    private final BodegaRepository bodegaRepository;

    public ProductoController(ProductoService productoService, BodegaRepository bodegaRepository) {
        this.productoService = productoService;
        this.bodegaRepository = bodegaRepository;
    }

    @GetMapping
    public List<ProductoDTO> listarProductos() {
        log.debug("ProductoController - GET /productos");
        return productoService.listarProductos()
                .stream()
                .map(ProductoDTO::fromEntity)
                .toList();
    }

    @PostMapping
    public ProductoDTO guardarProducto(@RequestBody ProductoRequestDTO request) {
        log.debug("ProductoController - POST /productos body: nombre={} stock={} precio={}",
                request.nombre(), request.stock(), request.precio());

        Bodega bodega = null;
        if (request.bodegaId() != null) {
            bodega = bodegaRepository.findById(request.bodegaId())
                    .orElseThrow(() -> new RuntimeException("Bodega no encontrada con id " + request.bodegaId()));
        }

        Producto producto = Producto.builder()
                .nombre(request.nombre())
                .sku(request.sku())
                .precio(request.precio())
                .stock(request.stock())
                .bodega(bodega)
                .build();

        Producto guardado = productoService.guardarProducto(producto);
        return ProductoDTO.fromEntity(guardado);
    }
}