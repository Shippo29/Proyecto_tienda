# Proyecto Tienda - Microservicios

Este proyecto es una aplicación de tienda implementada con microservicios usando Spring Boot, incluyendo API Gateway, Auth0, Kafka y Circuit Breaker.

## Arquitectura

- **API Gateway**: Servicio de puerta de enlace que enruta las solicitudes a los microservicios apropiados
- **Pedidos Service**: Gestiona los pedidos (puerto 8082)
- **Inventario Service**: Gestiona el inventario (puerto 8081)
- **Envios Service**: Gestiona los envíos (puerto 8083)
- **Kafka**: Mensajería asíncrona entre servicios
- **Auth0**: Autenticación y autorización
- **Circuit Breaker**: Resiliencia con Resilience4j

## Requisitos Previos

- Java 17
- Docker y Docker Compose
- Maven

## Configuración de Auth0

1. Crea una aplicación en Auth0
2. Configura el issuer URI en `api-gateway-service/src/main/resources/application.properties`
3. Actualiza el audience con tu API identifier

## Ejecución con Docker

1. Construye y ejecuta todos los servicios:

```bash
docker-compose up --build
```

2. Los servicios estarán disponibles en:
   - API Gateway: http://localhost:8080
   - Pedidos: http://localhost:8082
   - Inventario: http://localhost:8081
   - Envios: http://localhost:8083

## Ejecución Local

1. Inicia Kafka y MySQL con Docker:

```bash
docker-compose up zookeeper kafka mysql
```

2. Ejecuta cada servicio:

```bash
# En terminales separadas
cd pedidos-service && ./mvnw spring-boot:run
cd inventario-service && ./mvnw spring-boot:run
cd envios-service && ./mvnw spring-boot:run
cd api-gateway-service && ./mvnw spring-boot:run
```

## Endpoints

Todos los endpoints están disponibles a través del API Gateway:

- `GET /api/pedidos/*` - Operaciones de pedidos
- `GET /api/inventario/*` - Operaciones de inventario
- `GET /api/envios/*` - Operaciones de envíos

## Circuit Breaker

Los circuit breakers están configurados para cada servicio con:
- Umbral de tasa de fallo: 50%
- Tiempo de espera en estado abierto: 30 segundos
- Llamadas permitidas en estado semiabierto: 3

## Kafka Topics

Los servicios utilizan Kafka para comunicación asíncrona. Los topics se crean automáticamente.

## Base de Datos

Cada servicio tiene su propia base de datos MySQL:
- pedidos_db
- inventario_db
- envios_db

En Docker, todos usan la base de datos compartida `tienda_db`.