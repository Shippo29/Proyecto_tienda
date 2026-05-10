import React, { useEffect, useState } from "react";
import { getShipments } from "../services/shipmentService";
import ShipmentCard from "../components/Shipment/ShipmentCard";

export default function ShipmentsPage() {
  const [shipments, setShipments] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    getShipments()
      .then((res) => setShipments(res.data || []))
      .catch((err) => {
        console.error("Error fetching shipments", err);
        alert("Error al cargar envíos (revisa Gateway)");
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h1>Envíos</h1>
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <div>
          {shipments.length === 0 ? (
            <p>No hay envíos</p>
          ) : (
            shipments.map((s) => <ShipmentCard key={s.id} shipment={s} />)
          )}
        </div>
      )}
    </div>
  );
}
