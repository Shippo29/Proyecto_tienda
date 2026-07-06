package com.example.pedidos.pedidos_service.factory;

import com.example.pedidos.pedidos_service.model.Pedido;

public final class PedidoFactory {

    private static final String BODEGA_POR_DEFECTO = "Bodega Central Santiago";

    private PedidoFactory() {
    }

    public static Pedido crearPedido(Pedido datos) {
        String bodegaOrigen = (datos.getBodegaOrigen() == null || datos.getBodegaOrigen().isBlank())
            ? BODEGA_POR_DEFECTO
            : datos.getBodegaOrigen();

        return Pedido.builder()
            .cliente(datos.getCliente())
            .producto(datos.getProducto())
            .cantidad(datos.getCantidad())
            .total(datos.getTotal())
            .bodegaOrigen(bodegaOrigen)
            .build();
    }
}
