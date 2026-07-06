package com.example.inventario.inventario_service.dto;

import com.example.inventario.inventario_service.model.Producto;

public record ProductoDTO(
        Long id,
        String nombre,
        String sku,
        Double precio,
        Integer stock,
        Long bodegaId,
        String bodegaNombre
) {
    public static ProductoDTO fromEntity(Producto p) {
        return new ProductoDTO(
                p.getId(),
                p.getNombre(),
                p.getSku(),
                p.getPrecio(),
                p.getStock(),
                p.getBodega() != null ? p.getBodega().getId() : null,
                p.getBodega() != null ? p.getBodega().getNombre() : null
        );
    }
}