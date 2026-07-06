package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.model.Bodega;
import com.example.inventario.inventario_service.repository.BodegaRepository;
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

@WebMvcTest(BodegaController.class)
class BodegaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BodegaRepository bodegaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Bodega bodega;

    @BeforeEach
    void setUp() {
        bodega = Bodega.builder()
                .id(1L)
                .nombre("Bodega Central Santiago")
                .ubicacion("Santiago")
                .tipo("BODEGA")
                .build();
    }

    @Test
    void listarBodegas_debeRetornar200ConListaDeBodegas() throws Exception {
        List<Bodega> bodegas = Arrays.asList(bodega,
                Bodega.builder().id(2L).nombre("Tienda Providencia").ubicacion("Providencia").tipo("TIENDA").build());
        when(bodegaRepository.findAll()).thenReturn(bodegas);

        mockMvc.perform(get("/bodegas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Bodega Central Santiago"))
                .andExpect(jsonPath("$[0].tipo").value("BODEGA"))
                .andExpect(jsonPath("$[1].tipo").value("TIENDA"));
    }

    @Test
    void listarBodegas_sinBodegas_debeRetornarListaVacia() throws Exception {
        when(bodegaRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/bodegas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void guardarBodega_conDatosValidos_debeRetornar200() throws Exception {
        Bodega nueva = Bodega.builder().nombre("Nueva Bodega").ubicacion("Valparaiso").tipo("BODEGA").build();
        when(bodegaRepository.save(any(Bodega.class))).thenReturn(bodega);

        mockMvc.perform(post("/bodegas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Bodega Central Santiago"));
    }

    @Test
    void guardarBodega_debeRetornarTipoTienda() throws Exception {
        Bodega tienda = Bodega.builder().id(3L).nombre("Tienda Centro").ubicacion("Centro").tipo("TIENDA").build();
        Bodega nueva = Bodega.builder().nombre("Tienda Centro").ubicacion("Centro").tipo("TIENDA").build();
        when(bodegaRepository.save(any(Bodega.class))).thenReturn(tienda);

        mockMvc.perform(post("/bodegas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("TIENDA"))
                .andExpect(jsonPath("$.id").value(3));
    }
}
