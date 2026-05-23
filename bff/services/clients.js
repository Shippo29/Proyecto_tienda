const axios = require("axios");
const { SERVICES } = require("../config");

const inventarioClient = axios.create({
  baseURL: SERVICES.inventario,
  timeout: 5000,
  headers: { "Content-Type": "application/json" },
});

const pedidosClient = axios.create({
  baseURL: SERVICES.pedidos,
  timeout: 5000,
  headers: { "Content-Type": "application/json" },
});

const enviosClient = axios.create({
  baseURL: SERVICES.envios,
  timeout: 5000,
  headers: { "Content-Type": "application/json" },
});

module.exports = { inventarioClient, pedidosClient, enviosClient };
