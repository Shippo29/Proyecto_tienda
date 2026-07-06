import React, { useState, useEffect } from "react";
import { getBodegas } from "../../services/bodegaService";
import { useApp } from "../../hooks/useApp";
import "./ProductForm.css";

export default function ProductForm({ onSubmit, onCancel }) {
const [form, setForm] = useState({
    nombre: "",
    sku: "",
    precio: "",
    stock: "",
    bodegaId: "",
});
const [bodegas, setBodegas] = useState([]);
const [loadingBodegas, setLoadingBodegas] = useState(true);
const [errors, setErrors] = useState({});
const [isSubmitting, setIsSubmitting] = useState(false);
const { showNotification } = useApp();

useEffect(() => {
    (async () => {
    try {
        const data = await getBodegas();
        setBodegas(Array.isArray(data) ? data : []);
    } catch (err) {
        console.error("Error loading bodegas:", err);
        setBodegas([]);
    } finally {
        setLoadingBodegas(false);
    }
    })();
}, []);

const validateForm = () => {
    const newErrors = {};
    if (!form.nombre.trim()) newErrors.nombre = "El nombre es requerido";
    if (form.precio === "" || Number(form.precio) <= 0)
    newErrors.precio = "El precio debe ser mayor a 0";
    if (form.stock === "" || Number(form.stock) < 0)
    newErrors.stock = "El stock no puede ser negativo";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
};

const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) setErrors((prev) => ({ ...prev, [name]: "" }));
};

const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) {
    showNotification("Por favor, completa todos los campos correctamente", "warning");
    return;
    }

    setIsSubmitting(true);
    try {
    await onSubmit({
        nombre: form.nombre.trim(),
        sku: form.sku.trim() || null,
        precio: parseFloat(form.precio),
        stock: parseInt(form.stock, 10),
        bodegaId: form.bodegaId ? Number(form.bodegaId) : null,
    });
    setForm({ nombre: "", sku: "", precio: "", stock: "", bodegaId: "" });
    } catch (err) {
    } finally {
    setIsSubmitting(false);
    }
};

return (
    <form onSubmit={handleSubmit} className="product-form">
    <div className="form-group">
        <label htmlFor="nombre">Nombre *</label>
        <input
        id="nombre"
        type="text"
        name="nombre"
        value={form.nombre}
        onChange={handleChange}
        placeholder="Ej: Laptop Dell XPS"
        className={errors.nombre ? "error" : ""}
        />
        {errors.nombre && <span className="error-message">{errors.nombre}</span>}
    </div>

    <div className="form-row">
        <div className="form-group">
        <label htmlFor="sku">SKU</label>
        <input
            id="sku"
            type="text"
            name="sku"
            value={form.sku}
            onChange={handleChange}
            placeholder="Ej: SKU-0001"
        />
        </div>

        <div className="form-group">
          <label htmlFor="precio">Precio *</label>
        <input
            id="precio"
            type="number"
            step="0.01"
            min="0"
            name="precio"
            value={form.precio}
            onChange={handleChange}
            className={errors.precio ? "error" : ""}
        />
        {errors.precio && <span className="error-message">{errors.precio}</span>}
        </div>
    </div>

    <div className="form-row">
        <div className="form-group">
          <label htmlFor="stock">Stock *</label>
        <input
            id="stock"
            type="number"
            min="0"
            name="stock"
            value={form.stock}
            onChange={handleChange}
            className={errors.stock ? "error" : ""}
        />
        {errors.stock && <span className="error-message">{errors.stock}</span>}
        </div>

        <div className="form-group">
        <label htmlFor="bodegaId">Bodega/Tienda</label>
        <select
            id="bodegaId"
            name="bodegaId"
            value={form.bodegaId}
            onChange={handleChange}
            disabled={loadingBodegas}
        >
            <option value="">-- Sin asignar --</option>
            {bodegas.map((b) => (
            <option key={b.id} value={b.id}>
                {b.nombre}
            </option>
            ))}
        </select>
        </div>
    </div>

    <div className="form-actions">
        <button type="submit" disabled={isSubmitting} className="btn-primary">
        {isSubmitting ? "Guardando..." : "✅ Agregar Producto"}
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