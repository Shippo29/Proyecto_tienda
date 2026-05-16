import React from "react";
import { Link } from "react-router-dom";

export default function Header() {
  return (
    <header
      style={{ padding: 10, borderBottom: "1px solid #ddd", marginBottom: 16 }}
    >
      <Link to="/" style={{ marginRight: 12 }}>
        Productos
      </Link>
      <Link to="/pedidos/new" style={{ marginRight: 12 }}>
        Crear Pedido
      </Link>
      <Link to="/envios">Envíos</Link>
      <Link to="/pedidos" style={{ marginRight: 12 }}>Pedidos</Link>
    </header>
  );
}
