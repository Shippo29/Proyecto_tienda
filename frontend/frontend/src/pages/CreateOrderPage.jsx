import React from "react";
import { useNavigate } from "react-router-dom";
import OrderForm from "../components/Order/OrderForm";
import { createOrder } from "../services/orderService";

export default function CreateOrderPage() {
  const navigate = useNavigate();

  const handleSubmit = async (payload) => {
    try {
      const res = await createOrder(payload);
      alert("Pedido creado (id: " + (res.data && res.data.id) + ")");
      // after create, redirect to shipments to observe Kafka result
      navigate("/envios");
    } catch (err) {
      console.error("Error creando pedido", err);
      alert("Error creando pedido (revisa Gateway y backend)");
    }
  };

  return (
    <div>
      <h1>Crear Pedido</h1>
      <OrderForm onSubmit={handleSubmit} />
    </div>
  );
}
