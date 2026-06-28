import React from "react";
import "./ShipmentCard.css";

const STATUS_COLORS = {
  pending: "#ff9800",
  in_transit: "#2196f3",
  delivered: "#4caf50",
  cancelled: "#f44336",
};

const STATUS_LABELS = {
  pending: "Pendiente",
  in_transit: "En tránsito",
  delivered: "Entregado",
  cancelled: "Cancelado",
};

export default function ShipmentCard({ shipment }) {
  if (!shipment) {
    return <div className="shipment-card error">Envío no disponible</div>;
  }

  const { id, pedidoId, direccion, estado, fecha } = shipment;
  const statusLabel = STATUS_LABELS[estado] || estado || "Desconocido";
  const statusColor = STATUS_COLORS[estado] || "#999";

  return (
    <div className="shipment-card">
      <div className="shipment-header">
        <div className="shipment-id">Envío #{id}</div>
        <div className="shipment-status" style={{ backgroundColor: statusColor }}>
          {statusLabel}
        </div>
      </div>

      <div className="shipment-info">
        <div className="info-row">
          <span className="label">📦 Pedido:</span>
          <span className="value">#{pedidoId}</span>
        </div>
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
        <div className={`timeline-item ${estado === "delivered" ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>Entregado</span>
        </div>
        <div className={`timeline-item ${estado === "in_transit" ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>En tránsito</span>
        </div>
        <div className={`timeline-item ${estado === "pending" ? "active" : ""}`}>
          <div className="timeline-dot"></div>
          <span>Pendiente</span>
        </div>
      </div>
    </div>
  );
}

