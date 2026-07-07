package com.example.inventario.inventario_service.kafka;

import com.example.inventario.inventario_service.events.PedidoCreadoEvent;
import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoCreatedListenerTest {

    @Mock
    private ProductoRepository productoRepository;

    private PedidoCreatedListener listener;

    @BeforeEach
    void setUp() {
        listener = new PedidoCreatedListener(productoRepository);
    }

    @Test
    void handlePedidoCreado_conProductoExistente_debeDecrementarStock() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Laptop Dell XPS")
                .precio(999.99)
                .stock(10)
                .build();

        PedidoCreadoEvent event = PedidoCreadoEvent.builder()
                .pedidoId(5L)
                .cliente("Juan Perez")
                .producto("Laptop Dell XPS")
                .cantidad(3)
                .build();

        when(productoRepository.findAll()).thenReturn(List.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        listener.handlePedidoCreado(event);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository, times(1)).save(captor.capture());
        assertEquals(7, captor.getValue().getStock());
    }

    @Test
    void handlePedidoCreado_conProductoInexistente_noDebeGuardarNada() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Mouse Logitech")
                .precio(29.99)
                .stock(50)
                .build();

        PedidoCreadoEvent event = PedidoCreadoEvent.builder()
                .pedidoId(6L)
                .cliente("Ana Garcia")
                .producto("Producto Que No Existe")
                .cantidad(1)
                .build();

        when(productoRepository.findAll()).thenReturn(List.of(producto));

        assertDoesNotThrow(() -> listener.handlePedidoCreado(event));

        verify(productoRepository, never()).save(any(Producto.class));
    }

    @Test
    void handlePedidoCreado_debeCompararNombreIgnorandoMayusculas() {
        Producto producto = Producto.builder()
                .id(2L)
                .nombre("Teclado Mecanico")
                .precio(59.99)
                .stock(20)
                .build();

        PedidoCreadoEvent event = PedidoCreadoEvent.builder()
                .pedidoId(7L)
                .cliente("Cliente X")
                .producto("teclado mecanico")
                .cantidad(2)
                .build();

        when(productoRepository.findAll()).thenReturn(List.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        listener.handlePedidoCreado(event);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository, times(1)).save(captor.capture());
        assertEquals(18, captor.getValue().getStock());
    }

    @Test
    void fallbackActualizarStock_noDebeLanzarExcepcionNiInteractuarConElRepositorio() {
        PedidoCreadoEvent event = PedidoCreadoEvent.builder()
                .pedidoId(99L)
                .cliente("Cliente Fallback")
                .producto("Producto Y")
                .cantidad(1)
                .build();
        Throwable error = new RuntimeException("Kafka no disponible");

        assertDoesNotThrow(() -> listener.fallbackActualizarStock(event, error));
    }
}
