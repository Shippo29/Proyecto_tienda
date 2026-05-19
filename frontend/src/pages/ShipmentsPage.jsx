import React from "react";
import { getShipments } from "../services/shipmentService";
import ShipmentCard from "../components/Shipment/ShipmentCard";
import { PageStateContainer } from "../components/common/PageStates";
import { useFetch } from "../hooks/useFetch";
import { useApp } from "../hooks/useApp";
import "./pages.css";

export default function ShipmentsPage() {
  const { showNotification } = useApp();

  const { data: shipments, loading, error, refetch } = useFetch(getShipments, {
    onSuccess: () => showNotification("✅ Envíos cargados", "success", 2000),
    onError: (err) => showNotification(`❌ ${err.message}`, "error"),
  });

  return (
    <div className="page-wrapper">
      <div className="page-header">
        <h1>🚚 Envíos</h1>
        <button onClick={refetch} disabled={loading} className="btn-reload">
          {loading ? "Cargando..." : "🔄 Recargar"}
        </button>
      </div>

      <PageStateContainer
        loading={loading && !shipments?.length}
        error={error}
        isEmpty={!loading && !error && !shipments?.length}
        onRetry={refetch}
      >
        <div className="items-grid">
          {shipments?.map((shipment) => (
            <ShipmentCard key={shipment.id} shipment={shipment} />
          ))}
        </div>
      </PageStateContainer>
    </div>
  );
}

