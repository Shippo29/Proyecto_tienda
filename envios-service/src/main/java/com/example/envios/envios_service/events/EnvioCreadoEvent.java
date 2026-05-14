package com.example.envios.envios_service.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioCreadoEvent {
    private Long envioId;
    private Long pedidoId;
    private String direccion;
    private String estado;
    private java.time.Instant createdAt;
}
