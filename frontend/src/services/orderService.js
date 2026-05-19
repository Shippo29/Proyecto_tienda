import api from "./api";
import { API_ENDPOINTS, ERROR_MESSAGES } from "../utils/constants";

/**
 * Obtiene la lista de pedidos
 */
export const getOrders = async () => {
  try {
    const response = await api.get(API_ENDPOINTS.ORDERS);
    return response.data || [];
  } catch (error) {
    console.error(ERROR_MESSAGES.FETCH_ORDERS, error);
    throw {
      message: ERROR_MESSAGES.FETCH_ORDERS,
      originalError: error,
    };
  }
};

/**
 * Obtiene un pedido por ID
 */
export const getOrderById = async (id) => {
  try {
    const response = await api.get(`${API_ENDPOINTS.ORDERS}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching order ${id}:`, error);
    throw {
      message: `Error al cargar pedido ${id}`,
      originalError: error,
    };
  }
};

/**
 * Crea un nuevo pedido
 */
export const createOrder = async (payload) => {
  try {
    if (!payload || typeof payload !== "object") {
      throw new Error("Payload inválido");
    }
    
    if (import.meta.env.DEV) {
      console.debug("📦 Creating order with payload:", payload);
    }

    const response = await api.post(API_ENDPOINTS.ORDERS, payload);
    return response.data;
  } catch (error) {
    console.error(ERROR_MESSAGES.CREATE_ORDER, error);
    throw {
      message: ERROR_MESSAGES.CREATE_ORDER,
      originalError: error,
    };
  }
};

/**
 * Actualiza un pedido existente
 */
export const updateOrder = async (id, payload) => {
  try {
    const response = await api.put(`${API_ENDPOINTS.ORDERS}/${id}`, payload);
    return response.data;
  } catch (error) {
    console.error(`Error updating order ${id}:`, error);
    throw {
      message: `Error al actualizar pedido ${id}`,
      originalError: error,
    };
  }
};

/**
 * Elimina un pedido
 */
export const deleteOrder = async (id) => {
  try {
    const response = await api.delete(`${API_ENDPOINTS.ORDERS}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error deleting order ${id}:`, error);
    throw {
      message: `Error al eliminar pedido ${id}`,
      originalError: error,
    };
  }
};

export default {
  getOrders,
  getOrderById,
  createOrder,
  updateOrder,
  deleteOrder,
};
