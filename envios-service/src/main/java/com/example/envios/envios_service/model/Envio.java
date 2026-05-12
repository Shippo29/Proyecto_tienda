package com.example.envios.envios_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "Estado es obligatorio")
    private String estado;

    @NotNull(message = "Pedido ID es obligatorio")
    private Long pedidoId;
}