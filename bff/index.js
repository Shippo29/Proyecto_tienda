const express = require("express");
const cors    = require("cors");
const { PORT, CORS_ORIGIN }    = require("./config");
const { logger, errorHandler } = require("./middlewares");
const { getStats }             = require("./services/circuitBreaker");
const productosRouter          = require("./routes/productos");
const pedidosRouter            = require("./routes/pedidos");
const enviosRouter             = require("./routes/envios");

const app = express();

app.use(cors({ origin: CORS_ORIGIN, credentials: true }));
app.use(express.json());
app.use(logger);

app.use("/productos", productosRouter);
app.use("/pedidos",   pedidosRouter);
app.use("/envios",    enviosRouter);

app.get("/health", (_req, res) => {
  res.json({
    status:          "OK",
    service:         "smartlogix-bff",
    port:            PORT,
    circuitBreakers: getStats(),
  });
});

app.use(errorHandler);

app.listen(PORT, () => {
  console.log("================================================");
  console.log(`  SmartLogix BFF corriendo en http://localhost:${PORT}`);
  console.log("================================================");
  console.log("  Rutas disponibles:");
  console.log("    GET  /health  (incluye estado de Circuit Breakers)");
  console.log("    GET  /productos");
  console.log("    POST /productos");
  console.log("    GET  /pedidos");
  console.log("    POST /pedidos  (verifica stock antes de crear)");
  console.log("    GET  /envios   (enriquecido con datos del pedido)");
  console.log("    POST /envios");
  console.log("================================================");
});