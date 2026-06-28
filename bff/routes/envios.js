const express = require("express");
const { enviosClient, pedidosClient } = require("../services/clients");

const router = express.Router();

router.get("/", async (_req, res, next) => {
  try {
    console.log("[BFF/envios] Consultando envios-service y pedidos-service en paralelo...");
    const [enviosRes, pedidosRes] = await Promise.all([
      enviosClient.get("/envios"),
      pedidosClient.get("/pedidos"),
    ]);

    const envios  = enviosRes.data;
    const pedidos = pedidosRes.data;

    const pedidosPorId = pedidos.reduce((mapa, pedido) => {
      mapa[pedido.id] = pedido;
      return mapa;
    }, {});

    const enviosEnriquecidos = envios.map((envio) => {
      const pedido = pedidosPorId[envio.pedidoId];
      return {
        id:        envio.id,
        pedidoId:  envio.pedidoId,
        direccion: envio.direccion,
        estado:    envio.estado,
        cliente:   pedido?.cliente  ?? "Desconocido",
        producto:  pedido?.producto ?? "Desconocido",
        cantidad:  pedido?.cantidad ?? null,
        total:     pedido?.total    ?? null,
      };
    });

    res.json(enviosEnriquecidos);
  } catch (err) {
    console.error("[BFF/envios] Error al obtener envíos:", err.message);
    next({ status: 502, message: "No se pudo obtener la lista de envíos" });
  }
});

router.get("/:id", async (req, res, next) => {
  try {
    const { data } = await enviosClient.get(`/envios/${req.params.id}`);
    res.json(data);
  } catch (err) {
    const status = err.response?.status === 404 ? 404 : 502;
    next({
      status,
      message: status === 404 ? "Envío no encontrado" : "Error al obtener el envío",
    });
  }
});

router.post("/", async (req, res, next) => {
  const { pedidoId, direccion, estado } = req.body;

  if (!pedidoId || !direccion) {
    return next({ status: 400, message: "Faltan campos: pedidoId, direccion" });
  }

  try {
    const { data } = await enviosClient.post("/envios", {
      pedidoId,
      direccion,
      estado: estado || "PENDIENTE",
    });
    res.status(201).json(data);
  } catch (err) {
    console.error("[BFF/envios] Error al crear envío:", err.message);
    next({ status: 502, message: "No se pudo crear el envío" });
  }
});

router.put("/:id", async (req, res, next) => {
  try {
    const { data } = await enviosClient.put(`/envios/${req.params.id}`, req.body);
    res.json(data);
  } catch (err) {
    console.error("[BFF/envios] Error al actualizar envío:", err.message);
    next({ status: 502, message: "Error al actualizar el envío" });
  }
});

router.delete("/:id", async (req, res, next) => {
  try {
    await enviosClient.delete(`/envios/${req.params.id}`);
    res.status(204).send();
  } catch (err) {
    console.error("[BFF/envios] Error al eliminar envío:", err.message);
    next({ status: 502, message: "Error al eliminar el envío" });
  }
});

module.exports = router;
