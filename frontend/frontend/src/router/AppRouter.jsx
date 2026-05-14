import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Header from "../components/common/Header";
import ProductsPage from "../pages/ProductsPage";
import CreateOrderPage from "../pages/CreateOrderPage";
import ShipmentsPage from "../pages/ShipmentsPage";

export default function AppRouter() {
  return (
    <BrowserRouter>
      <Header />
      <main style={{ padding: 16 }}>
        <Routes>
          <Route path="/" element={<ProductsPage />} />
          <Route path="/pedidos/new" element={<CreateOrderPage />} />
          <Route path="/envios" element={<ShipmentsPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}
