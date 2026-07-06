package com.example.pedidos.pedidos_service.service.impl;

import com.example.pedidos.pedidos_service.events.PedidoCreadoEvent;
import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

@Mock
private PedidoRepository repository;

@Mock
private KafkaTemplate<String, PedidoCreadoEvent> kafkaTemplate;

@InjectMocks
private PedidoServiceImpl pedidoService;

private Pedido pedido;

@BeforeEach
void setUp() {
    pedido = Pedido.builder()
            .id(1L)
            .cliente("Juan Perez")
            .producto("Laptop Dell XPS")
            .cantidad(2)
            .total(new BigDecimal("1999.98"))
            .build();
}

@Test
void listarPedidos_debeRetornarListaDePedidos() {
    List<Pedido> pedidos = Arrays.asList(pedido,
            Pedido.builder().id(2L).cliente("Ana Garcia").producto("Mouse")
                    .cantidad(1).total(new BigDecimal("29.99")).build());
        when(repository.findAll()).thenReturn(pedidos);
        List<Pedido> resultado = pedidoService.listarPedidos();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getCliente());
        verify(repository, times(1)).findAll();
}

@Test
void listarPedidos_sinPedidos_debeRetornarListaVacia() {
    when(repository.findAll()).thenReturn(List.of());
    List<Pedido> resultado = pedidoService.listarPedidos();
    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());
}

@Test
void guardarPedido_debeGuardarYPublicarEvento() {

    Pedido nuevo = Pedido.builder()
            .cliente("Juan Perez").producto("Laptop").cantidad(1)
            .total(new BigDecimal("999.99")).build();

    when(repository.save(any(Pedido.class))).thenReturn(pedido);
    when(kafkaTemplate.send(anyString(), anyString(), any(PedidoCreadoEvent.class)))
            .thenReturn(CompletableFuture.completedFuture(null));
    Pedido resultado = pedidoService.guardarPedido(nuevo);
    assertNotNull(resultado);
    assertEquals(1L, resultado.getId());
    assertEquals("Juan Perez", resultado.getCliente());
    verify(repository, times(1)).save(any(Pedido.class));
    verify(kafkaTemplate, times(1)).send(eq("pedidos.created"), anyString(), any(PedidoCreadoEvent.class));
}

@Test
void obtenerPedidoPorId_conIdExistente_debeRetornarPedido() {
    when(repository.findById(1L)).thenReturn(Optional.of(pedido));
    Optional<Pedido> resultado = pedidoService.obtenerPedidoPorId(1L);
    assertTrue(resultado.isPresent());
    assertEquals("Juan Perez", resultado.get().getCliente());
    assertEquals("Laptop Dell XPS", resultado.get().getProducto());
    verify(repository, times(1)).findById(1L);
}

@Test
void obtenerPedidoPorId_conIdInexistente_debeRetornarVacio() {
    when(repository.findById(99L)).thenReturn(Optional.empty());
    Optional<Pedido> resultado = pedidoService.obtenerPedidoPorId(99L);
    assertFalse(resultado.isPresent());
    verify(repository, times(1)).findById(99L);
}

@Test
void actualizarPedido_conIdExistente_debeActualizarPedido() {
    Pedido actualizado = Pedido.builder()
            .cliente("Juan Actualizado")
            .total(new BigDecimal("2500.00"))
            .build();

    Pedido pedidoActualizado = Pedido.builder()
            .id(1L)
            .cliente("Juan Actualizado")
            .producto("Laptop Dell XPS")
            .cantidad(2)
            .total(new BigDecimal("2500.00"))
            .build();

    when(repository.findById(1L)).thenReturn(Optional.of(pedido));
    when(repository.save(any(Pedido.class))).thenReturn(pedidoActualizado);
    Pedido resultado = pedidoService.actualizarPedido(1L, actualizado);
    assertNotNull(resultado);
    assertEquals("Juan Actualizado", resultado.getCliente());
    assertEquals(new BigDecimal("2500.00"), resultado.getTotal());
    verify(repository, times(1)).findById(1L);
    verify(repository, times(1)).save(any(Pedido.class));
}

@Test
void actualizarPedido_conIdInexistente_debeLanzarExcepcion() {
    Pedido actualizado = Pedido.builder().cliente("X").total(BigDecimal.ONE).build();
    when(repository.findById(99L)).thenReturn(Optional.empty());
    RuntimeException ex = assertThrows(RuntimeException.class,
            () -> pedidoService.actualizarPedido(99L, actualizado));

    assertTrue(ex.getMessage().contains("Pedido no encontrado"));
    verify(repository, never()).save(any());
}

@Test
void eliminarPedido_debeInvocarDeleteById() {
    doNothing().when(repository).deleteById(1L);
    pedidoService.eliminarPedido(1L);
    verify(repository, times(1)).deleteById(1L);
}

@Test
void fallbackPublicarEvento_debeBuscarPedidoExistenteORetornarOriginal() {
    Pedido pedidoBuscado = Pedido.builder()
            .id(5L).cliente("Juan Perez").producto("Laptop Dell XPS")
            .cantidad(1).total(new BigDecimal("999.99")).build();
    when(repository.findAll()).thenReturn(List.of(pedidoBuscado));
    Pedido resultado = pedidoService.fallbackPublicarEvento(pedido, new RuntimeException("Kafka caído"));
    assertNotNull(resultado);
    assertEquals("Juan Perez", resultado.getCliente());
}
}