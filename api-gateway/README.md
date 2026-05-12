# Dev API Gateway

This is a small Node.js development API Gateway (proxy) to forward frontend requests to the local microservices.

Install dependencies:

```bash
cd api-gateway
npm install
```

Run gateway:

```bash
npm start
```

Routes proxied:

- GET /productos -> http://localhost:8081/productos
- POST /pedidos -> http://localhost:8082/pedidos
- GET /envios -> http://localhost:8083/envios

CORS is enabled for http://localhost:5173 (Vite dev server).
