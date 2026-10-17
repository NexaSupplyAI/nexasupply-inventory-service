# Inventory Service

Microservicio propietario del catálogo, los almacenes y el estado del inventario de NexaSupply AI.

## Responsabilidades

- Categorías y productos.
- Almacenes.
- Saldos por producto y almacén.
- Reservas y movimientos de inventario.
- Consulta de productos con stock bajo.

Es propietario del esquema PostgreSQL `inventory`. Los demás servicios deben consumir su API y no acceder directamente a sus tablas.

## Requisitos

- Java 21.
- PostgreSQL 16 o posterior.
- Un emisor OpenID Connect, como Microsoft Entra ID.
- Esquema inicial creado con [`../nexasupplyai.sql`](../nexasupplyai.sql).

## Configuración

| Variable | Obligatoria | Valor local predeterminado |
|---|---:|---|
| `DB_URL` | No | `jdbc:postgresql://localhost:5432/nexasupplyai` |
| `DB_USER` | No | `postgres` |
| `DB_PASSWORD` | No | `postgres` |
| `JWT_ISSUER_URI` | Producción | `http://localhost:9999` |
| `JWT_JWK_SET_URI` | Producción | `http://localhost:9999/.well-known/jwks.json` |
| `APP_SECURITY_ENABLED` | No | `true` |
| `CORS_ALLOWED_ORIGINS` | No | `http://localhost:4200` |

El esquema se inicializa con el script SQL central. Hibernate lo valida al arrancar y no crea tablas.

## Ejecución

```bash
psql -U postgres -d nexasupplyai -f ../nexasupplyai.sql
export JWT_ISSUER_URI="https://login.microsoftonline.com/<tenant-id>/v2.0"
./mvnw spring-boot:run
```

El servicio escucha en `http://localhost:8081`. Todas las rutas `/api/**` requieren `Authorization: Bearer <token>`. La salud es pública:

```bash
curl http://localhost:8081/actuator/health
```

### Docker

```bash
docker build -t nexasupply/inventory-service:local .
docker run --rm --add-host=host.docker.internal:host-gateway \
  -p 8081:8081 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/nexasupplyai \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  -e JWT_ISSUER_URI=https://login.microsoftonline.com/<tenant-id>/v2.0 \
  nexasupply/inventory-service:local
```

## API

| Método | Ruta | Uso |
|---|---|---|
| `GET/POST` | `/api/v1/categorias` | Listar o crear categorías |
| `GET/PUT/DELETE` | `/api/v1/categorias/{id}` | Consultar, actualizar o eliminar |
| `GET/POST` | `/api/v1/productos?idCategoria={uuid}` | Listar por categoría o crear |
| `GET/PUT/DELETE` | `/api/v1/productos/{id}` | Operaciones por producto |
| `GET/POST` | `/api/v1/almacenes` | Listar o crear almacenes |
| `GET/PUT/DELETE` | `/api/v1/almacenes/{id}` | Operaciones por almacén |
| `GET/POST` | `/api/v1/saldos-inventario?idProducto={uuid}` | Saldos por producto o creación |
| `GET` | `/api/v1/saldos-inventario` | Todos los saldos |
| `GET` | `/api/v1/saldos-inventario/stock-bajo` | Saldos bajo mínimo/punto de reorden |
| `GET/PUT/DELETE` | `/api/v1/saldos-inventario/{id}` | Operaciones por saldo |
| `GET/POST` | `/api/v1/reservas-inventario?idOrdenExterna={uuid}` | Reservas por pedido o creación |
| `GET/PUT/DELETE` | `/api/v1/reservas-inventario/{id}` | Operaciones por reserva |
| `GET/POST` | `/api/v1/movimientos-inventario?idProducto={uuid}` | Movimientos por producto o creación |
| `GET` | `/api/v1/movimientos-inventario/{id}` | Consultar movimiento |
| `POST` | `/api/v1/operaciones-inventario/reservas` | Reservar todas las líneas de un pedido de forma atómica |
| `POST` | `/api/v1/operaciones-inventario/ordenes/{id}/consumir` | Descontar una reserva al completar el pedido |
| `POST` | `/api/v1/operaciones-inventario/ordenes/{id}/liberar` | Liberar la reserva de un pedido cancelado |
| `POST` | `/api/v1/operaciones-inventario/entradas` | Registrar una entrada idempotente de mercadería |

Los valores persistidos de unidad son `UNIT`, `KG`, `G`, `L`, `ML`, `M`, `CM`, `BOX` y `PACK`. Los estados de reserva son `ACTIVE`, `CONFIRMED`, `RELEASED`, `CANCELLED` y `EXPIRED`.

## Pruebas y empaquetado

```bash
./mvnw clean test
./mvnw clean package
```

Los tests de contexto usan H2 y no necesitan PostgreSQL ni un servidor de identidad real.

## Garantías del flujo

Las operaciones usan transacciones, bloqueo pesimista del saldo, versión optimista, claves de idempotencia y restricciones únicas. Reservar, consumir o liberar actualiza saldo, reserva y movimiento dentro de la misma transacción PostgreSQL.
