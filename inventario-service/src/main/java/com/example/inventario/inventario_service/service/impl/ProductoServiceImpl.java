package com.example.inventario.inventario_service.service.impl;

import com.example.inventario.inventario_service.events.ProductoActualizadoEvent;
import com.example.inventario.inventario_service.exception.BusinessException;
import com.example.inventario.inventario_service.exception.ResourceNotFoundException;
import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.repository.ProductoRepository;
import com.example.inventario.inventario_service.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final KafkaTemplate<String, ProductoActualizadoEvent> kafkaTemplate;

    public ProductoServiceImpl(ProductoRepository productoRepository, KafkaTemplate<String, ProductoActualizadoEvent> kafkaTemplate) {
        this.productoRepository = productoRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        if (producto.getStock() != null && producto.getStock() < 0) {
            throw new BusinessException("Stock no puede ser negativo");
        }
        if (producto.getPrecio() != null && producto.getPrecio() < 0) {
            throw new BusinessException("Precio no puede ser negativo");
        }
        log.info("ProductoService - Saving Producto: nombre={}, precio={}, stock={}", producto.getNombre(), producto.getPrecio(), producto.getStock());
        Producto saved = productoRepository.save(producto);
        log.info("ProductoService - Producto saved: id={}", saved.getId());

        ProductoActualizadoEvent event = ProductoActualizadoEvent.builder()
                .productoId(saved.getId())
                .nombre(saved.getNombre())
                .precio(saved.getPrecio())
                .stock(saved.getStock())
                .updatedAt(Instant.now())
                .build();
        kafkaTemplate.send("inventario-events", String.valueOf(saved.getId()), event);

        return saved;
    }

    @Override
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        if (producto.getStock() != null && producto.getStock() < 0) {
            throw new BusinessException("Stock no puede ser negativo");
        }
        if (producto.getPrecio() != null && producto.getPrecio() < 0) {
            throw new BusinessException("Precio no puede ser negativo");
        }
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setStock(producto.getStock());
        Producto updated = productoRepository.save(productoExistente);

        ProductoActualizadoEvent event = ProductoActualizadoEvent.builder()
                .productoId(updated.getId())
                .nombre(updated.getNombre())
                .precio(updated.getPrecio())
                .stock(updated.getStock())
                .updatedAt(Instant.now())
                .build();
        kafkaTemplate.send("inventario-events", String.valueOf(updated.getId()), event);

        return updated;
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
        log.info("ProductoService - Producto deleted: id={}", id);
    }
}