package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.model.Producto;
import com.example.inventario.inventario_service.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.inventario.inventario_service.repository.BodegaRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

@Autowired
private MockMvc mockMvc;

@MockBean
private ProductoService productoService;

@MockBean
private BodegaRepository bodegaRepository;

@Autowired
private ObjectMapper objectMapper;

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
    void listarProductos_debeRetornar200ConListaDeProductos() throws Exception {
        List<Producto> productos = Arrays.asList(producto,
            Producto.builder().id(2L).nombre("Mouse").precio(29.99).stock(50).build());
        when(productoService.listarProductos()).thenReturn(productos);

    mockMvc.perform(get("/productos"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].nombre").value("Laptop Dell XPS"))
            .andExpect(jsonPath("$[1].nombre").value("Mouse"));
    }

    @Test
    void listarProductos_sinProductos_debeRetornarListaVacia() throws Exception {
        when(productoService.listarProductos()).thenReturn(List.of());

        mockMvc.perform(get("/productos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void guardarProducto_conDatosValidos_debeRetornar200() throws Exception {
        Producto nuevo = Producto.builder().nombre("Teclado").precio(49.99).stock(20).build();
        when(productoService.guardarProducto(any(Producto.class))).thenReturn(producto);

    mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Laptop Dell XPS"));
    }
}