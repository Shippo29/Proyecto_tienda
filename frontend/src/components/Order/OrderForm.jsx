import React, { useState, useEffect } from "react";
import { getProducts } from "../../services/productService";
import { useApp } from "../../hooks/useApp";
import "./OrderForm.css";

export default function OrderForm({ onSubmit, onCancel, initial = {} }) {
  const [form, setForm] = useState({
    cliente: initial.cliente || "",
    producto: initial.producto || "",
    cantidad: initial.cantidad || 1,
    total: initial.total || 0,
    direccion: initial.direccion || "",
  });
  const [products, setProducts] = useState([]);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { showNotification } = useApp();

  useEffect(() => {
    loadProducts();
  }, []);

  useEffect(() => {
    if (selectedProduct) {
      const total = parseFloat(selectedProduct.precio || 0) * form.cantidad;
      setForm((prev) => ({ ...prev, total: parseFloat(total.toFixed(2)) }));
    }
  }, [selectedProduct, form.cantidad]);

  const loadProducts = async () => {
    try {
      setLoading(true);
      const data = await getProducts();
      setProducts(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error("Error loading products:", err);
      showNotification("Error al cargar productos", "error");
      setProducts([]);
    } finally {
      setLoading(false);
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!form.cliente.trim()) {
      newErrors.cliente = "El nombre del cliente es requerido";
    }
    if (!form.producto) {
      newErrors.producto = "Debes seleccionar un producto";
    }
    if (form.cantidad < 1) {
      newErrors.cantidad = "La cantidad debe ser mayor a 0";
    }
    if (selectedProduct && form.cantidad > selectedProduct.stock) {
      newErrors.cantidad = `Stock insuficiente (disponible: ${selectedProduct.stock})`;
    }
    if (form.total <= 0) {
      newErrors.total = "El total debe ser mayor a 0";
    }
    if (!form.direccion.trim()) {
      newErrors.direccion = "La dirección de envío es requerida";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    let newValue = value;

    if (name === "cantidad") {
      newValue = value === "" ? 1 : Math.max(1, Number(value));
    }

    if (name === "producto") {
      const product = products.find((p) => p.nombre === value);
      setSelectedProduct(product || null);
      if (!product) {
        setForm((prev) => ({ ...prev, producto: value, total: 0 }));
        return;
      }
    }

    setForm((prev) => ({ ...prev, [name]: newValue }));

    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      showNotification("Por favor, completa todos los campos correctamente", "warning");
      return;
    }

    setIsSubmitting(true);
    try {
      await onSubmit(form);
    } catch (err) {
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return <div className="form-loading">Cargando formulario...</div>;
  }

  return (
    <form onSubmit={handleSubmit} className="order-form">
      <div className="form-group">
        <label htmlFor="cliente">Nombre del Cliente *</label>
        <input
          id="cliente"
          type="text"
          name="cliente"
          value={form.cliente}
          onChange={handleChange}
          placeholder="Ej: Juan Pérez"
          className={errors.cliente ? "error" : ""}
          required
        />
        {errors.cliente && <span className="error-message">{errors.cliente}</span>}
      </div>

      <div className="form-group">
        <label htmlFor="producto">Producto *</label>
        <select
          id="producto"
          name="producto"
          value={form.producto}
          onChange={handleChange}
          className={errors.producto ? "error" : ""}
          required
        >
          <option value="">-- Selecciona un producto --</option>
          {products.map((p) => (
            <option key={p.id} value={p.nombre} disabled={p.stock === 0}>
              {p.nombre} (Stock: {p.stock}) - ${parseFloat(p.precio || 0).toFixed(2)}
            </option>
          ))}
        </select>
        {errors.producto && <span className="error-message">{errors.producto}</span>}
        {selectedProduct && (
          <div className="product-info">
            <p>Precio unitario: ${parseFloat(selectedProduct.precio || 0).toFixed(2)}</p>
          </div>
        )}
      </div>

      <div className="form-row">
        <div className="form-group">
          <label htmlFor="cantidad">Cantidad *</label>
          <input
            id="cantidad"
            type="number"
            name="cantidad"
            min="1"
            value={form.cantidad}
            onChange={handleChange}
            className={errors.cantidad ? "error" : ""}
            required
          />
          {errors.cantidad && <span className="error-message">{errors.cantidad}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="total">Total ($)</label>
          <input
            id="total"
            type="number"
            step="0.01"
            name="total"
            value={form.total}
            readOnly
            disabled
            placeholder="0.00"
            style={{ backgroundColor: "#f0f4f8", fontWeight: "600", color: "#2c3e50" }}
          />
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="direccion">📍 Dirección de Envío *</label>
        <input
          id="direccion"
          type="text"
          name="direccion"
          value={form.direccion}
          onChange={handleChange}
          placeholder="Ej: Av. Siempre Viva 742, Santiago"
          className={errors.direccion ? "error" : ""}
          required
        />
        {errors.direccion && <span className="error-message">{errors.direccion}</span>}
      </div>

      <div className="form-actions">
        <button type="submit" disabled={isSubmitting} className="btn-primary">
          {isSubmitting ? "Creando..." : "✅ Crear Pedido"}
        </button>
        {onCancel && (
          <button type="button" onClick={onCancel} className="btn-secondary" disabled={isSubmitting}>
            ✕ Cancelar
          </button>
        )}
      </div>
    </form>
  );
}