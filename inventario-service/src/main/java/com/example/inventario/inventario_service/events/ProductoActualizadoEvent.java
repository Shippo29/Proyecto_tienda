package com.example.inventario.inventario_service.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoActualizadoEvent {
    private Long productoId;
    private String nombre;
    private Double precio;
    private Integer stock;
    private java.time.Instant updatedAt;
}
