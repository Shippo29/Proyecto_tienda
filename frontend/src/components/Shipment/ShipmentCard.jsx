import React from "react";
import "./ShipmentCard.css";

const STATUS_COLORS = {
  PENDIENTE:   "#ff9800",
  EN_CAMINO:   "#2196f3",
  ENTREGADO:   "#4caf50",
  CANCELADO:   "#f44336",
  pending:     "#ff9800",
  in_transit:  "#2196f3",
  delivered:   "#4caf50",
  cancelled:   "#f44336",
};

const STATUS_LABELS = {
  PENDIENTE:   "Pendiente",
  EN_CAMINO:   "En tránsito",
  ENTREGADO:   "Entregado",
  CANCELADO:   "Cancelado",
  pending:     "Pendiente",
  in_transit:  "En tránsito",
  delivered:   "Entregado",
  cancelled:   "Cancelado",
};

const STATUS_ICON = {
  PENDIENTE:  "🕐",
  EN_CAMINO:  "🚚",
  ENTREGADO:  "✅",
  CANCELADO:  "❌",
  pending:    "🕐",
  in_transit: "🚚",
  delivered:  "✅",
  cancelled:  "❌",
};

export default function ShipmentCard({ shipment }) {
  if (!shipment) {
    return <div className="shipment-card error">Envío no disponible</div>;
  }

  const { id, pedidoId, direccion, estado, fecha, cliente, producto, cantidad, total } = shipment;
  const statusLabel = STATUS_LABELS[estado] || estado || "Desconocido";
  const statusColor = STATUS_COLORS[estado] || "#999";
  const statusIcon  = STATUS_ICON[estado]  || "📦";

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
            <span className="label">👤 Cliente:</span>
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

        {fecha && (
          <div className="info-row">
            <span className="label">📅 Fecha:</span>
            <span className="value">{new Date(fecha).toLocaleDateString("es-ES")}</span>
          </div>
        )}
      </div>

      <div className="shipment-timeline">
        <div className={`timeline-item ${["ENTREGADO","delivered"].includes(estado) ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>Entregado</span>
        </div>
        <div className={`timeline-item ${["EN_CAMINO","in_transit"].includes(estado) ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>En tránsito</span>
        </div>
        <div className={`timeline-item ${["PENDIENTE","pending"].includes(estado) ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>Pendiente</span>
        </div>
      </div>
    </div>
  );
}