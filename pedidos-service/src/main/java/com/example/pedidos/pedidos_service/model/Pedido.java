package com.example.pedidos.pedidos_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedidos")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Cliente es obligatorio")
    private String cliente;

    @NotBlank(message = "Producto es obligatorio")
    private String producto;

    @NotNull(message = "Cantidad es obligatoria")
    @Min(value = 1, message = "Cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "Total es obligatorio")
    @DecimalMin(value = "0.01", message = "Total debe ser mayor a 0")
    private BigDecimal total;

    private String estado;

    private String direccion;
}
