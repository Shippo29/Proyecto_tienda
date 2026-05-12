package com.example.envios.envios_service.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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