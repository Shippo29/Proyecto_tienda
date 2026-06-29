import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { useAuth0 } from "@auth0/auth0-react";
import { ROUTES } from "../../utils/constants";
import "./Header.css";

export default function Header() {
  const location = useLocation();
  const { isAuthenticated, isLoading, user, loginWithRedirect, logout } = useAuth0();
  const [menuOpen, setMenuOpen] = useState(false);

  const isActive = (path) => location.pathname === path;

  return (
    <header className="header">
      <div className="header-container">
        <div className="header-brand">
          <h1>🛒 Tienda Online</h1>
        </div>

        <nav className="header-nav">
          <Link to={ROUTES.PRODUCTS}     className={`nav-link ${isActive(ROUTES.PRODUCTS)     ? "active" : ""}`}>📦 Productos</Link>
          <Link to={ROUTES.CREATE_ORDER} className={`nav-link ${isActive(ROUTES.CREATE_ORDER) ? "active" : ""}`}>➕ Crear Pedido</Link>
          <Link to={ROUTES.ORDERS}       className={`nav-link ${isActive(ROUTES.ORDERS)       ? "active" : ""}`}>📋 Pedidos</Link>
          <Link to={ROUTES.SHIPMENTS}    className={`nav-link ${isActive(ROUTES.SHIPMENTS)    ? "active" : ""}`}>🚚 Envíos</Link>
        </nav>

        <div className="header-auth">
          {isLoading ? (
            <span className="auth-loading">...</span>
          ) : isAuthenticated ? (
            <div className="auth-user">
              <button
                className="auth-avatar-btn"
                onClick={() => setMenuOpen((o) => !o)}
                title={user?.name}
              >
                {user?.picture
                  ? <img src={user.picture} alt={user.name} className="auth-avatar" />
                  : <span className="auth-avatar-fallback">
                      {(user?.name || user?.email || "U")[0].toUpperCase()}
                    </span>
                }
              </button>

              {menuOpen && (
                <div className="auth-dropdown">
                  <div className="auth-dropdown-name">{user?.name || user?.email}</div>
                  <div className="auth-dropdown-email">{user?.email}</div>
                  <hr className="auth-dropdown-divider" />
                  <button
                    className="auth-logout-btn"
                    onClick={() => {
                      setMenuOpen(false);
                      logout({ logoutParams: { returnTo: window.location.origin } });
                    }}
                  >
                    🚪 Cerrar Sesión
                  </button>
                </div>
              )}
            </div>
          ) : (
            <button
              className="auth-login-btn"
              onClick={() => loginWithRedirect()}
            >
              🔐 Iniciar Sesión
            </button>
          )}
        </div>
      </div>
    </header>
  );
}