package com.example.inventario.inventario_service.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoActualizadoEvent {
    private Long productoId;
    private String nombre;
    private Double precio;
    private Integer stock;
    private Instant updatedAt;
}