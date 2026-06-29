const axios   = require("axios");
const { SERVICES } = require("../config");
const { getBreaker } = require("./circuitBreaker");

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


function withBreaker(name, client) {
  const get    = getBreaker(`${name}.GET`,    (url, cfg)  => client.get(url, cfg));
  const post   = getBreaker(`${name}.POST`,   (url, data) => client.post(url, data));
  const put    = getBreaker(`${name}.PUT`,    (url, data) => client.put(url, data));
  const del    = getBreaker(`${name}.DELETE`, (url)       => client.delete(url));

  const fallback = (method) => (err) => {
    throw Object.assign(
      new Error(`Servicio ${name} no disponible (${method}). Intenta de nuevo en unos segundos.`),
      { status: 503, isCircuitOpen: true }
    );
  };

  get.fallback(fallback("GET"));
  post.fallback(fallback("POST"));
  put.fallback(fallback("PUT"));
  del.fallback(fallback("DELETE"));

  return {
    get:    (url, cfg)  => get.fire(url, cfg),
    post:   (url, data) => post.fire(url, data),
    put:    (url, data) => put.fire(url, data),
    delete: (url)       => del.fire(url),
  };
}

module.exports = {
  inventarioClient: withBreaker("inventario", inventarioClient),
  pedidosClient:    withBreaker("pedidos",    pedidosClient),
  enviosClient:     withBreaker("envios",     enviosClient),
};