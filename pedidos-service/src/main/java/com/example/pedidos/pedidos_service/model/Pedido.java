package com.example.pedidos.pedidos_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;

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

    // Empresa/PYME o punto de venta que origina el pedido dentro de SmartLogix
    // (se mantiene el nombre de campo "cliente" por compatibilidad con el
    // BFF y el frontend ya integrados; conceptualmente representa a quién
    // gestiona el pedido, no a un comprador final).
    @NotBlank(message = "El cliente no puede estar vacío")
    private String cliente;

    @NotBlank(message = "El producto no puede estar vacío")
    private String producto;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    private BigDecimal total;

    // Bodega/tienda desde donde se despacha este pedido. Conecta el
    // pedido con el módulo de inventario multi-bodega.
    private String bodegaOrigen;
}