# Ticket Booking System

A learning/demo ticket-booking backend built with Spring Boot microservices. It demonstrates how an API gateway, independently running services, a relational database, and Kafka can work together to expose event inventory and process ticket bookings asynchronously.

## What the project does

The system lets a caller inspect venues and events, then request tickets for an event. The booking service checks the customer and available inventory, calculates the total price, and publishes a booking event to Kafka. The order service consumes that event, saves an order to MySQL, and updates the remaining event capacity through the inventory service.

The API gateway is the public entry point. It routes the supported inventory and booking requests, validates JWT bearer tokens using Keycloak, and applies a circuit breaker to booking requests. This is a demonstration project; it is not a complete production ticketing platform.

## Services and components

| Component | Purpose | Default address |
| --- | --- | --- |
| API Gateway | Authenticates and routes supported API requests | `http://localhost:8090` |
| Inventory Service | Reads venue/event inventory and updates event capacity | `http://localhost:8080` |
| Booking Service | Validates and submits booking requests | `http://localhost:8081` |
| Order Service | Consumes booking events, stores orders, and updates inventory | `http://localhost:8082` |
| MySQL | Stores customers, venues, events, and orders | Host port configured in Docker Compose |
| Kafka | Delivers booking events from Booking Service to Order Service | `localhost:9092` |
| Keycloak | Issues JWTs used by the gateway | `http://localhost:8091` |

`inventoryservice/docker-compose.yml` starts the local infrastructure, including MySQL, Kafka, Zookeeper, Kafka UI, Schema Registry, and Keycloak. The four services are separate Maven projects rather than a single root Maven build.

## Requirements

- Java 21
- Docker with the Docker Compose plugin
- A terminal and `curl` (or another HTTP client)

## Setup and run

1. **Start the infrastructure.** From the repository root:

   ```bash
   cd inventoryservice
   docker compose up -d
   ```

   The Compose file publishes MySQL on host port `3307`, Kafka on `9092`, Kafka UI on `8084`, Schema Registry on `8083`, and Keycloak on `8091`. Kafka UI is available at `http://localhost:8084`.

2. **Configure database connectivity.** Each service's `src/main/resources/application.properties` currently uses `localhost:3306`; the Compose file publishes MySQL on `3307`. Before starting the services, set the datasource URL in all three database-using services (`inventoryservice`, `bookingservice`, and `orderservice`) to the published host port (for example, `jdbc:mysql://localhost:3307/ticketing`). Set each service's database username and password to credentials that match the MySQL service in `docker-compose.yml`. The Inventory Service runs the Flyway migrations that create the application tables.

3. **Configure Keycloak.** The gateway expects a Keycloak realm named `ticketing-security-realm` and a valid JWT issuer at `http://localhost:8091/realms/ticketing-security-realm`. The Compose file starts Keycloak in development mode and mounts `inventoryservice/docker/keycloak/realms/` as its realm-import directory. Provide a realm export there (or configure the realm and an appropriate client/user in Keycloak) before trying authenticated gateway requests. Use an access token issued by that realm as a bearer token.

4. **Run the services.** Open a separate terminal for each service and run:

   ```bash
   cd inventoryservice
   ./mvnw spring-boot:run
   ```

   Repeat the command from the repository root in `orderservice`, `bookingservice`, and `apigateway`. Start the Inventory Service first so that database migrations complete; ensure MySQL and Kafka are ready before starting the services. Use the configured ports shown above.

To stop the infrastructure, run `docker compose down` from `inventoryservice`. This preserves the named MySQL volume. To remove persisted Compose data as well, use `docker compose down -v` (this deletes the local database contents).

## Using the API

Send requests through the gateway at `http://localhost:8090`. For protected API routes, include a bearer token from the configured Keycloak realm:

```http
Authorization: Bearer YOUR_KEYCLOAK_ACCESS_TOKEN
```

Supported gateway routes:

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/v1/inventory/venue/{venueId}` | Get venue information |
| `GET` | `/api/v1/inventory/event/{eventId}` | Get event and remaining inventory |
| `POST` | `/api/v1/booking` | Submit a booking request |

Example booking request:

```bash
curl -X POST http://localhost:8090/api/v1/booking \
  -H "Authorization: Bearer YOUR_KEYCLOAK_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"eventId":1,"ticketCount":2}'
```

The `userId` must identify an existing customer and the event must have sufficient remaining capacity. A successful response confirms the booking request was accepted for asynchronous processing; the Order Service then consumes the Kafka event, writes the order, and updates inventory.

The gateway's combined Swagger UI is at `http://localhost:8090/swagger-ui.html`. Inventory and booking API documentation is also exposed through the gateway at `/docs/inventoryservice/v3/api-docs` and `/docs/bookingservice/v3/api-docs`.

## Build and test

Run the Maven wrapper from any service directory:

```bash
./mvnw test
./mvnw package
```
