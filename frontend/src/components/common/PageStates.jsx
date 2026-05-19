import React from "react";
import "./PageStates.css";

/**
 * Componente para mostrar estados de página (loading, error, empty)
 */
export function LoadingState({ message = "Cargando..." }) {
  return (
    <div className="page-state loading-state">
      <div className="spinner"></div>
      <p>{message}</p>
    </div>
  );
}

export function ErrorState({ message, onRetry }) {
  return (
    <div className="page-state error-state">
      <p>⚠️ {message}</p>
      {onRetry && (
        <button onClick={onRetry} className="btn-retry">
          Reintentar
        </button>
      )}
    </div>
  );
}

export function EmptyState({ message = "No hay datos disponibles" }) {
  return (
    <div className="page-state empty-state">
      <p>{message}</p>
    </div>
  );
}

/**
 * Componente contenedor que maneja los estados de página
 */
export function PageStateContainer({ loading, error, isEmpty, onRetry, children }) {
  if (loading) {
    return <LoadingState />;
  }

  if (error) {
    return <ErrorState message={error} onRetry={onRetry} />;
  }

  if (isEmpty) {
    return <EmptyState />;
  }

  return children;
}
