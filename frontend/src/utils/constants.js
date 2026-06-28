export const API_BASE_URL = import.meta.env.VITE_API_GATEWAY_URL || "http://localhost:8080";
export const API_TIMEOUT = parseInt(import.meta.env.VITE_API_TIMEOUT || "10000", 10);

export const ROUTES = {
  HOME: "/",
  PRODUCTS: "/",
  CREATE_ORDER: "/pedidos/new",
  ORDERS: "/pedidos",
  SHIPMENTS: "/envios",
};

export const API_ENDPOINTS = {
  PRODUCTS: "/productos",
  ORDERS: "/pedidos",
  SHIPMENTS: "/envios",
};

export const ERROR_MESSAGES = {
  FETCH_PRODUCTS: "Error al cargar productos",
  FETCH_ORDERS: "Error al cargar pedidos",
  FETCH_SHIPMENTS: "Error al cargar envíos",
  CREATE_ORDER: "Error al crear pedido",
  CREATE_SHIPMENT: "Error al crear envío",
  NETWORK: "Error de conexión con el servidor",
  UNKNOWN: "Ocurrió un error inesperado",
};
