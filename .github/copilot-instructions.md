# Service Alpha — Copilot Agent Instructions

## Repository Purpose
Primary Spring Boot 3.x RESTful microservice exposing User CRUD APIs. Depends on `common-library` for shared DTOs, utilities, and exception handling.

## Architecture
- **Language**: Java 21
- **Framework**: Spring Boot 3.2.x
- **Build Tool**: Maven 3.9+
- **Database**: H2 (dev/test), PostgreSQL (prod)
- **Port**: 8081

## Package Structure
```
com.ecosystem.alpha.config       → Spring configuration classes
com.ecosystem.alpha.controller   → REST controllers (@RestController)
com.ecosystem.alpha.service      → Business logic (@Service)
com.ecosystem.alpha.repository   → Data access (@Repository, Spring Data JPA)
com.ecosystem.alpha.model        → JPA entity classes (@Entity)
```

## Rules for Agents
1. Follow the layered architecture strictly: Controller → Service → Repository.
2. Controllers MUST only handle HTTP concerns (validation, status codes). No business logic.
3. Services MUST be annotated with @Service and use constructor injection.
4. All endpoints MUST return ResponseEntity<T> with appropriate HTTP status codes.
5. Use common-library DTOs for API request/response — do NOT duplicate.
6. All REST endpoints MUST have integration tests using @WebMvcTest or @SpringBootTest.
7. Exception handling via @ControllerAdvice using common-library exception hierarchy.
8. Minimum 80% test coverage.
