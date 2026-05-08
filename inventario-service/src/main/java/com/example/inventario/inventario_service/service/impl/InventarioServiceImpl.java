package com.example.inventario.inventario_service.service.impl;

import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.repository.ProductoRepository;
import com.example.inventario.inventario_service.service.ProductoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public InventarioServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        return productoRepository.findById(id)
                .map(existing -> {
                    existing.setNombre(producto.getNombre());
                    existing.setPrecio(producto.getPrecio());
                    existing.setStock(producto.getStock());
                    return productoRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}