import api from "./api";
import { requestApi, validatePayload } from "./apiUtils";
import { API_ENDPOINTS, ERROR_MESSAGES } from "../utils/constants";


export const getProducts = async () => {
  return requestApi(
    () => api.get(API_ENDPOINTS.PRODUCTS),
    ERROR_MESSAGES.FETCH_PRODUCTS,
  ).then((data) => data || []);
};


export const getProductById = async (id) => {
  return requestApi(
    () => api.get(`${API_ENDPOINTS.PRODUCTS}/${id}`),
    `Error al cargar producto ${id}`,
  );
};

export const createProduct = async (payload) => {
  validatePayload(payload, "Payload de producto inválido");

  if (import.meta.env.DEV) {
    console.debug("📦 Creating product with payload:", payload);
  }

  return requestApi(
    () => api.post(API_ENDPOINTS.PRODUCTS, payload),
    ERROR_MESSAGES.CREATE_PRODUCT,
  );
};

export default {
  getProducts,
  getProductById,
  createProduct,
};