import React from "react";
import "./ProductCard.css";

export default function ProductCard({ product }) {
  if (!product) {
    return <div className="product-card error">Producto no disponible</div>;
  }

  const { id, nombre, precio, stock, descripcion } = product;
  const isOutOfStock = stock <= 0;

  return (
    <div className={`product-card ${isOutOfStock ? "out-of-stock" : ""}`}>
      <div className="product-header">
        <h3 className="product-name">{nombre || "Sin nombre"}</h3>
        {isOutOfStock && <span className="out-of-stock-badge">Agotado</span>}
      </div>

      {descripcion && (
        <p className="product-description">{descripcion}</p>
      )}

      <div className="product-info">
        <div className="product-price">
          <span className="label">Precio</span>
          <span className="value">${parseFloat(precio || 0).toFixed(2)}</span>
        </div>
        <div className="product-stock">
          <span className="label">Stock</span>
          <span className={`value ${isOutOfStock ? "low" : ""}`}>
            {stock || 0} unidades
          </span>
        </div>
      </div>

      <div className="product-id">ID: {id}</div>
    </div>
  );
}

