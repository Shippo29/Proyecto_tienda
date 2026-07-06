package com.example.pedidos.pedidos_service.factory;

import com.example.pedidos.pedidos_service.model.Pedido;

/**
 * Factory Method para la creación de instancias de {@link Pedido}.
 *
 * Centraliza las reglas de construcción de un pedido (valores por defecto,
 * normalización de datos) para que el servicio no dependa de la forma
 * exacta en que llegó el payload desde el BFF/frontend. Esto es lo que
 * pide el caso SmartLogix como patrón "Factory Method para la creación
 * de instancias".
 */
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
