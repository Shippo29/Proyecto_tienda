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

    public Object getTotal() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotal'");
    }

    public void setTotal(Object total) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTotal'");
    }
}
