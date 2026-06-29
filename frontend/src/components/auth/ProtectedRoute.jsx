import React from "react";
import { useAuth0 } from "@auth0/auth0-react";

export default function ProtectedRoute({ children }) {
const { isAuthenticated, isLoading, loginWithRedirect } = useAuth0();

if (isLoading) {
    return (
    <div style={{
        display: "flex", justifyContent: "center",
        alignItems: "center", height: "60vh", flexDirection: "column", gap: 16
    }}>
        <div style={{ fontSize: 40 }}>🔐</div>
        <p style={{ color: "#a0aec0" }}>Verificando sesión...</p>
    </div>
    );
}

if (!isAuthenticated) {
    return (
    <div style={{
        display: "flex", justifyContent: "center",
        alignItems: "center", height: "60vh", flexDirection: "column", gap: 24
    }}>
        <div style={{ fontSize: 56 }}>🔒</div>
        <h2 style={{ color: "#f5f6fa", margin: 0 }}>Acceso restringido</h2>
        <p style={{ color: "#a0aec0", margin: 0 }}>
        Debes iniciar sesión para ver esta página.
        </p>
        <button
        onClick={() => loginWithRedirect()}
        style={{
            padding: "12px 32px",
            background: "linear-gradient(90deg, #6366f1, #4f46e5)",
            color: "#fff",
            border: "none",
            borderRadius: 8,
            fontSize: 16,
            fontWeight: 600,
            cursor: "pointer",
        }}
        >
        🚀 Iniciar Sesión
        </button>
    </div>
    );
}

return children;
}