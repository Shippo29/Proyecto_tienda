import api from "./api";
import { requestApi, validatePayload } from "./apiUtils";
import { API_ENDPOINTS, ERROR_MESSAGES } from "../utils/constants";


export const getShipments = async () => {
  return requestApi(
    () => api.get(API_ENDPOINTS.SHIPMENTS),
    ERROR_MESSAGES.FETCH_SHIPMENTS,
  ).then((data) => data || []);
};

export const getShipmentById = async (id) => {
  return requestApi(
    () => api.get(`${API_ENDPOINTS.SHIPMENTS}/${id}`),
    `Error al cargar envío ${id}`,
  );
};


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

export const updateShipment = async (id, payload) => {
  return requestApi(
    () => api.put(`${API_ENDPOINTS.SHIPMENTS}/${id}`, payload),
    `Error al actualizar envío ${id}`,
  );
};

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