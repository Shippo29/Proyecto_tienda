
INSERT INTO productos (nombre, precio, stock)
SELECT 'Laptop Dell XPS', 999.99, 10
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Laptop Dell XPS');

INSERT INTO productos (nombre, precio, stock)
SELECT 'Mouse Logitech MX', 49.99, 50
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Mouse Logitech MX');

INSERT INTO productos (nombre, precio, stock)
SELECT 'Teclado Mecánico', 79.99, 30
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Teclado Mecánico');

INSERT INTO productos (nombre, precio, stock)
SELECT 'Monitor 24"', 299.99, 15
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Monitor 24"');

INSERT INTO productos (nombre, precio, stock)
SELECT 'Auriculares Sony', 149.99, 25
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Auriculares Sony');
