import api from "./api";

export const getOrders = () => api.get("/api/v1/pedidos");
export const getOrderById = (id) => api.get(`/api/v1/pedidos/${id}`);
export const createOrder = (order) => api.post("/api/v1/pedidos", order);
export const updateOrder = (id, order) => api.put(`/api/v1/pedidos/${id}`, order);
export const deleteOrder = (id) => api.delete(`/api/v1/pedidos/${id}`);

export default { getOrders, getOrderById, createOrder, updateOrder, deleteOrder };
