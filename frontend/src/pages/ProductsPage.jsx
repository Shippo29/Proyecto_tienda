import React, { useState } from "react";
import { getProducts, createProduct } from "../services/productService";
import ProductCard from "../components/Product/ProductCard";
import ProductForm from "../components/Product/ProductForm";
import PageHeader from "../components/common/PageHeader";
import { PageStateContainer } from "../components/common/PageStates";
import { useFetch } from "../hooks/useFetch";
import { useApp } from "../hooks/useApp";
import "./pages.css";

export default function ProductsPage() {
  const { showNotification } = useApp();
  const [showForm, setShowForm] = useState(false);

  const { data: products, loading, error, refetch } = useFetch(getProducts, {
    onSuccess: () => showNotification("✅ Productos cargados", "success", 2000),
    onError: (err) => showNotification(`❌ ${err.message}`, "error"),
  });

  const handleCreate = async (payload) => {
    try {
      const nuevo = await createProduct(payload);
      showNotification(`✅ Producto "${nuevo.nombre}" creado correctamente`, "success", 3000);
      setShowForm(false);
      refetch();
    } catch (err) {
      showNotification(`❌ ${err.message || "Error al crear el producto"}`, "error");
      throw err; 
    }
  };

  return (
    <div className="page-wrapper">
      <PageHeader
        icon="📦"
        title="Inventario"
        actionLabel="🔄 Recargar"
        onAction={refetch}
        actionDisabled={loading}
      />

      <div style={{ marginBottom: "20px" }}>
        <button className="btn-secondary" onClick={() => setShowForm((v) => !v)}>
          {showForm ? "✕ Cancelar" : "➕ Nuevo producto"}
        </button>
      </div>

      {showForm && (
        <ProductForm onSubmit={handleCreate} onCancel={() => setShowForm(false)} />
      )}

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