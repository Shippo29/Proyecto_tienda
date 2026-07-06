const express = require("express");
const { pedidosClient, inventarioClient } = require("../services/clients");
const router = express.Router();

router.get("/", async (_req, res, next) => {
  try {
    const { data } = await pedidosClient.get("/pedidos");
    res.json(data);
  } catch (err) {
    console.error("[BFF/pedidos] Error al listar pedidos:", err.message);
    next({ status: 502, message: "No se pudo obtener la lista de pedidos" });
  }
});

router.get("/:id", async (req, res, next) => {
  try {
    const { data } = await pedidosClient.get(`/pedidos/${req.params.id}`);
    res.json(data);
  } catch (err) {
    const status = err.response?.status === 404 ? 404 : 502;
    next({
      status,
      message: status === 404 ? "Pedido no encontrado" : "Error al obtener el pedido",
    });
  }
});

router.post("/", async (req, res, next) => {
  const { cliente, producto, cantidad, total, bodegaOrigen } = req.body;
  if (!cliente || !producto || !cantidad || total == null) {
    return next({
      status: 400,
      message: "Faltan campos obligatorios: cliente, producto, cantidad, total",
    });
  }
  try {
    console.log(`[BFF/pedidos] Verificando stock de "${producto}" antes de crear pedido...`);
    const { data: productos } = await inventarioClient.get("/productos");
    const productoEncontrado = productos.find(
      (p) => p.nombre.toLowerCase() === producto.toLowerCase()
    );
    if (!productoEncontrado) {
      return next({
        status: 422,
        message: `El producto "${producto}" no existe en el inventario`,
      });
    }
    if (productoEncontrado.stock < cantidad) {
      return next({
        status: 422,
        message: `Stock insuficiente para "${producto}". Disponible: ${productoEncontrado.stock}, solicitado: ${cantidad}`,
      });
    }

    console.log(`[BFF/pedidos] Stock OK (${productoEncontrado.stock} unidades). Creando pedido...`);
    const { data: pedidoCreado } = await pedidosClient.post("/pedidos", {
      cliente,
      producto,
      cantidad,
      total,
      bodegaOrigen,
    });

    console.log(`[BFF/pedidos] Pedido creado con id=${pedidoCreado.id}`);
    res.status(201).json(pedidoCreado);
  } catch (err) {
    console.error("[BFF/pedidos] Error al procesar pedido:", err.message);
    next({ status: 502, message: "Error al procesar el pedido" });
  }
});

router.put("/:id", async (req, res, next) => {
  try {
    const { data } = await pedidosClient.put(`/pedidos/${req.params.id}`, req.body);
    res.json(data);
  } catch (err) {
    console.error("[BFF/pedidos] Error al actualizar pedido:", err.message);
    next({ status: 502, message: "Error al actualizar el pedido" });
  }
});

router.delete("/:id", async (req, res, next) => {
  try {
    await pedidosClient.delete(`/pedidos/${req.params.id}`);
    res.status(204).send();
  } catch (err) {
    console.error("[BFF/pedidos] Error al eliminar pedido:", err.message);
    next({ status: 502, message: "Error al eliminar el pedido" });
  }
});

module.exports = router;
