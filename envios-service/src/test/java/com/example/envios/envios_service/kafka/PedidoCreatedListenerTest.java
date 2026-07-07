package com.example.envios.envios_service.kafka;

import com.example.envios.envios_service.events.PedidoCreadoEvent;
import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.service.EnvioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoCreatedListenerTest {

    @Mock
    private EnvioService envioService;

    private PedidoCreatedListener listener;

    @BeforeEach
    void setUp() {
        listener = new PedidoCreatedListener(envioService);
    }

    @Test
    void handlePedidoCreado_debeCrearEnvioConEstadoCreadoYDireccionPorDefinir() {
        PedidoCreadoEvent event = PedidoCreadoEvent.builder()
                .pedidoId(10L)
                .cliente("Juan Perez")
                .producto("Laptop Dell XPS")
                .cantidad(1)
                .total(new BigDecimal("999.99"))
                .createdAt(Instant.now())
                .build();

        Envio guardado = new Envio(1L, "Por definir", "CREADO", 10L, null, null);
        when(envioService.guardarEnvio(any(Envio.class))).thenReturn(guardado);

        listener.handlePedidoCreado(event);

        ArgumentCaptor<Envio> captor = ArgumentCaptor.forClass(Envio.class);
        verify(envioService, times(1)).guardarEnvio(captor.capture());

        Envio enviado = captor.getValue();
        assertEquals(10L, enviado.getPedidoId());
        assertEquals("Por definir", enviado.getDireccion());
        assertEquals("CREADO", enviado.getEstado());
    }

    @Test
    void fallbackCrearEnvio_noDebeLanzarExcepcionNiInteractuarConElServicio() {
        PedidoCreadoEvent event = PedidoCreadoEvent.builder()
                .pedidoId(99L)
                .cliente("Cliente Fallback")
                .producto("Producto X")
                .cantidad(2)
                .build();
        Throwable error = new RuntimeException("Kafka no disponible");

        assertDoesNotThrow(() -> listener.fallbackCrearEnvio(event, error));
        verifyNoInteractions(envioService);
    }
}
