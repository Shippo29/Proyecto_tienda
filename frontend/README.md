# 🛒 Frontend - Tienda Online

Frontend moderno y responsivo para la aplicación de comercio electrónico, construido con **React 19** y **Vite**.

## ✨ Características

- ✅ **Interfaz moderna y responsive** - Diseño mobile-first
- ✅ **Gestión de estado global** - Context API + Custom Hooks
- ✅ **Rutas dinámicas** - React Router v7
- ✅ **API integrada** - Axios con interceptores
- ✅ **Manejo de errores robusto** - Notificaciones toast
- ✅ **Validación de formularios** - Validación client-side
- ✅ **Loading states** - Indicadores visuales de carga
- ✅ **Estilos CSS moderno** - CSS Grid y Flexbox

## 📁 Estructura del Proyecto

```
src/
├── components/           # Componentes reutilizables
│   ├── common/          # Componentes generales (Header, Notification)
│   ├── Order/           # Componentes de pedidos
│   ├── Product/         # Componentes de productos
│   └── Shipment/        # Componentes de envíos
├── contexts/            # Context API
│   └── AppContext.jsx   # Contexto global de la aplicación
├── hooks/               # Custom Hooks
│   ├── useApp.js        # Hook para acceder al contexto
│   └── useAsync.js      # Hook para manejo de datos asincronos
├── pages/               # Páginas/Views
│   ├── CreateOrderPage.jsx
│   ├── OrdersPage.jsx
│   ├── ProductsPage.jsx
│   └── ShipmentsPage.jsx
├── router/              # Configuración de rutas
│   └── AppRouter.jsx
├── services/            # Servicios API
│   ├── api.js           # Configuración de Axios
│   ├── orderService.js
│   ├── productService.js
│   └── shipmentService.js
├── utils/               # Utilidades
│   └── constants.js     # Constantes de la aplicación
├── App.jsx              # Componente raíz
├── main.jsx             # Punto de entrada
└── index.css            # Estilos globales
```

## 🚀 Inicio Rápido

### Prerrequisitos
- Node.js 16+ 
- npm o yarn

### Instalación

1. **Instalar dependencias**
   ```bash
   npm install
   ```

2. **Configurar variables de entorno**
   ```bash
   cp .env.example .env.local
   # Editar .env.local según necesario
   ```

3. **Iniciar servidor de desarrollo**
   ```bash
   npm run dev
   ```
   La aplicación se abrirá en `http://localhost:3000`

### Comandos disponibles

```bash
# Desarrollo
npm run dev          # Inicia servidor de desarrollo

# Build
npm run build        # Compila para producción
npm run preview      # Vista previa del build

# Linting
npm run lint         # Valida el código con ESLint
```

## 🎨 Características de Diseño

### Paleta de Colores
- **Principal**: #3498db (Azul)
- **Fondo**: #f5f7fa (Gris claro)
- **Texto**: #2c3e50 (Gris oscuro)
- **Éxito**: #4caf50 (Verde)
- **Error**: #f44336 (Rojo)
- **Advertencia**: #ff9800 (Naranja)

### Componentes principales

#### Header
- Navegación sticky
- Links activos destacados
- Responsive en mobile

#### Notification System
- Toast notifications automáticas
- Diferentes tipos: success, error, warning, info
- Auto-cierre configurable

#### ProductCard
- Información del producto
- Stock disponible
- Precios formateados
- Estados visuales

#### OrderForm
- Validación en tiempo real
- Selección de productos
- Cálculo automático de cantidades
- Manejo de errores

## 🔌 Integración API

### Servicios disponibles

**Product Service**
```javascript
import { getProducts, getProductById } from '@/services/productService'
```

**Order Service**
```javascript
import { getOrders, createOrder, updateOrder, deleteOrder } from '@/services/orderService'
```

**Shipment Service**
```javascript
import { getShipments, createShipment, updateShipment } from '@/services/shipmentService'
```

### Configuración API

La configuración se define en `src/utils/constants.js`:
```javascript
API_BASE_URL = http://localhost:8080  // URL del gateway
API_TIMEOUT = 10000                   // Timeout en ms
```

## 🛠 Desarrollo

### Crear un nuevo componente

```javascript
// src/components/MyComponent/MyComponent.jsx
import React from 'react'
import './MyComponent.css'

export default function MyComponent() {
  return <div>My Component</div>
}
```

### Usar el contexto global

```javascript
import { useApp } from '@/hooks/useApp'

export default function MyComponent() {
  const { showNotification, notification } = useApp()
  
  const handleAction = () => {
    showNotification('¡Éxito!', 'success')
  }
  
  return <button onClick={handleAction}>Click</button>
}
```

### Consumir datos de la API

```javascript
import { useAsync } from '@/hooks/useAsync'
import { getProducts } from '@/services/productService'

export default function ProductList() {
  const { data: products, isLoading, error } = useAsync(getProducts)
  
  if (isLoading) return <p>Cargando...</p>
  if (error) return <p>Error: {error.message}</p>
  
  return <div>{products.map(p => <p key={p.id}>{p.nombre}</p>)}</div>
}
```

## 📱 Responsive Design

La aplicación es totalmente responsiva con breakpoints:
- **Desktop**: > 1024px
- **Tablet**: 768px - 1024px  
- **Mobile**: < 768px

## 🐛 Manejo de Errores

### Error Handling
- Interceptores de API centralizados
- Mensajes de error amigables
- Retry logic en componentes
- Logging en desarrollo

### Validación
- Validación de formularios en cliente
- Validación en servicios API
- Errores específicos por campo

## 📦 Dependencias principales

- **react**: 19.2.5
- **react-router-dom**: 7.0.0
- **axios**: 1.7.2
- **vite**: 8.0.10

## 🔒 Seguridad

- ✅ Variables sensibles en .env
- ✅ CORS configurado en API Gateway
- ✅ Validación en cliente y servidor
- ✅ Protección contra XSS

## 📝 Buenas Prácticas

1. **Estructura de componentes** - Componentes pequeños y reutilizables
2. **Separación de responsabilidades** - Lógica en servicios y hooks
3. **Consistent naming** - Nombres descriptivos en inglés
4. **Comments** - Documentación de funciones complejas
5. **Error handling** - Manejo robusto de errores
6. **Performance** - Lazy loading y optimización de renders

## 🤝 Contribuir

Para contribuir al proyecto:
1. Crea una rama para tu feature
2. Commit con mensajes descriptivos
3. Push a la rama
4. Crea un Pull Request

## 📞 Soporte

Para reportar bugs o sugerencias, crea un issue en el repositorio.

---

**Versión**: 1.0.0  
**Última actualización**: May 2026
