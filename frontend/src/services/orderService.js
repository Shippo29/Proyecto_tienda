import api from "./api";
import { requestApi, validatePayload } from "./apiUtils";
import { API_ENDPOINTS, ERROR_MESSAGES } from "../utils/constants";


export const getOrders = async () => {
  return requestApi(
    () => api.get(API_ENDPOINTS.ORDERS),
    ERROR_MESSAGES.FETCH_ORDERS,
  ).then((data) => data || []);
};


export const getOrderById = async (id) => {
  return requestApi(
    () => api.get(`${API_ENDPOINTS.ORDERS}/${id}`),
    `Error al cargar pedido ${id}`,
  );
};


export const createOrder = async (payload) => {
  validatePayload(payload, "Payload inválido");

  if (import.meta.env.DEV) {
    console.debug("📦 Creating order with payload:", payload);
  }

  return requestApi(
    () => api.post(API_ENDPOINTS.ORDERS, payload),
    ERROR_MESSAGES.CREATE_ORDER,
  );
};

/**
 * Actualiza un pedido existente
 */
export const updateOrder = async (id, payload) => {
  return requestApi(
    () => api.put(`${API_ENDPOINTS.ORDERS}/${id}`, payload),
    `Error al actualizar pedido ${id}`,
  );
};


export const deleteOrder = async (id) => {
  return requestApi(
    () => api.delete(`${API_ENDPOINTS.ORDERS}/${id}`),
    `Error al eliminar pedido ${id}`,
  );
};

export default {
  getOrders,
  getOrderById,
  createOrder,
  updateOrder,
  deleteOrder,
};
