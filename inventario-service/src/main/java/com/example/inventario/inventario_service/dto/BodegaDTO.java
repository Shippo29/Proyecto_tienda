package com.example.inventario.inventario_service.dto;

import com.example.inventario.inventario_service.model.Bodega;

public record BodegaDTO(
        Long id,
        String nombre,
        String ubicacion,
        String tipo
) {
    public static BodegaDTO fromEntity(Bodega b) {
        return new BodegaDTO(b.getId(), b.getNombre(), b.getUbicacion(), b.getTipo());
    }
}