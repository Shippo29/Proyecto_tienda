import React from "react";
import { Link, useLocation } from "react-router-dom";
import { ROUTES } from "../../utils/constants";
import "./Header.css";

export default function Header() {
  const location = useLocation();

  const isActive = (path) => location.pathname === path;

  return (
    <header className="header">
      <div className="header-container">
        <div className="header-brand">
          <h1>🛒 Tienda Online</h1>
        </div>
        <nav className="header-nav">
          <Link
            to={ROUTES.PRODUCTS}
            className={`nav-link ${isActive(ROUTES.PRODUCTS) ? "active" : ""}`}
          >
            📦 Productos
          </Link>
          <Link
            to={ROUTES.CREATE_ORDER}
            className={`nav-link ${isActive(ROUTES.CREATE_ORDER) ? "active" : ""}`}
          >
            ➕ Crear Pedido
          </Link>
          <Link
            to={ROUTES.ORDERS}
            className={`nav-link ${isActive(ROUTES.ORDERS) ? "active" : ""}`}
          >
            📋 Pedidos
          </Link>
          <Link
            to={ROUTES.SHIPMENTS}
            className={`nav-link ${isActive(ROUTES.SHIPMENTS) ? "active" : ""}`}
          >
            🚚 Envíos
          </Link>
        </nav>
      </div>
    </header>
  );
}

