import api from "./api";

export const getProducts = () => api.get("/api/v1/productos");
export const getProductById = (id) => api.get(`/api/v1/productos/${id}`);
export const createProduct = (product) => api.post("/api/v1/productos", product);
export const updateProduct = (id, product) => api.put(`/api/v1/productos/${id}`, product);
export const deleteProduct = (id) => api.delete(`/api/v1/productos/${id}`);

export default { getProducts, getProductById, createProduct, updateProduct, deleteProduct };
