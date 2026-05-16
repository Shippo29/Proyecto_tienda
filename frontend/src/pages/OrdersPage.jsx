import React, { useEffect, useState } from "react";
import api from "../services/api";

export default function OrdersPage() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
    setLoading(true);
    api.get("/pedidos")
        .then((res) => setOrders(res.data || []))
        .catch((err) => {
        console.error("Error fetching orders", err);
        alert("Error al cargar pedidos");
        })
        .finally(() => setLoading(false));
    }, []);

return (
    <div>
    <h1>Pedidos</h1>
    {loading ? <p>Cargando...</p> : (
        orders.length === 0 ? <p>No hay pedidos</p> :
        orders.map((o) => (
        <div key={o.id} style={{ border: "1px solid #ccc", padding: 12, margin: 8 }}>
            <div>ID: {o.id}</div>
            <div>Cliente: {o.cliente}</div>
            <div>Producto: {o.producto}</div>
            <div>Cantidad: {o.cantidad}</div>
            <div>Total: {o.total}</div>
        </div>
        ))
    )}
    </div>
    );
}