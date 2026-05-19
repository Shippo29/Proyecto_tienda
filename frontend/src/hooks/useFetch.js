import { useState, useEffect, useCallback } from "react";

/**
 * Hook para gestionar fetch de datos con estados automáticos
 * @param {Function} fetchFn - Función que retorna una Promise
 * @param {Object} options - Opciones: autoLoad, onSuccess, onError
 */
export function useFetch(fetchFn, options = {}) {
  const { autoLoad = true, onSuccess, onError } = options;
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetch = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await fetchFn();
      setData(Array.isArray(result) ? result : result);
      onSuccess?.(result);
      return result;
    } catch (err) {
      const errorMsg = err.message || "Error desconocido";
      setError(errorMsg);
      onError?.(err);
      throw err;
    } finally {
      setLoading(false);
    }
  }, [fetchFn, onSuccess, onError]);

  useEffect(() => {
    if (autoLoad) {
      fetch();
    }
  }, [fetch, autoLoad]);

  const refetch = useCallback(() => fetch(), [fetch]);

  return { data, loading, error, refetch, isLoading: loading, isError: !!error };
}
