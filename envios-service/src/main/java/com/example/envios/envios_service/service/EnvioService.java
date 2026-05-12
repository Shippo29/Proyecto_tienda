package com.example.envios.envios_service.service;

import com.example.envios.envios_service.model.Envio;

import java.util.List;
import java.util.Optional;

public interface EnvioService {

    List<Envio> obtenerEnvios();

    Envio guardarEnvio(Envio envio);

    Optional<Envio> obtenerEnvioPorId(Long id);

    Envio actualizarEnvio(Long id, Envio envio);

    void eliminarEnvio(Long id);
}