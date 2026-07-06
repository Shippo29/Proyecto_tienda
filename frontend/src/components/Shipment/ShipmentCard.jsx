import React, { useState } from "react";
import "./ShipmentCard.css";

const STATUS_COLORS = {
  PENDIENTE:   "#ff9800",
  EN_CAMINO:   "#2196f3",
  ENTREGADO:   "#4caf50",
  CANCELADO:   "#f44336",
};

const STATUS_LABELS = {
  PENDIENTE:   "Pendiente",
  EN_CAMINO:   "En tránsito",
  ENTREGADO:   "Entregado",
  CANCELADO:   "Cancelado",
};

const STATUS_ICON = {
  PENDIENTE:  "🕐",
  EN_CAMINO:  "🚚",
  ENTREGADO:  "✅",
  CANCELADO:  "❌",
};

const ESTADOS_DISPONIBLES = ["PENDIENTE", "EN_CAMINO", "ENTREGADO", "CANCELADO"];

export default function ShipmentCard({ shipment, onStatusChange }) {
  const [nuevoEstado, setNuevoEstado] = useState(shipment?.estado || "PENDIENTE");
  const [updating, setUpdating] = useState(false);

  if (!shipment) {
    return <div className="shipment-card error">Envío no disponible</div>;
  }

  const { id, pedidoId, direccion, estado, fecha, cliente, producto, cantidad, total, transportista, rutaEstimada } = shipment;
  const statusLabel = STATUS_LABELS[estado] || estado || "Desconocido";
  const statusColor = STATUS_COLORS[estado] || "#999";
  const statusIcon  = STATUS_ICON[estado]  || "📦";

  const handleGuardarEstado = async () => {
    if (!onStatusChange || nuevoEstado === estado) return;
    setUpdating(true);
    try {
      await onStatusChange(id, nuevoEstado);
    } finally {
      setUpdating(false);
    }
  };

  return (
    <div className="shipment-card">
      <div className="shipment-header">
        <div className="shipment-id">Envío #{id}</div>
        <div className="shipment-status" style={{ backgroundColor: statusColor }}>
          {statusIcon} {statusLabel}
        </div>
      </div>

      <div className="shipment-info">
        <div className="info-row">
          <span className="label">🧾 Pedido:</span>
          <span className="value">#{pedidoId}</span>
        </div>

        {cliente && cliente !== "Desconocido" && (
          <div className="info-row">
            <span className="label">🏢 Empresa:</span>
            <span className="value">{cliente}</span>
          </div>
        )}

        {producto && producto !== "Desconocido" && (
          <div className="info-row">
            <span className="label">📦 Producto:</span>
            <span className="value">
              {producto}{cantidad ? ` × ${cantidad}` : ""}
            </span>
          </div>
        )}

        {total != null && (
          <div className="info-row">
            <span className="label">💰 Total:</span>
            <span className="value highlight">${parseFloat(total).toFixed(2)}</span>
          </div>
        )}

        <div className="info-row">
          <span className="label">📍 Dirección:</span>
          <span className="value">{direccion || "No especificada"}</span>
        </div>

        {transportista && (
          <div className="info-row">
            <span className="label">🚛 Transportista:</span>
            <span className="value">{transportista}</span>
          </div>
        )}

        {rutaEstimada && (
          <div className="info-row">
            <span className="label">🗺️ Ruta estimada:</span>
            <span className="value">{rutaEstimada}</span>
          </div>
        )}

        {fecha && (
          <div className="info-row">
            <span className="label">📅 Fecha:</span>
            <span className="value">{new Date(fecha).toLocaleDateString("es-ES")}</span>
          </div>
        )}
      </div>

      {onStatusChange && (
        <div className="shipment-status-editor">
          <select
            value={nuevoEstado}
            onChange={(e) => setNuevoEstado(e.target.value)}
            disabled={updating}
          >
            {ESTADOS_DISPONIBLES.map((e) => (
              <option key={e} value={e}>{STATUS_LABELS[e]}</option>
            ))}
          </select>
          <button
            onClick={handleGuardarEstado}
            disabled={updating || nuevoEstado === estado}
          >
            {updating ? "Guardando..." : "Guardar estado"}
          </button>
        </div>
      )}

      <div className="shipment-timeline">
        <div className={`timeline-item ${estado === "ENTREGADO" ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>Entregado</span>
        </div>
        <div className={`timeline-item ${estado === "EN_CAMINO" ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>En tránsito</span>
        </div>
        <div className={`timeline-item ${estado === "PENDIENTE" ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>Pendiente</span>
        </div>
      </div>
    </div>
  );
}