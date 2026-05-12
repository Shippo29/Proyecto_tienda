package com.example.envios.envios_service.events;

import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCreadoEvent {
    private Long pedidoId;
    private String cliente;
    private String producto;
    private Integer cantidad;
    private BigDecimal total;
    private Instant createdAt;
}
