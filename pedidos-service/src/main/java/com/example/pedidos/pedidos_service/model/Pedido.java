package com.example.pedidos.pedidos_service.model;

import jakarta.persistence.*;
import lombok.*;

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

    private String cliente;

    private String producto;

    private Integer cantidad;

    private Double total;
}

