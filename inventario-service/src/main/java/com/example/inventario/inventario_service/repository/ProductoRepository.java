package com.example.inventario.inventario_service.repository;

import com.example.inventario.inventario_service.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNombreIgnoreCase(String nombre);
}