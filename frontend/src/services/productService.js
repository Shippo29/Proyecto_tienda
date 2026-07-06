import api from "./api";
import { requestApi } from "./apiUtils";
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

export default {
  getProducts,
  getProductById,
};
