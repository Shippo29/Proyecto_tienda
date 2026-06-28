import React, { Suspense } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "../components/common/Header";
import ProductsPage from "../pages/ProductsPage";
import CreateOrderPage from "../pages/CreateOrderPage";
import ShipmentsPage from "../pages/ShipmentsPage";
import OrdersPage from "../pages/OrdersPage";
import "./AppRouter.css";

// Página de error 404
function NotFound() {
  return (
    <div className="not-found">
      <h1>404</h1>
      <p>Página no encontrada</p>
      <a href="/">Volver al inicio</a>
    </div>
  );
}

// Loading fallback
function LoadingFallback() {
  return (
    <div className="loading-fallback">
      <p>Cargando página...</p>
    </div>
  );
}

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Header />
      <main className="main-content">
        <Suspense fallback={<LoadingFallback />}>
          <Routes>
            <Route path="/" element={<ProductsPage />} />
            <Route path="/pedidos/new" element={<CreateOrderPage />} />
            <Route path="/envios" element={<ShipmentsPage />} />
            <Route path="/pedidos" element={<OrdersPage />} />
            {/* Catch all - 404 */}
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Suspense>
      </main>
    </BrowserRouter>
  );
}

