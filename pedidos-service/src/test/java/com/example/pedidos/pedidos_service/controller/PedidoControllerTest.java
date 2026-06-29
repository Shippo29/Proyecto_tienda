package com.example.pedidos.pedidos_service.controller;

import com.example.pedidos.pedidos_service.model.Pedido;
import com.example.pedidos.pedidos_service.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void listarPedidos_debeRetornar200ConListaDePedidos() throws Exception {
        List<Pedido> pedidos = Arrays.asList(pedido,
                Pedido.builder().id(2L).cliente("Ana").producto("Mouse")
                        .cantidad(1).total(new BigDecimal("29.99")).build());
        when(pedidoService.listarPedidos()).thenReturn(pedidos);
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].cliente").value("Juan Perez"))
                .andExpect(jsonPath("$[0].producto").value("Laptop Dell XPS"))
                .andExpect(jsonPath("$[1].cliente").value("Ana"));
    }

    @Test
    void guardarPedido_conDatosValidos_debeRetornar201() throws Exception {
        Pedido nuevo = Pedido.builder()
                .cliente("Juan Perez").producto("Laptop").cantidad(1)
                .total(new BigDecimal("999.99")).build();
        when(pedidoService.guardarPedido(any(Pedido.class))).thenReturn(pedido);
        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cliente").value("Juan Perez"));
    }

    @Test
    void guardarPedido_sinCliente_debeRetornar400() throws Exception {
        Pedido invalido = Pedido.builder()
                .cliente("").producto("Laptop").cantidad(1)
                .total(new BigDecimal("999.99")).build();
        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void guardarPedido_cantidadCero_debeRetornar400() throws Exception {
        Pedido invalido = Pedido.builder()
                .cliente("Juan").producto("Laptop").cantidad(0)
                .total(new BigDecimal("999.99")).build();

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());
    }
}
