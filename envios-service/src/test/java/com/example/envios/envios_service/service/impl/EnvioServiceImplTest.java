package com.example.envios.envios_service.service.impl;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceImplTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioServiceImpl envioService;

    private Envio envio;

    @BeforeEach
    void setUp() {
        envio = new Envio(1L, "Av. Principal 123", "PENDIENTE", 10L, null, null);
    }

    @Test
    void obtenerEnvios_debeRetornarListaDeEnvios() {
        List<Envio> envios = Arrays.asList(
                envio,
                new Envio(2L, "Calle 456", "ENVIADO", 11L, null, null)
        );
        when(envioRepository.findAll()).thenReturn(envios);
        List<Envio> resultado = envioService.obtenerEnvios();
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Av. Principal 123", resultado.get(0).getDireccion());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        verify(envioRepository, times(1)).findAll();
    }

    @Test
    void obtenerEnvios_sinEnvios_debeRetornarListaVacia() {
        when(envioRepository.findAll()).thenReturn(List.of());
        List<Envio> resultado = envioService.obtenerEnvios();
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void guardarEnvio_debeGuardarYRetornarEnvio() {
        Envio nuevo = new Envio(null, "Calle Nueva 789", "PENDIENTE", 20L, null, null);
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);
        Envio resultado = envioService.guardarEnvio(nuevo);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Av. Principal 123", resultado.getDireccion());
        assertEquals("PENDIENTE", resultado.getEstado());
        assertEquals(10L, resultado.getPedidoId());
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void guardarEnvio_debeAsociarPedidoId() {
        Envio nuevo = new Envio(null, "Calle Sur 100", "PENDIENTE", 42L, null, null);
        Envio guardado = new Envio(5L, "Calle Sur 100", "PENDIENTE", 42L, null, null);
        when(envioRepository.save(any(Envio.class))).thenReturn(guardado);
        Envio resultado = envioService.guardarEnvio(nuevo);
        assertEquals(42L, resultado.getPedidoId());
        assertEquals(5L, resultado.getId());
    }

    @Test
    void guardarEnvio_estadoEntregado_debeGuardarCorrectamente() {
        Envio entregado = new Envio(null, "Calle Norte 50", "ENTREGADO", 30L, null, null);
        Envio guardado  = new Envio(3L, "Calle Norte 50", "ENTREGADO", 30L, null, null);
        when(envioRepository.save(any(Envio.class))).thenReturn(guardado);
        Envio resultado = envioService.guardarEnvio(entregado);
        assertEquals("ENTREGADO", resultado.getEstado());
        assertEquals(3L, resultado.getId());
    }
}
