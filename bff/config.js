module.exports = {
  PORT: process.env.PORT || 8080,

  SERVICES: {
    inventario: process.env.INVENTARIO_URL || "http://localhost:8081",
    pedidos:    process.env.PEDIDOS_URL    || "http://localhost:8082",
    envios:     process.env.ENVIOS_URL     || "http://localhost:8083",
  },

  CORS_ORIGIN: process.env.CORS_ORIGIN || "http://localhost:5173",
};
