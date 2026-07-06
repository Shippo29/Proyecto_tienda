
const CircuitBreaker = require("opossum");

const DEFAULT_OPTIONS = {
timeout: 5000,           
errorThresholdPercentage: 50, 
resetTimeout: 10000,     
volumeThreshold: 3,      
rollingCountTimeout: 10000,
};


const breakers = {};

/**
 * 
 * @param {string} name   
 * @param {Function} fn   
 * @param {object} opts   
 */
function getBreaker(name, fn, opts = {}) {
if (!breakers[name]) {
    const breaker = new CircuitBreaker(fn, { ...DEFAULT_OPTIONS, ...opts });

    breaker.on("open",     () => console.warn(`[CB] ⛔ Circuit OPEN  → ${name} (demasiados errores)`));
    breaker.on("halfOpen", () => console.warn(`[CB] 🔁 Circuit HALF-OPEN → ${name} (probando...)`));
    breaker.on("close",    () => console.info (`[CB] ✅ Circuit CLOSED → ${name} (recuperado)`));
    breaker.on("fallback", () => console.warn (`[CB] 🔀 Fallback ejecutado → ${name}`));
    breaker.on("timeout",  () => console.error(`[CB] ⏱  Timeout → ${name}`));
    breaker.on("reject",   () => console.error(`[CB] 🚫 Rejected (circuito abierto) → ${name}`));

    breakers[name] = breaker;
}
return breakers[name];
}


function getStats() {
return Object.entries(breakers).reduce((acc, [name, cb]) => {
    acc[name] = {
    state:    cb.opened ? "OPEN" : cb.halfOpen ? "HALF_OPEN" : "CLOSED",
    stats:    cb.stats,
    };
    return acc;
}, {});
}

module.exports = { getBreaker, getStats };