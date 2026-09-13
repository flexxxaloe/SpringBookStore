# Spring Bookstore API

A learning backend project for a bookstore: browse books, authenticate users and place orders with stock updates. Built as a portfolio project with Spring Boot, PostgreSQL and automated tests.

## Features

- Public book search with pagination and filters, book details and author lookup.
- JWT authentication, BCrypt passwords and USER/ADMIN access rules.
- ADMIN operations for creating, updating and deleting books/authors, and changing user roles.
- Transactional orders, stock validation and access to the current user's orders.
- Optimistic locking on books to detect conflicting updates.
- Prices and totals stored as Long values in cents: 1000 means EUR 10.00.
- Flyway migrations, Hibernate schema validation and database constraints.

## Stack

Java 17, Spring Boot 4.1.0, Spring Web, Spring Data JPA/Hibernate, Spring Security, PostgreSQL 16, Flyway, JUnit, Mockito and Testcontainers. Maven Wrapper is included. Docker Compose runs the development database; the Java application runs separately.

## Run locally

Requirements: JDK 17, a running Docker engine with Compose, and available ports 5432 and 8080. Run commands from the repository root. A separate Maven installation is not required.

### 1. Configure environment variables

The application requires DB_USERNAME, DB_PASSWORD, ADMIN_USERNAME, ADMIN_PASSWORD, ADMIN_EMAIL and JWT_SECRET.

Set them in the same terminal used to start the application. These are public local-demo values; replace them for your own environment.

PowerShell:

```powershell
$env:DB_USERNAME = 'bookstore'
$env:DB_PASSWORD = 'local-db-password'
$env:ADMIN_USERNAME = 'admin'
$env:ADMIN_PASSWORD = 'local-admin-password'
$env:ADMIN_EMAIL = 'admin@example.com'
$env:JWT_SECRET = 'local-demo-signing-key-replace-me-0123456789abcdef'
.\mvnw.cmd -version
```

Bash:

```bash
export DB_USERNAME='bookstore'
export DB_PASSWORD='local-db-password'
export ADMIN_USERNAME='admin'
export ADMIN_PASSWORD='local-admin-password'
export ADMIN_EMAIL='admin@example.com'
export JWT_SECRET='local-demo-signing-key-replace-me-0123456789abcdef'
bash ./mvnw -version
```

Check that Maven reports Java 17. If necessary, set JAVA_HOME to your JDK installation. When using an IDE, configure the same six variables in the application's run configuration.

JWT_SECRET is used as raw UTF-8 key bytes and must contain at least 32 bytes. The current .env.example lists the variables, but its JWT placeholder is too short and must be replaced.

Docker Compose can read a root .env file automatically. The Java application does **not** automatically load it: pass the values through the terminal or IDE as above. .env is not currently listed in .gitignore; add it before keeping credentials there and do not commit that file.

### 2. Start PostgreSQL

```text
docker compose up -d
docker compose logs postgres
```

Wait until PostgreSQL reports that it is ready to accept connections. The datasource is jdbc:postgresql://localhost:5432/bookstore. The named Docker volume retains data between restarts. For an existing volume, use its original database credentials: changing environment variables does not change an existing user's password.

### 3. Start the application

PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Bash:

```bash
bash ./mvnw spring-boot:run
```

The API uses http://localhost:8080. Flyway applies migrations and Hibernate validates the schema. An ADMIN account is bootstrapped from the configured credentials. Public registration creates USER accounts.

An empty database has no sample books. Create an author and book as ADMIN before trying a purchase.

Stop the application with Ctrl+C. Use docker compose stop to stop PostgreSQL while retaining data.

## Try the API

Use Postman or another HTTP client. The examples below are request bodies; send JSON with Content-Type: application/json. Replace IDs with those returned by your requests.

### 1. Register and log in

POST /auth/register:

```json
{
  "username": "reader",
  "email": "reader@example.com",
  "password": "reader-password"
}
```

POST /auth/login:

```json
{
  "username": "reader",
  "password": "reader-password"
}
```

Login returns a JSON object with a token field. Authenticated requests use:

```http
Authorization: Bearer <token>
```

### 2. Create stock as ADMIN

Log in through /auth/login with ADMIN_USERNAME and ADMIN_PASSWORD. Use the returned ADMIN token for these requests.

POST /authors:

```json
{
  "name": "Demo Author",
  "bio": "Author used for a local API demonstration.",
  "bornDate": "1980-01-01"
}
```

POST /catalog — replace authorId with the returned author ID:

```json
{
  "title": "Demo Book",
  "authorId": 1,
  "description": "A sample book",
  "publicationDate": "2024-01-01",
  "amount": 2,
  "price": 1000
}
```

### 3. Search and purchase as USER

GET /catalog/search?title=Demo&page=0&size=20 and GET /catalog/{id} are public. GET /catalog returns an informational message, not the book list.

Switch back to the reader's token. POST /orders — use the created book ID:

```json
{
  "items": [
    { "bookId": 1, "quantity": 1 }
  ]
}
```

Inspect GET /orders/me or GET /orders/{id} with the same token. A successful purchase decreases stock and records the price at purchase.

## Access rules

- Public: registration/login, GET /catalog/** and GET /authors/**.
- Authenticated: create an order, list own orders and retrieve an own order. Individual order lookup enforces ownership.
- ADMIN: write operations on books/authors, PATCH /users/{id}/role, and GET /orders/admin/{id} for administrative order lookup.

## Tests

Start Docker and run:

```powershell
.\mvnw.cmd verify
```

Or on Bash:

```bash
bash ./mvnw verify
```

Tests supply their own configuration and PostgreSQL container. They do not need the development Compose database or the application environment variables above. The first run may download dependencies and container images.

The last full local verification on September 9, 2026 passed 15 tests: 12 unit tests and 3 service integration tests. The integration tests cover:

- Successful order creation and a reduced stock count.
- Rollback when a later item has insufficient stock.
- Two orders reading the last copy before continuing: exactly one succeeds, the other encounters an optimistic locking failure, and only one order/item remains.

The concurrency test intercepts one repository lookup, performs a real JPA read and synchronizes workers with a barrier. It tests the service and database, not HTTP status mapping. A control run without @Version made it fail because both orders succeeded.

HTTP security/ownership integration tests and GitHub Actions CI are not yet included. Reports are written to target/surefire-reports.

## API documentation status

The springdoc Swagger UI dependency is installed. Its default UI path is /swagger-ui/index.html; the OpenAPI description is at /v3/api-docs. The current security configuration requires authentication for these paths, and a Bearer scheme has not yet been configured. The browser login-and-try workflow is therefore not ready; use an HTTP client for now.

## Design and limitations

The code follows Controller → Service → Repository. DTOs are mapped manually. Order creation is transactional; book versions detect conflicting writes. Application errors are handled centrally, including optimistic locking conflicts mapped to 409.

Remaining work includes HTTP-level security verification, completing PATCH field limits, configuring Swagger authentication and CI. The project has no payment integration or refresh-token flow. It is a learning portfolio project, not a deployed commercial bookstore.
