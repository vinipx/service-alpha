# Java Coding Guidelines (Agent-Enforced)

## 1. Language & Platform
- Java 21+ required.
- Spring Boot 3.x conventions.
- Prefer immutable data structures and constructor injection.
- Enable `-parameters` compiler flag where applicable.

## 2. Architectural Principles
- Follow clean layering:
  - Controller (API)
  - Service (business logic)
  - Repository/Adapter (persistence/external)
- No business logic in controllers.
- Shared cross-cutting logic belongs in `common-library`.
- Favor composition over inheritance.

## 3. API Design
- Use resource-oriented REST conventions.
- Use DTOs for API boundaries; never expose JPA entities directly.
- Validate input with Jakarta Validation annotations.
- Return meaningful HTTP status codes and structured error payloads.
- Maintain backward compatibility unless explicitly approved.

## 4. Naming & Code Style
- Classes: `PascalCase`
- Methods/variables: `camelCase`
- Constants: `UPPER_SNAKE_CASE`
- Package names: lowercase, domain-oriented.
- Method size: keep focused and small.
- Avoid deep nesting; prefer guard clauses.

## 5. Error Handling
- Use domain-specific exceptions.
- Centralize error mapping via `@ControllerAdvice`.
- Never swallow exceptions silently.
- Include actionable error messages without leaking secrets.

## 6. Logging & Observability
- Use structured, parameterized logs.
- Never log credentials, tokens, or sensitive PII.
- Include correlation/request IDs when available.
- Log at proper levels (`INFO`, `WARN`, `ERROR`, `DEBUG`).

## 7. Security
- Validate and sanitize all external inputs.
- Principle of least privilege for integrations.
- Keep secrets in environment/secret manager, never in source code.
- Ensure dependency vulnerability scanning is enabled.

## 8. Testing Standards
### Unit Tests
- Framework: JUnit 5 + Mockito.
- Test behavior, not implementation details.
- Naming pattern: `methodName_condition_expectedResult`.
- Cover positive, negative, and edge cases.

### Integration Tests
- Use Spring Boot test slices and full-context tests only where needed.
- Prefer Testcontainers for real dependency fidelity (DB, message broker).
- Verify endpoint contracts, serialization, and persistence flows.

### Coverage & Quality Gates
- Minimum line coverage: 80% (higher for shared library critical modules).
- Critical paths require explicit edge-case tests.
- CI must fail on test failures and static analysis violations.

## 9. Dependency & Versioning
- Pin dependency versions intentionally.
- Avoid unnecessary libraries.
- For `common-library`, follow semantic versioning:
  - MAJOR: breaking API changes
  - MINOR: backward-compatible features
  - PATCH: fixes

## 10. Pull Request Standards
Each PR must include:
1. Jira link/key
2. Problem statement
3. Architectural decision summary
4. Files changed grouped by concern
5. Test evidence (unit/integration)
6. Backward compatibility note
7. Rollback strategy (if production-impacting)

## 11. Agent-Specific Rules
- Agents must read `.github/copilot-instructions.md` before code generation.
- Agents must map each acceptance criterion to code/test artifacts.
- Agents must run/verify formatting, tests, and static checks before PR.
- Agents must keep change sets minimal and deterministic.
