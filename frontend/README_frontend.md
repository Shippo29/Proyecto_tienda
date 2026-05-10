# Frontend (React + Vite)

Archivos creados para integrarse con API Gateway y los microservicios:

- .env
- src/
  - services/api.js
  - services/productService.js
  - services/orderService.js
  - services/shipmentService.js
  - components/common/Header.jsx
  - components/Product/ProductCard.jsx
  - components/Order/OrderForm.jsx
  - components/Shipment/ShipmentCard.jsx
  - pages/ProductsPage.jsx
  - pages/CreateOrderPage.jsx
  - pages/ShipmentsPage.jsx
  - router/AppRouter.jsx
  - App.jsx
  - main.jsx
  - index.css

## Comandos

Instalar dependencias (si no las instalaste):

```bash
cd frontend
npm install axios react-router-dom
```

Levantar frontend:

```bash
npm run dev
```

Asegúrate de que `VITE_API_GATEWAY_URL` en `.env` apunta al API Gateway (p.ej. http://localhost:8080).

## Rutas esperadas en API Gateway

- GET /productos
- POST /pedidos
- GET /envios

Si tu Gateway mapea rutas con prefijos (p.ej. /api/productos), actualiza `src/services/*.js` para usar esas rutas.

## Pruebas manuales

1. Levanta Kafka y microservicios.
2. Abre frontend en http://localhost:5173
3. Ver productos -> crear pedido -> ver envíos
4. Revisa logs en `pedidos-service` para ver publicación al topic `pedidos.created` y en `envios-service`/`inventario-service` para consumo.
