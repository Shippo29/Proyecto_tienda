import api from "./api";

export const getShipments = () => api.get("/envios");
export default { getShipments };
