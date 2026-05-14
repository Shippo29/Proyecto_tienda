package com.example.inventario.inventario_service.service.impl;

import com.example.inventario.inventario_service.events.ProductoActualizadoEvent;
import com.example.inventario.inventario_service.events.ProductoCreadoEvent;
import com.example.inventario.inventario_service.exception.BadRequestException;
import com.example.inventario.inventario_service.exception.ResourceNotFoundException;
import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.repository.ProductoRepository;
import com.example.inventario.inventario_service.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class InventarioServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventarioServiceImpl(ProductoRepository productoRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.productoRepository = productoRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        // Validaciones de negocio
        if (producto.getPrecio() != null && producto.getPrecio() < 0) {
            throw new BadRequestException("Precio no puede ser negativo");
        }
        if (producto.getStock() != null && producto.getStock() < 0) {
            throw new BadRequestException("Stock no puede ser negativo");
        }

        Producto saved = productoRepository.save(producto);

        // Publicar evento
        ProductoCreadoEvent event = ProductoCreadoEvent.builder()
                .productoId(saved.getId())
                .nombre(saved.getNombre())
                .precio(saved.getPrecio())
                .stock(saved.getStock())
                .createdAt(java.time.Instant.now())
                .build();

        CompletableFuture<SendResult<String, Object>> sendFuture = kafkaTemplate.send("productos.created", String.valueOf(saved.getId()), event);
        sendFuture.whenComplete((result, ex) -> {
            if (ex != null) {
                org.slf4j.LoggerFactory.getLogger(InventarioServiceImpl.class).error("InventarioService - Failed to publish ProductoCreadoEvent for productoId={} cause={}", saved.getId(), ex.getMessage(), ex);
            } else {
                org.slf4j.LoggerFactory.getLogger(InventarioServiceImpl.class).info("InventarioService - Product created event published productoId={} topic={} partition={} offset={}", saved.getId(), result.getRecordMetadata().topic(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });

        return saved;
    }

    @Override
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        return productoRepository.findById(id)
                .map(existing -> {
                    if (producto.getNombre() != null) existing.setNombre(producto.getNombre());
                    if (producto.getPrecio() != null) {
                        if (producto.getPrecio() < 0) throw new BadRequestException("Precio no puede ser negativo");
                        existing.setPrecio(producto.getPrecio());
                    }
                    if (producto.getStock() != null) {
                        if (producto.getStock() < 0) throw new BadRequestException("Stock no puede ser negativo");
                        existing.setStock(producto.getStock());
                    }

                    Producto saved = productoRepository.save(existing);

                    ProductoActualizadoEvent updatedEvent = ProductoActualizadoEvent.builder()
                            .productoId(saved.getId())
                            .nombre(saved.getNombre())
                            .precio(saved.getPrecio())
                            .stock(saved.getStock())
                            .updatedAt(java.time.Instant.now())
                            .build();

                    CompletableFuture<SendResult<String, Object>> sendFuture = kafkaTemplate.send("productos.updated", String.valueOf(saved.getId()), updatedEvent);
                    sendFuture.whenComplete((result, ex) -> {
                        if (ex != null) {
                            org.slf4j.LoggerFactory.getLogger(InventarioServiceImpl.class).error("InventarioService - Failed to publish ProductoActualizadoEvent for productoId={} cause={}", saved.getId(), ex.getMessage(), ex);
                        } else {
                            org.slf4j.LoggerFactory.getLogger(InventarioServiceImpl.class).info("InventarioService - Product updated event published productoId={}", saved.getId());
                        }
                    });

                    return saved;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id " + id);
        }
        productoRepository.deleteById(id);
    }
}