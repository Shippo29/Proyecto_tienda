package com.example.inventario.inventario_service.controller;

import com.example.inventario.inventario_service.dto.BodegaDTO;
import com.example.inventario.inventario_service.model.Bodega;
import com.example.inventario.inventario_service.repository.BodegaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bodegas")
@Slf4j
public class BodegaController {

    private final BodegaRepository bodegaRepository;

    public BodegaController(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    @GetMapping
    public List<BodegaDTO> listarBodegas() {
        log.debug("BodegaController - GET /bodegas");
        return bodegaRepository.findAll()
                .stream()
                .map(BodegaDTO::fromEntity)
                .toList();
    }

    @PostMapping
    public BodegaDTO guardarBodega(@RequestBody Bodega bodega) {
        log.debug("BodegaController - POST /bodegas body: nombre={} tipo={}", bodega.getNombre(), bodega.getTipo());
        return BodegaDTO.fromEntity(bodegaRepository.save(bodega));
    }
}