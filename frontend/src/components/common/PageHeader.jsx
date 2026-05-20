import React from "react";
import "./PageHeader.css";

export default function PageHeader({ icon, title, actionLabel = "🔄 Recargar", onAction, actionDisabled }) {
  return (
    <div className="page-header">
      <div className="page-title">
        <h1>{icon} {title}</h1>
      </div>
      {onAction && (
        <button onClick={onAction} disabled={actionDisabled} className="btn-reload">
          {actionDisabled ? "Cargando..." : actionLabel}
        </button>
      )}
    </div>
  );
}
