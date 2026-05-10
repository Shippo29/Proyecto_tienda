import React from "react";

export default function ShipmentCard({ shipment }) {
  return (
    <div style={{ border: "1px solid #ccc", padding: 12, margin: 8 }}>
      <div>ID: {shipment.id}</div>
      <div>Pedido ID: {shipment.pedidoId}</div>
      <div>Dirección: {shipment.direccion}</div>
      <div>Estado: {shipment.estado}</div>
    </div>
  );
}
