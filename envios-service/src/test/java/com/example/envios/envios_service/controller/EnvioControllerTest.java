package com.example.envios.envios_service.controller;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.service.EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnvioController.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService envioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Envio envio;

    @BeforeEach
    void setUp() {
        envio = new Envio(1L, "Av. Principal 123", "PENDIENTE", 10L, null, null);
    }

    @Test
    void obtenerEnvios_debeRetornar200ConListaDeEnvios() throws Exception {
        List<Envio> envios = Arrays.asList(
                envio,
                new Envio(2L, "Calle 456", "ENVIADO", 11L, null, null)
        );
        when(envioService.obtenerEnvios()).thenReturn(envios);
        mockMvc.perform(get("/envios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].direccion").value("Av. Principal 123"))
                .andExpect(jsonPath("$[0].estado").value("PENDIENTE"))
                .andExpect(jsonPath("$[1].estado").value("ENVIADO"));
    }

    @Test
    void obtenerEnvios_sinEnvios_debeRetornarListaVacia() throws Exception {
        when(envioService.obtenerEnvios()).thenReturn(List.of());
        mockMvc.perform(get("/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void guardarEnvio_conDatosValidos_debeRetornar200() throws Exception {
        Envio nuevo = new Envio(null, "Calle Nueva 789", "PENDIENTE", 20L, null, null);
        when(envioService.guardarEnvio(any(Envio.class))).thenReturn(envio);
        mockMvc.perform(post("/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.direccion").value("Av. Principal 123"))
                .andExpect(jsonPath("$.pedidoId").value(10));
    }

    @Test
    void guardarEnvio_debeRetornarEnvioConEstado() throws Exception {
        Envio entregado = new Envio(3L, "Calle Sur", "ENTREGADO", 30L, null, null);
        Envio nuevo = new Envio(null, "Calle Sur", "ENTREGADO", 30L, null, null);
        when(envioService.guardarEnvio(any(Envio.class))).thenReturn(entregado);
        mockMvc.perform(post("/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"))
                .andExpect(jsonPath("$.id").value(3));
    }
}