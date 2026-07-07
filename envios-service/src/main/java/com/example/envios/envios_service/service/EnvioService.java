package com.example.envios.envios_service.service;

import com.example.envios.envios_service.model.Envio;

import java.util.List;

public interface EnvioService {

    List<Envio> obtenerEnvios();

    Envio guardarEnvio(Envio envio);

    Envio actualizarEnvio(Long id, Envio envio); // 👈 nuevo método
}