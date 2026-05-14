import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_GATEWAY_URL || "http://localhost:8080",
  timeout: parseInt(import.meta.env.VITE_API_TIMEOUT || "10000", 10),
});

// Interceptores de depuración (solo en desarrollo)
api.interceptors.request.use((config) => {
  try {
    console.debug("FRONTEND DEBUG -> Request", {
      method: config.method,
      url: config.baseURL ? `${config.baseURL}${config.url}` : config.url,
      headers: config.headers,
      data: config.data,
    });
  } catch (e) {
    console.debug("FRONTEND DEBUG -> Request (error serializing)", e);
  }
  return config;
});

api.interceptors.response.use(
  (response) => {
    try {
      console.debug("FRONTEND DEBUG <- Response", {
        status: response.status,
        url: response.config.url,
        data: response.data,
      });
    } catch (e) {
      console.debug("FRONTEND DEBUG <- Response (error)", e);
    }
    return response;
  },
  (error) => {
    try {
      console.debug(
        "FRONTEND DEBUG <- Response Error",
        error &&
          (error.response
            ? { status: error.response.status, data: error.response.data }
            : error.message),
      );
    } catch (e) {
      console.debug("FRONTEND DEBUG <- Response Error (serializing)", e);
    }
    return Promise.reject(error);
  },
);

export default api;
