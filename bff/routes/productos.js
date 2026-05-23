const express = require("express");
const { inventarioClient } = require("../services/clients");
const router = express.Router();

router.get("/", async (_req, res, next) => {
  try {
    const { data } = await inventarioClient.get("/productos");
    const productos = data.map((p) => ({
      id:     p.id,
      nombre: p.nombre,
      precio: p.precio,
      stock:  p.stock,
    }));

    res.json(productos);
  } catch (err) {
    console.error("[BFF/productos] Error al listar productos:", err.message);
    next({ status: 502, message: "No se pudo obtener el catálogo de productos" });
  }
});

router.get("/:id", async (req, res, next) => {
  try {
    const { data } = await inventarioClient.get(`/productos/${req.params.id}`);
    res.json(data);
  } catch (err) {
    const status = err.response?.status === 404 ? 404 : 502;
    next({
      status,
      message: status === 404 ? "Producto no encontrado" : "Error al obtener el producto",
    });
  }
});

router.post("/", async (req, res, next) => {
  const { nombre, precio, stock } = req.body;
  if (!nombre || precio == null || stock == null) {
    return next({ status: 400, message: "Faltan campos: nombre, precio, stock" });
  }
  try {
    const { data } = await inventarioClient.post("/productos", { nombre, precio, stock });
    res.status(201).json(data);
  } catch (err) {
    console.error("[BFF/productos] Error al crear producto:", err.message);
    next({ status: 502, message: "No se pudo crear el producto" });
  }
});

module.exports = router;
