# Spring Bookstore API

A REST API for a simple online bookstore built with Spring Boot.

The project was created to practise backend development with Spring, JPA, PostgreSQL, security, transactions, validation, and database migrations.

## Tech Stack

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* Hibernate
* Flyway
* Lombok
* Docker / Docker Compose

## How to Run

### Requirements

* Java 17
* Maven
* Docker
* Docker Compose

### 1. Clone the repository

```bash
git clone <repository-url>
cd SpringBookStore
```

### 2. Start PostgreSQL

```bash
docker compose up -d
```

### 3. Start the application

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

Flyway applies the database migrations automatically on startup.

Hibernate is configured with:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

so the database schema is managed by Flyway and validated by Hibernate.

## Basic Usage

Typical usage flow:

```text
Register
   ↓
Login
   ↓
Browse catalog
   ↓
Create an order
```

1. Register a new user.
2. Login and receive a JWT token.
3. Use the token in protected requests:

```http
Authorization: Bearer <token>
```

4. Browse the book catalog.
5. Create and view orders.

Administrative endpoints can be restricted to users with the `ADMIN` role.

## Architecture

The application follows a layered architecture:

```text
Controller → Service → Repository → PostgreSQL
```

* **Controller** — handles HTTP requests and request validation.
* **Service** — contains business logic and transactional operations.
* **Repository** — provides database access through Spring Data JPA.
* **DTOs** are used for API requests and responses.
* Mapping between DTOs and entities is implemented manually.

## Technical Decisions

* **Flyway** is used for reproducible database schema migrations.
* Hibernate uses `ddl-auto=validate` instead of creating tables automatically.
* Important rules are enforced both through DTO validation and database constraints such as `NOT NULL`, `UNIQUE`, foreign keys, and `CHECK`.
* **Optimistic locking** is used to detect concurrent modifications of books.
* **JWT authentication** is used for stateless authentication.
* Entity relationships use lazy loading where appropriate.

## Error Handling

The application uses centralized exception handling with `@ControllerAdvice`.

Typical responses include:

* `400 Bad Request` — invalid request data
* `401 Unauthorized` — authentication is required
* `403 Forbidden` — insufficient permissions
* `404 Not Found` — resource does not exist
* `409 Conflict` — conflicting updates or database constraint violations

## Known Limitations

* No real payment provider is integrated.
* JWT authentication does not use refresh tokens.
* Order and inventory processing are simplified.
* The project is intended as a learning project and does not include production-level monitoring or distributed caching.
