import React from "react";
import { useNavigate } from "react-router-dom";
import OrderForm from "../components/Order/OrderForm";
import { createOrder } from "../services/orderService";
import { useApp } from "../hooks/useApp";
import "./CreateOrderPage.css";

export default function CreateOrderPage() {
  const navigate = useNavigate();
  const { showNotification } = useApp();

  const handleSubmit = async (payload) => {
    try {
      const result = await createOrder(payload);
      const orderId = result?.id || "desconocido";
      showNotification(
        `✅ Pedido creado exitosamente (ID: ${orderId})`,
        "success",
        3000
      );
      // Redirect after success
      setTimeout(() => {
        navigate("/envios");
      }, 1500);
    } catch (err) {
      const message = err.message || "Error al crear el pedido";
      showNotification(`❌ ${message}`, "error");
      console.error("Error creating order:", err);
    }
  };

  const handleCancel = () => {
    navigate("/");
  };

  return (
    <div className="create-order-page">
      <div className="page-container">
        <h1>➕ Crear Nuevo Pedido</h1>
        <OrderForm onSubmit={handleSubmit} onCancel={handleCancel} />
      </div>
    </div>
  );
}

