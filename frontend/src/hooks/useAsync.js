import { useState, useEffect, useCallback } from "react";

/**
 * Hook para manejar llamadas asincrónicas
 * @param {Function} asyncFunction - Función asincrónica a ejecutar
 * @param {boolean} immediate - Si se debe ejecutar inmediatamente (default: true)
 */
export function useAsync(asyncFunction, immediate = true) {
  const [status, setStatus] = useState("idle");
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  const execute = useCallback(async (...args) => {
    setStatus("pending");
    setData(null);
    setError(null);
    try {
      const response = await asyncFunction(...args);
      setData(response);
      setStatus("success");
      return response;
    } catch (err) {
      setError(err);
      setStatus("error");
      throw err;
    }
  }, [asyncFunction]);

  useEffect(() => {
    if (immediate) {
      execute();
    }
  }, [execute, immediate]);

  return {
    execute,
    status,
    data,
    error,
    isLoading: status === "pending",
    isError: status === "error",
    isSuccess: status === "success",
  };
}
