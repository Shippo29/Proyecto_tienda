package com.example.inventario.inventario_service.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String sku;

    private Double precio;

    private Integer stock;

    // Bodega/tienda donde reside este stock. Permite sincronizar
    // inventario entre múltiples ubicaciones, tal como pide el caso.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bodega_id")
    private Bodega bodega;
}