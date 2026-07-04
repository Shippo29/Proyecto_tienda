package com.example.envios.envios_service.model;

import jakarta.persistence.*;
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

    private String direccion;

    private String estado;

    private Long pedidoId;

    // Transportista asignado y ruta estimada: soporta el requisito de
    // "mejorar la planificación de rutas y la comunicación con
    // transportistas" del caso SmartLogix.
    private String transportista;

    private String rutaEstimada;
}