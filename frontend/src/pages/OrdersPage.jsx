import React from "react";
import { getOrders } from "../services/orderService";
import { PageStateContainer } from "../components/common/PageStates";
import { useFetch } from "../hooks/useFetch";
import { useApp } from "../hooks/useApp";
import "./pages.css";

export default function OrdersPage() {
  const { showNotification } = useApp();

  const { data: orders, loading, error, refetch } = useFetch(getOrders, {
    onSuccess: () => showNotification("✅ Pedidos cargados", "success", 2000),
    onError: (err) => showNotification(`❌ ${err.message}`, "error"),
  });

  return (
    <div className="page-wrapper">
      <div className="page-header">
        <h1>📋 Pedidos</h1>
        <button onClick={refetch} disabled={loading} className="btn-reload">
          {loading ? "Cargando..." : "🔄 Recargar"}
        </button>
      </div>

      <PageStateContainer
        loading={loading && !orders?.length}
        error={error}
        isEmpty={!loading && !error && !orders?.length}
        onRetry={refetch}
      >
        <div className="items-list">
          {orders?.map((order) => (
            <div key={order.id} className="order-item">
              <div className="order-header">
                <div className="order-id">Pedido #{order.id}</div>
                <div className="order-status">{order.estado || "Pendiente"}</div>
              </div>
              <div className="order-info">
                <div className="info-row">
                  <span className="label">Cliente:</span>
                  <span className="value">{order.cliente}</span>
                </div>
                <div className="info-row">
                  <span className="label">Producto:</span>
                  <span className="value">{order.producto}</span>
                </div>
              </div>
              <div className="order-details">
                <div className="detail-item">
                  <span className="label">Cantidad:</span>
                  <span className="value">{order.cantidad}</span>
                </div>
                <div className="detail-item">
                  <span className="label">Total:</span>
                  <span className="value price">${parseFloat(order.total || 0).toFixed(2)}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </PageStateContainer>
    </div>
  );
}