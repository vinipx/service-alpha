# service-alpha

Primary Spring Boot 3.x RESTful microservice exposing User CRUD APIs. Depends on `common-library` for shared DTOs, utilities, and exception handling.

## Prerequisites

- Java 21
- Maven 3.9+
- `common-library` installed locally (`mvn install` in the common-library repo)

## Build and Run

```bash
# Build and run all tests
mvn clean verify

# Run the application
mvn spring-boot:run
```

The service starts on port **8081**.

## API Endpoints

| Method | Path | Description | Response |
|--------|------|-------------|----------|
| GET | `/api/v1/users` | List all users | 200 `ApiResponse<List<UserDto>>` |
| GET | `/api/v1/users/{id}` | Get user by ID | 200 `ApiResponse<UserDto>` / 404 |
| POST | `/api/v1/users` | Create a user | 201 `ApiResponse<UserDto>` / 409 |
| PUT | `/api/v1/users/{id}` | Update a user | 200 `ApiResponse<UserDto>` / 404 |
| DELETE | `/api/v1/users/{id}` | Delete a user | 204 No Content / 404 |

## Architecture Overview

```
UserController  →  UserService  →  UserRepository  →  H2 (in-memory)
      ↓
GlobalExceptionHandler (ControllerAdvice)
      ↓
common-library (ApiResponse, UserDto, exceptions)
```