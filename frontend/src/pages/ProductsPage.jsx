import React from "react";
import { getProducts } from "../services/productService";
import ProductCard from "../components/Product/ProductCard";
import { PageStateContainer } from "../components/common/PageStates";
import { useFetch } from "../hooks/useFetch";
import { useApp } from "../hooks/useApp";
import "./pages.css";

export default function ProductsPage() {
  const { showNotification } = useApp();

  const { data: products, loading, error, refetch } = useFetch(getProducts, {
    onSuccess: () => showNotification("✅ Productos cargados", "success", 2000),
    onError: (err) => showNotification(`❌ ${err.message}`, "error"),
  });

  return (
    <div className="page-wrapper">
      <div className="page-header">
        <h1>📦 Productos</h1>
        <button onClick={refetch} disabled={loading} className="btn-reload">
          {loading ? "Cargando..." : "🔄 Recargar"}
        </button>
      </div>

      <PageStateContainer
        loading={loading && !products?.length}
        error={error}
        isEmpty={!loading && !error && !products?.length}
        onRetry={refetch}
      >
        <div className="items-grid">
          {products?.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      </PageStateContainer>
    </div>
  );
}

