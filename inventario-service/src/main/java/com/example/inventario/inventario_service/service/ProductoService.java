package com.example.inventario.inventario_service.service;

import com.example.inventario.inventario_service.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {

    List<Producto> listarProductos();

    Producto guardarProducto(Producto producto);

    Optional<Producto> obtenerProductoPorId(Long id);

    Producto actualizarProducto(Long id, Producto producto);

    void eliminarProducto(Long id);
}