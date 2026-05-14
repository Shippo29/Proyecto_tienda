import React, { useEffect, useState } from "react";
import { getProducts } from "../services/productService";
import ProductCard from "../components/Product/ProductCard";

export default function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    getProducts()
      .then((res) => setProducts(res.data || []))
      .catch((err) => {
        console.error("Error fetching products", err);
        alert("Error al cargar productos (revisa Gateway)");
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div>
      <h1>Productos</h1>
      {loading ? (
        <p>Cargando...</p>
      ) : (
        <div>
          {products.length === 0 ? (
            <p>No hay productos</p>
          ) : (
            products.map((p) => <ProductCard key={p.id} product={p} />)
          )}
        </div>
      )}
    </div>
  );
}
