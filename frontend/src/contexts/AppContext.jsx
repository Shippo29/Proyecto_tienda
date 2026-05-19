import React, { createContext, useState, useCallback } from "react";

export const AppContext = createContext();

export function AppProvider({ children }) {
  const [notification, setNotification] = useState(null);
  const [loading, setLoading] = useState(false);

  const showNotification = useCallback((message, type = "info", duration = 3000) => {
    setNotification({ message, type });
    if (duration) {
      setTimeout(() => setNotification(null), duration);
    }
  }, []);

  const clearNotification = useCallback(() => {
    setNotification(null);
  }, []);

  const value = {
    notification,
    showNotification,
    clearNotification,
    loading,
    setLoading,
  };

  return (
    <AppContext.Provider value={value}>
      {children}
    </AppContext.Provider>
  );
}
