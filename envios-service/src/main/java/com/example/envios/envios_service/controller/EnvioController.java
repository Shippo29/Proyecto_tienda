package com.example.envios.envios_service.controller;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/envios")
@Tag(name = "Envios", description = "Endpoints para gestión de envíos")
@Slf4j
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @Operation(summary = "Listar envíos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envíos listados correctamente")
    })
    @GetMapping
    public List<Envio> obtenerEnvios() {
        log.debug("EnvioController - GET /api/v1/envios");
        return envioService.obtenerEnvios();
    }

    @Operation(summary = "Obtener envío por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío encontrado"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerEnvioPorId(@PathVariable Long id) {
        log.debug("EnvioController - GET /api/v1/envios/{}", id);
        Optional<Envio> envio = envioService.obtenerEnvioPorId(id);
        return envio.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear envío")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<Envio> guardarEnvio(@Valid @RequestBody Envio envio) {
        log.debug("EnvioController - POST /api/v1/envios body: pedidoId={} direccion={} estado={} ", envio.getPedidoId(), envio.getDireccion(), envio.getEstado());
        Envio saved = envioService.guardarEnvio(envio);
        return ResponseEntity.ok(saved);
    }

    @Operation(summary = "Actualizar envío")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Envío actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Envio> actualizarEnvio(@PathVariable Long id, @Valid @RequestBody Envio envio) {
        log.debug("EnvioController - PUT /api/v1/envios/{} body: pedidoId={} direccion={} estado={}", id, envio.getPedidoId(), envio.getDireccion(), envio.getEstado());
        Envio updated = envioService.actualizarEnvio(id, envio);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Eliminar envío")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Envío eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {
        log.debug("EnvioController - DELETE /api/v1/envios/{}", id);
        envioService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }
}