import axios from "axios";
import { API_BASE_URL, API_TIMEOUT, ERROR_MESSAGES } from "../utils/constants";

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: API_TIMEOUT,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const isDev = import.meta.env.DEV;
    if (isDev) {
      console.debug("🚀 API Request:", {
        method: config.method.toUpperCase(),
        url: `${config.baseURL}${config.url}`,
        data: config.data,
      });
    }
    return config;
  },
  (error) => {
    console.error("❌ Request error:", error);
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => {
    const isDev = import.meta.env.DEV;
    if (isDev) {
      console.debug("✅ API Response:", {
        status: response.status,
        url: response.config.url,
        data: response.data,
      });
    }
    return response;
  },
  (error) => {
    let message = ERROR_MESSAGES.UNKNOWN;

    if (error.response) {
      console.error("Server error:", error.response.status, error.response.data);
    } else if (error.request) {
      message = ERROR_MESSAGES.NETWORK;
      console.error("No response from server:", error.request);
    } else {
      console.error("Error:", error.message);
    }

    return Promise.reject(error);
  }
);

export default api;
