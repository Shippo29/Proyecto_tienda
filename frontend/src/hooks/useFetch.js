import { useState, useEffect, useCallback, useRef } from "react";

/**
 * @param {Function} fetchFn
 * @param {Object} options 
 */
export function useFetch(fetchFn, options = {}) {
  const { autoLoad = true, onSuccess, onError } = options;
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const fetchFnRef = useRef(fetchFn);
  fetchFnRef.current = fetchFn;

  const onSuccessRef = useRef(onSuccess);
  onSuccessRef.current = onSuccess;

  const onErrorRef = useRef(onError);
  onErrorRef.current = onError;

  const fetch = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const result = await fetchFnRef.current();
      setData(result);
      onSuccessRef.current?.(result);
      return result;
    } catch (err) {
      const errorMsg = err.message || "Error desconocido";
      setError(errorMsg);
      onErrorRef.current?.(err);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []); 

  useEffect(() => {
    if (autoLoad) {
      fetch();
    }
  }, [fetch, autoLoad]);

  const refetch = useCallback(() => fetch(), [fetch]);
  return { data, loading, error, refetch, isLoading: loading, isError: !!error };
}