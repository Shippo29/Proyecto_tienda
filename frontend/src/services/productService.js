import api from "./api";

export const getProducts = () => api.get("/productos");
export default { getProducts };
