package com.example.envios.envios_service.controller;

import com.example.envios.envios_service.model.Envio;
import com.example.envios.envios_service.service.EnvioService;
import com.example.envios.envios_service.exception.ResourceNotFoundException;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/envios")
@Slf4j
@Tag(name = "Envios", description = "Operaciones CRUD sobre envíos")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    @Operation(summary = "Listar envíos")
    public ResponseEntity<List<Envio>> obtenerEnvios() {
        log.debug("EnvioController - GET /envios");
        return ResponseEntity.ok(envioService.obtenerEnvios());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener envío por id")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Envío encontrado"), @ApiResponse(responseCode = "404", description = "Envío no encontrado")})
    public ResponseEntity<Envio> obtenerEnvio(@PathVariable Long id) {
        return envioService.obtenerEnvioPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado con id " + id));
    }

    @PostMapping
    @Operation(summary = "Crear envío")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Envío creado")})
    public ResponseEntity<Envio> guardarEnvio(@RequestBody Envio envio) {
        log.debug("EnvioController - POST /envios body: pedidoId={} direccion={} estado={} ", envio.getPedidoId(), envio.getDireccion(), envio.getEstado());
        Envio saved = envioService.guardarEnvio(envio);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar envío")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Envío actualizado"), @ApiResponse(responseCode = "404", description = "Envío no encontrado")})
    public ResponseEntity<Envio> actualizarEnvio(@PathVariable Long id, @RequestBody Envio envio) {
        Envio updated = envioService.actualizarEnvio(id, envio);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar envío")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Envío eliminado"), @ApiResponse(responseCode = "404", description = "Envío no encontrado")})
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {
        envioService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }
}