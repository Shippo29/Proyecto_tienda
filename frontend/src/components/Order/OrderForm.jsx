import React, { useState, useEffect } from "react";
import { getProducts } from "../../services/productService";

export default function OrderForm({ onSubmit, initial = {} }) {
  const [form, setForm] = useState({
    cliente: initial.cliente || "",
    producto: initial.producto || "",
    cantidad: initial.cantidad || 1,
    total: initial.total || 0,
  });
  const [products, setProducts] = useState([]);

  useEffect(() => {
    getProducts().then((res) => setProducts(res.data || []));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]: name === "cantidad" || name === "total" ? Number(value) : value,
    }));
  };

  return (
    <form onSubmit={(e) => { e.preventDefault(); onSubmit(form); }}>
      <div>
        <label>
          Cliente:{" "}
          <input name="cliente" value={form.cliente} onChange={handleChange} required />
        </label>
      </div>

      <div>
        <label>
          Producto:{" "}
          <select name="producto" value={form.producto} onChange={handleChange} required>
            <option value="">-- Selecciona un producto --</option>
            {products.map((p) => (
              <option key={p.id} value={p.nombre}>
                {p.nombre} (Stock: {p.stock})
              </option>
            ))}
          </select>
        </label>
      </div>

      <div>
        <label>
          Cantidad:{" "}
          <input type="number" name="cantidad" min="1" value={form.cantidad} onChange={handleChange} required />
        </label>
      </div>
      <div>
        <label>
          Total:{" "}
          <input type="number" step="0.01" name="total" value={form.total} onChange={handleChange} required />
        </label>
      </div>
      <div style={{ marginTop: 8 }}>
        <button type="submit">Crear Pedido</button>
      </div>
    </form>
  );
}