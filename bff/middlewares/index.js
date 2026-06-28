function logger(req, _res, next) {
  const now = new Date().toISOString();
  console.log(`[BFF] ${now} | ${req.method} ${req.originalUrl}`);
  if (req.body && Object.keys(req.body).length > 0) {
    console.log(`[BFF] Body:`, JSON.stringify(req.body));
  }
  next();
}

// eslint-disable-next-line no-unused-vars
function errorHandler(err, _req, res, _next) {
  const status = err.status || 500;
  const message = err.message || "Error interno del BFF";
  console.error(`[BFF] ERROR ${status}:`, message);
  res.status(status).json({ error: message });
}

module.exports = { logger, errorHandler };
