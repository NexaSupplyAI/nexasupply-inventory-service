# Inventory Service

API propietaria del esquema `inventory`, puerto `8081`. Gestiona categorías, productos, almacenes, saldos, reservas y movimientos.

Las escrituras de stock solo se permiten por `/api/v1/operaciones-inventario`: reservar, consumir, liberar y registrar entrada. Los recursos de saldos, reservas y movimientos son de consulta para evitar saltarse invariantes. Las operaciones usan transacciones, bloqueo pesimista e idempotencia.

```bash
export SPRING_PROFILES_ACTIVE=local DB_URL=jdbc:postgresql://localhost:5432/nexastock DB_USER=postgres DB_PASSWORD='<local>'
./mvnw spring-boot:run
./mvnw clean test
```

Azure: `SPRING_PROFILES_ACTIVE=azure`, `AZURE_POSTGRESQL_JDBC_URL`, `AZURE_POSTGRESQL_USERNAME`, `APP_SECURITY_ENABLED=true`, `AZURE_ENTRA_ISSUER_URI` y `AZURE_ENTRA_AUDIENCE`. PostgreSQL usa `DefaultAzureCredential` mediante el plugin JDBC; no usa contraseña.

Contratos: `/v3/api-docs`; Swagger: `/swagger-ui.html`; health: `/actuator/health`.
