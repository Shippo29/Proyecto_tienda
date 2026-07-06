-- Bodegas / tiendas de la PYME (SmartLogix gestiona stock entre múltiples ubicaciones)
INSERT INTO bodegas (nombre, ubicacion, tipo)
SELECT 'Bodega Central Santiago', 'Santiago, RM', 'BODEGA'
WHERE NOT EXISTS (SELECT 1 FROM bodegas WHERE nombre = 'Bodega Central Santiago');

INSERT INTO bodegas (nombre, ubicacion, tipo)
SELECT 'Tienda Providencia', 'Providencia, RM', 'TIENDA'
WHERE NOT EXISTS (SELECT 1 FROM bodegas WHERE nombre = 'Tienda Providencia');

INSERT INTO bodegas (nombre, ubicacion, tipo)
SELECT 'Bodega Valparaíso', 'Valparaíso, V Región', 'BODEGA'
WHERE NOT EXISTS (SELECT 1 FROM bodegas WHERE nombre = 'Bodega Valparaíso');

-- Productos: stock por bodega/tienda de origen
INSERT INTO productos (nombre, sku, precio, stock, bodega_id)
SELECT 'Laptop Dell XPS', 'SKU-0001', 999.99, 10, (SELECT id FROM bodegas WHERE nombre = 'Bodega Central Santiago')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Laptop Dell XPS');

INSERT INTO productos (nombre, sku, precio, stock, bodega_id)
SELECT 'Mouse Logitech MX', 'SKU-0002', 49.99, 50, (SELECT id FROM bodegas WHERE nombre = 'Bodega Central Santiago')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Mouse Logitech MX');

INSERT INTO productos (nombre, sku, precio, stock, bodega_id)
SELECT 'Teclado Mecánico', 'SKU-0003', 79.99, 30, (SELECT id FROM bodegas WHERE nombre = 'Tienda Providencia')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Teclado Mecánico');

INSERT INTO productos (nombre, sku, precio, stock, bodega_id)
SELECT 'Monitor 24"', 'SKU-0004', 299.99, 15, (SELECT id FROM bodegas WHERE nombre = 'Tienda Providencia')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Monitor 24"');

INSERT INTO productos (nombre, sku, precio, stock, bodega_id)
SELECT 'Auriculares Sony', 'SKU-0005', 149.99, 25, (SELECT id FROM bodegas WHERE nombre = 'Bodega Valparaíso')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Auriculares Sony');
