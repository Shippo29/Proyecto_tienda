package com.example.inventario.inventario_service.events;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReservationResultEvent {
    private Long pedidoId;
    private String producto;
    private Integer cantidad;
    private String status;
    private Integer availableStock;
    private String message;
    private Instant timestamp;
}
