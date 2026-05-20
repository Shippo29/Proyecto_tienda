import api from "./api";
import { requestApi, validatePayload } from "./apiUtils";
import { API_ENDPOINTS, ERROR_MESSAGES } from "../utils/constants";

/**
 * Obtiene la lista de envíos
 */
export const getShipments = async () => {
  return requestApi(
    () => api.get(API_ENDPOINTS.SHIPMENTS),
    ERROR_MESSAGES.FETCH_SHIPMENTS,
  ).then((data) => data || []);
};

/**
 * Obtiene un envío por ID
 */
export const getShipmentById = async (id) => {
  return requestApi(
    () => api.get(`${API_ENDPOINTS.SHIPMENTS}/${id}`),
    `Error al cargar envío ${id}`,
  );
};

/**
 * Crea un nuevo envío
 */
export const createShipment = async (payload) => {
  validatePayload(payload, "Payload inválido");

  if (import.meta.env.DEV) {
    console.debug("📦 Creating shipment with payload:", payload);
  }

  return requestApi(
    () => api.post(API_ENDPOINTS.SHIPMENTS, payload),
    ERROR_MESSAGES.CREATE_SHIPMENT,
  );
};

/**
 * Actualiza un envío existente
 */
export const updateShipment = async (id, payload) => {
  return requestApi(
    () => api.put(`${API_ENDPOINTS.SHIPMENTS}/${id}`, payload),
    `Error al actualizar envío ${id}`,
  );
};

/**
 * Elimina un envío
 */
export const deleteShipment = async (id) => {
  return requestApi(
    () => api.delete(`${API_ENDPOINTS.SHIPMENTS}/${id}`),
    `Error al eliminar envío ${id}`,
  );
};

export default {
  getShipments,
  getShipmentById,
  createShipment,
  updateShipment,
  deleteShipment,
};

