package com.example.inventario.inventario_service.service.impl;

import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Laptop Dell XPS")
                .precio(999.99)
                .stock(10)
                .build();
    }

    @Test
    void listarProductos_debeRetornarListaDeProductos() {
        List<Producto> productos = Arrays.asList(producto,
                Producto.builder().id(2L).nombre("Mouse Logitech").precio(29.99).stock(50).build());
        when(productoRepository.findAll()).thenReturn(productos);
        List<Producto> resultado = inventarioService.listarProductos();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Laptop Dell XPS", resultado.get(0).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void listarProductos_listaVacia_debeRetornarListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());
        List<Producto> resultado = inventarioService.listarProductos();
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void guardarProducto_debeGuardarYRetornarProducto() {
        Producto nuevo = Producto.builder().nombre("Teclado").precio(49.99).stock(20).build();
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = inventarioService.guardarProducto(nuevo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laptop Dell XPS", resultado.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void obtenerProductoPorId_conIdExistente_debeRetornarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        Optional<Producto> resultado = inventarioService.obtenerProductoPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Laptop Dell XPS", resultado.get().getNombre());
        assertEquals(999.99, resultado.get().getPrecio());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerProductoPorId_conIdInexistente_debeRetornarVacio() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Producto> resultado = inventarioService.obtenerProductoPorId(99L);
        assertFalse(resultado.isPresent());
        verify(productoRepository, times(1)).findById(99L);
    }

    @Test
    void actualizarProducto_conIdExistente_debeActualizarProducto() {
        Producto actualizado = Producto.builder()
                .nombre("Laptop Dell XPS Pro")
                .precio(1299.99)
                .stock(5)
                .build();

        Producto productoActualizado = Producto.builder()
                .id(1L)
                .nombre("Laptop Dell XPS Pro")
                .precio(1299.99)
                .stock(5)
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoActualizado);

        Producto resultado = inventarioService.actualizarProducto(1L, actualizado);

        assertNotNull(resultado);
        assertEquals("Laptop Dell XPS Pro", resultado.getNombre());
        assertEquals(1299.99, resultado.getPrecio());
        assertEquals(5, resultado.getStock());
        verify(productoRepository, times(1)).findById(1L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void actualizarProducto_conIdInexistente_debeLanzarExcepcion() {
        Producto actualizado = Producto.builder().nombre("Nuevo").precio(100.0).stock(1).build();
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventarioService.actualizarProducto(99L, actualizado));

        assertTrue(ex.getMessage().contains("99"));
        verify(productoRepository, times(1)).findById(99L);
        verify(productoRepository, never()).save(any());
    }

    @Test
    void eliminarProducto_debeInvocarDeleteById() {
        doNothing().when(productoRepository).deleteById(1L);
        inventarioService.eliminarProducto(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }
}