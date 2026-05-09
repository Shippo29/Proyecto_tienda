package com.example.gateway.api_gateway_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/fallback/pedidos")
    public Mono<String> pedidosFallback() {
        return Mono.just("Servicio de pedidos no disponible. Por favor intente nuevamente más tarde.");
    }

    @GetMapping("/fallback/inventario")
    public Mono<String> inventarioFallback() {
        return Mono.just("Servicio de inventario no disponible. Por favor intente nuevamente más tarde.");
    }

    @GetMapping("/fallback/envios")
    public Mono<String> enviosFallback() {
        return Mono.just("Servicio de envíos no disponible. Por favor intente nuevamente más tarde.");
    }
}
