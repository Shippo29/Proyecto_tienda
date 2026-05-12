package com.example.pedidos.pedidos_service.events;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioActualizadoEvent {
    private Long envioId;
    private Long pedidoId;
    private String direccion;
    private String estado;
    private Instant updatedAt;
}
