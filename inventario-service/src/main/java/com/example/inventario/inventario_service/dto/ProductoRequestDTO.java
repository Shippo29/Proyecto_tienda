package com.example.inventario.inventario_service.dto;

public record ProductoRequestDTO(
        String nombre,
        String sku,
        Double precio,
        Integer stock,
        Long bodegaId
) {
}