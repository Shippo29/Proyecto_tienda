import React from "react";
import { useNavigate } from "react-router-dom";
import OrderForm from "../components/Order/OrderForm";
import PageHeader from "../components/common/PageHeader";
import { createOrder } from "../services/orderService";
import { createShipment } from "../services/shipmentService";
import { useApp } from "../hooks/useApp";
import "./CreateOrderPage.css";

export default function CreateOrderPage() {
  const navigate = useNavigate();
  const { showNotification } = useApp();

  const handleSubmit = async (payload) => {
    const { direccion, ...orderPayload } = payload;

    try {
      const result = await createOrder(orderPayload);
      const orderId = result?.id || "desconocido";

      try {
        await createShipment({
          pedidoId: result.id,
          direccion: direccion,
          estado: "PENDIENTE",
        });
        showNotification(
          `✅ Pedido #${orderId} creado y envío generado correctamente`,
          "success",
          3500
        );
      } catch (shipErr) {
        console.error("Error al crear envío:", shipErr);
        showNotification(
          `⚠️ Pedido #${orderId} creado, pero no se pudo generar el envío automáticamente`,
          "warning",
          4000
        );
      }

      setTimeout(() => {
        navigate("/envios");
      }, 1800);
    } catch (err) {
      const message = err.message || "Error al crear el pedido";
      showNotification(`❌ ${message}`, "error");
      console.error("Error creating order:", err);
      throw err;
    }
  };

  const handleCancel = () => {
    navigate("/");
  };

  return (
    <div className="create-order-page">
      <div className="page-container">
        <PageHeader icon="➕" title="Registrar Nuevo Pedido" />
        <OrderForm onSubmit={handleSubmit} onCancel={handleCancel} />
      </div>
    </div>
  );
}