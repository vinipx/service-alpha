# service-alpha

Primary Spring Boot 3.2.x RESTful microservice exposing CRUD APIs for User management. Depends on `com.ecosystem:common-library:1.0.0-SNAPSHOT` for shared DTOs, utilities, and exception handling.

## Prerequisites

- Java 21
- Maven 3.9+
- `common-library` installed in local Maven repository (`mvn install` in the common-library project)

## Build Instructions

```bash
# Build and run tests
mvn clean verify

# Build without tests
mvn clean package -DskipTests

# Run locally
mvn spring-boot:run
```

## API Documentation

The service runs on port **8081**.

| Method | Endpoint              | Description         | Response              |
|--------|-----------------------|---------------------|-----------------------|
| GET    | /api/v1/users         | List all users      | 200 OK                |
| GET    | /api/v1/users/{id}    | Get user by ID      | 200 OK / 404 Not Found|
| POST   | /api/v1/users         | Create a new user   | 201 Created           |
| PUT    | /api/v1/users/{id}    | Update a user       | 200 OK / 404 Not Found|
| DELETE | /api/v1/users/{id}    | Delete a user       | 204 No Content        |

All responses use the `ApiResponse<T>` wrapper from common-library:

```json
{
  "success": true,
  "message": "Success",
  "data": { ... }
}
```

## Running Locally

```bash
mvn spring-boot:run
```

- Application: http://localhost:8081
- H2 Console: http://localhost:8081/h2-console
- Actuator: http://localhost:8081/actuator/health
