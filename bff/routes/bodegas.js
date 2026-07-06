const express = require("express");
const { inventarioClient } = require("../services/clients");
const router = express.Router();

router.get("/", async (_req, res, next) => {
  try {
    const { data } = await inventarioClient.get("/bodegas");
    res.json(data);
  } catch (err) {
    console.error("[BFF/bodegas] Error al listar bodegas:", err.message);
    next({ status: 502, message: "No se pudo obtener la lista de bodegas" });
  }
});

router.post("/", async (req, res, next) => {
  const { nombre, ubicacion, tipo } = req.body;
  if (!nombre || !tipo) {
    return next({ status: 400, message: "Faltan campos: nombre, tipo" });
  }
  try {
    const { data } = await inventarioClient.post("/bodegas", { nombre, ubicacion, tipo });
    res.status(201).json(data);
  } catch (err) {
    console.error("[BFF/bodegas] Error al crear bodega:", err.message);
    next({ status: 502, message: "No se pudo crear la bodega" });
  }
});

module.exports = router;
