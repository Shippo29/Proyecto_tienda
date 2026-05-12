import api from "./api";

export const getShipments = () => api.get("/api/v1/envios");
export const getShipmentById = (id) => api.get(`/api/v1/envios/${id}`);
export const createShipment = (shipment) => api.post("/api/v1/envios", shipment);
export const updateShipment = (id, shipment) => api.put(`/api/v1/envios/${id}`, shipment);
export const deleteShipment = (id) => api.delete(`/api/v1/envios/${id}`);

export default { getShipments, getShipmentById, createShipment, updateShipment, deleteShipment };
