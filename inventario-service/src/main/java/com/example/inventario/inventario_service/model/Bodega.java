package com.example.inventario.inventario_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bodegas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String ubicacion;

    // "BODEGA" o "TIENDA": permite distinguir centros de almacenamiento
    // de puntos de venta físicos, tal como pide el caso SmartLogix.
    private String tipo;
}
