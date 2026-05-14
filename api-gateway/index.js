const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
const cors = require("cors");

const app = express();
const PORT = process.env.PORT || 8080;

// CORS para desarrollo con Vite
app.use(cors({ origin: "http://localhost:5173", credentials: true }));
app.use(express.json());

// Middleware de registro (detallado para desarrollo)
app.use((req, res, next) => {
  try {
    if (req.method === "GET") {
      console.log(`[API-GATEWAY] ${req.method} ${req.originalUrl}`);
    } else {
      console.log(
        `[API-GATEWAY] ${req.method} ${req.originalUrl} headers=${JSON.stringify(req.headers)}`,
      );
      if (req.body && Object.keys(req.body).length > 0) {
        console.log(
          `[API-GATEWAY] ${req.method} ${req.originalUrl} body=${JSON.stringify(req.body)}`,
        );
      }
    }
  } catch (e) {
    console.log("[API-GATEWAY] logging error", e);
  }
  next();
});

function createProxy(target, pathRewrite) {
  return createProxyMiddleware({
    target,
    changeOrigin: true,
    pathRewrite,
    onProxyReq: (proxyReq, req, res) => {
      try {
        console.log(
          `[API-GATEWAY] Proxying request -> ${req.method} ${req.originalUrl} -> ${target}${req.originalUrl}`,
        );
        console.log("[API-GATEWAY] ProxyReq headers:", proxyReq.getHeaders());
        if (req.body && Object.keys(req.body).length > 0) {
          console.log("[API-GATEWAY] ProxyReq body:", JSON.stringify(req.body));
        }
      } catch (e) {
        console.log("[API-GATEWAY] onProxyReq debug error", e);
      }
    },
    onProxyRes: (proxyRes, req, res) => {
      console.log(
        `[API-GATEWAY] Proxy response from target for ${req.originalUrl} status=${proxyRes.statusCode}`,
      );
    },
  });
}

// Rutas de proxy
app.use(
  "/productos",
  createProxy("http://localhost:8081", { "^/productos": "/productos" }),
);
app.use(
  "/pedidos",
  createProxy("http://localhost:8082", { "^/pedidos": "/pedidos" }),
);
app.use(
  "/envios",
  createProxy("http://localhost:8083", { "^/envios": "/envios" }),
);

app.listen(PORT, () => {
  console.log(`API Gateway running on http://localhost:${PORT}`);
});
