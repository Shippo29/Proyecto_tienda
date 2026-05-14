import React from "react";

export default function ProductCard({ product }) {
  return (
    <div style={{ border: "1px solid #ccc", padding: 12, margin: 8 }}>
      <h3>{product.nombre}</h3>
      <div>Precio: {product.precio}</div>
      <div>Stock: {product.stock}</div>
    </div>
  );
}
