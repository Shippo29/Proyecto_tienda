package com.example.pedidos.pedidos_service.factory;

import com.example.pedidos.pedidos_service.model.Pedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PedidoFactoryTest {

    @Test
    void crearPedido_sinBodegaOrigen_debeUsarBodegaPorDefecto() {
        Pedido datos = Pedido.builder()
                .cliente("Juan Perez")
                .producto("Laptop")
                .cantidad(1)
                .total(new BigDecimal("999.99"))
                .build();

        Pedido resultado = PedidoFactory.crearPedido(datos);

        assertEquals("Bodega Central Santiago", resultado.getBodegaOrigen());
        assertEquals("Juan Perez", resultado.getCliente());
        assertEquals("Laptop", resultado.getProducto());
        assertEquals(1, resultado.getCantidad());
        assertEquals(new BigDecimal("999.99"), resultado.getTotal());
        assertNull(resultado.getId());
    }

    @Test
    void crearPedido_conBodegaOrigenEnBlanco_debeUsarBodegaPorDefecto() {
        Pedido datos = Pedido.builder()
                .cliente("Ana Garcia")
                .producto("Mouse")
                .cantidad(2)
                .total(new BigDecimal("29.99"))
                .bodegaOrigen("   ")
                .build();

        Pedido resultado = PedidoFactory.crearPedido(datos);

        assertEquals("Bodega Central Santiago", resultado.getBodegaOrigen());
    }

    @Test
    void crearPedido_conBodegaOrigenDefinida_debeRespetarla() {
        Pedido datos = Pedido.builder()
                .cliente("Cliente X")
                .producto("Teclado")
                .cantidad(1)
                .total(new BigDecimal("49.99"))
                .bodegaOrigen("Bodega Valparaiso")
                .build();

        Pedido resultado = PedidoFactory.crearPedido(datos);

        assertEquals("Bodega Valparaiso", resultado.getBodegaOrigen());
    }
}
