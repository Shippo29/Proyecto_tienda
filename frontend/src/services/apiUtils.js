import api from "./api";

export async function requestApi(fn, errorMessage) {
  try {
    const response = await fn();
    return response.data;
  } catch (error) {
    console.error(errorMessage, error);
    throw {
      message: errorMessage,
      originalError: error,
    };
  }
}

export function validatePayload(payload, errorMessage) {
  if (!payload || typeof payload !== "object") {
    throw new Error(errorMessage);
  }
  return payload;
}
