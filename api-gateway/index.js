const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
const cors = require("cors");

const app = express();
const PORT = process.env.PORT || 8080;


const INVENTARIO_URL = process.env.INVENTARIO_URL || "http://localhost:8081";
const PEDIDOS_URL    = process.env.PEDIDOS_URL    || "http://localhost:8082";
const ENVIOS_URL     = process.env.ENVIOS_URL     || "http://localhost:8083";


app.use(cors({ origin: process.env.CORS_ORIGIN || "http://localhost:5173", credentials: true }));
app.use(express.json());


app.use((req, res, next) => {
  try {
    if (req.method === "GET") {
      console.log(`[API-GATEWAY] ${req.method} ${req.originalUrl}`);
    } else {
      console.log(`[API-GATEWAY] ${req.method} ${req.originalUrl} headers=${JSON.stringify(req.headers)}`);
      if (req.body && Object.keys(req.body).length > 0) {
        console.log(`[API-GATEWAY] ${req.method} ${req.originalUrl} body=${JSON.stringify(req.body)}`);
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
    on: {
      proxyReq: (proxyReq, req) => {
        if (req.body && Object.keys(req.body).length > 0) {
          const bodyData = JSON.stringify(req.body);
          proxyReq.setHeader('Content-Type', 'application/json');
          proxyReq.setHeader('Content-Length', Buffer.byteLength(bodyData));
          proxyReq.write(bodyData);
        }
      },
      proxyRes: (proxyRes, req) => {
        console.log(`[API-GATEWAY] ${req.method} ${req.originalUrl} -> ${proxyRes.statusCode}`);
      }
    }
  });
}


app.use("/productos", createProxy(INVENTARIO_URL, { "^/productos": "/productos" }));
app.use("/pedidos",   createProxy(PEDIDOS_URL,    { "^/pedidos":   "/pedidos"   }));
app.use("/envios",    createProxy(ENVIOS_URL,      { "^/envios":    "/envios"    }));

app.listen(PORT, () => {
  console.log(`API Gateway running on http://localhost:${PORT}`);
  console.log(`  -> /productos -> ${INVENTARIO_URL}`);
  console.log(`  -> /pedidos   -> ${PEDIDOS_URL}`);
  console.log(`  -> /envios    -> ${ENVIOS_URL}`);
});