import api from "./api";
import { API_ENDPOINTS, ERROR_MESSAGES } from "../utils/constants";

/**
 * Obtiene la lista de envíos
 */
export const getShipments = async () => {
  try {
    const response = await api.get(API_ENDPOINTS.SHIPMENTS);
    return response.data || [];
  } catch (error) {
    console.error(ERROR_MESSAGES.FETCH_SHIPMENTS, error);
    throw {
      message: ERROR_MESSAGES.FETCH_SHIPMENTS,
      originalError: error,
    };
  }
};

/**
 * Obtiene un envío por ID
 */
export const getShipmentById = async (id) => {
  try {
    const response = await api.get(`${API_ENDPOINTS.SHIPMENTS}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching shipment ${id}:`, error);
    throw {
      message: `Error al cargar envío ${id}`,
      originalError: error,
    };
  }
};

/**
 * Crea un nuevo envío
 */
export const createShipment = async (payload) => {
  try {
    if (!payload || typeof payload !== "object") {
      throw new Error("Payload inválido");
    }

    if (import.meta.env.DEV) {
      console.debug("📦 Creating shipment with payload:", payload);
    }

    const response = await api.post(API_ENDPOINTS.SHIPMENTS, payload);
    return response.data;
  } catch (error) {
    console.error(ERROR_MESSAGES.CREATE_SHIPMENT, error);
    throw {
      message: ERROR_MESSAGES.CREATE_SHIPMENT,
      originalError: error,
    };
  }
};

/**
 * Actualiza un envío existente
 */
export const updateShipment = async (id, payload) => {
  try {
    const response = await api.put(`${API_ENDPOINTS.SHIPMENTS}/${id}`, payload);
    return response.data;
  } catch (error) {
    console.error(`Error updating shipment ${id}:`, error);
    throw {
      message: `Error al actualizar envío ${id}`,
      originalError: error,
    };
  }
};

/**
 * Elimina un envío
 */
export const deleteShipment = async (id) => {
  try {
    const response = await api.delete(`${API_ENDPOINTS.SHIPMENTS}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting shipment ${id}:`, error);
    throw {
      message: `Error al eliminar envío ${id}`,
      originalError: error,
    };
  }
};

export default {
  getShipments,
  getShipmentById,
  createShipment,
  updateShipment,
  deleteShipment,
};

