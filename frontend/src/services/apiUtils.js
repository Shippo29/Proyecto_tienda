import api from "./api";

export async function requestApi(fn, errorMessage) {
  try {
    const response = await fn();
    return response.data;
  } catch (error) {
    const serverMessage =
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      error?.message;

    const finalMessage = serverMessage || errorMessage;

    console.error(errorMessage, error);
    throw {
      message: finalMessage,
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
