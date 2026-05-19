import api from "./api";
import { API_ENDPOINTS, ERROR_MESSAGES } from "../utils/constants";

/**
 * Obtiene la lista de productos
 */
export const getProducts = async () => {
  try {
    const response = await api.get(API_ENDPOINTS.PRODUCTS);
    return response.data || [];
  } catch (error) {
    console.error(ERROR_MESSAGES.FETCH_PRODUCTS, error);
    throw {
      message: ERROR_MESSAGES.FETCH_PRODUCTS,
      originalError: error,
    };
  }
};

/**
 * Obtiene un producto por ID
 */
export const getProductById = async (id) => {
  try {
    const response = await api.get(`${API_ENDPOINTS.PRODUCTS}/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching product ${id}:`, error);
    throw {
      message: `Error al cargar producto ${id}`,
      originalError: error,
    };
  }
};

export default {
  getProducts,
  getProductById,
};
