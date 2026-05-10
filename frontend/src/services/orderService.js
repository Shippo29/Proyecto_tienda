import api from "./api";

export const createOrder = (payload) => {
  try {
    console.debug("FRONTEND DEBUG - createOrder payload:", payload);
  } catch (e) {
    console.debug("FRONTEND DEBUG - createOrder payload (err)", e);
  }
  return api.post("/pedidos", payload);
};
export default { createOrder };
