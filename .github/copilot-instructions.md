# Copilot Agent Instructions — service-alpha

## Repository Purpose
`service-alpha` is a Spring Boot RESTful microservice responsible for CRUD APIs in the Alpha domain.
It consumes shared utilities from `vinipx/common-library`.

## Boundaries & Responsibilities
- Owns Alpha domain REST endpoints, service layer, persistence adapters, and API contracts.
- Must NOT duplicate reusable utilities that belong in `common-library`.
- Any cross-cutting utility should be implemented in `common-library` and then consumed here.

## Tech Stack
- Java 21+
- Spring Boot 3.x
- Maven
- JUnit 5, Mockito, Testcontainers (for integration tests)

## Agent Operating Instructions
1. Read linked Jira issue context before coding.
2. Validate impact:
   - Local impact (service-alpha)
   - Shared impact (common-library)
   - Downstream impact (service-beta if shared contract changes)
3. Follow `java-coding-guidelines.md` strictly.
4. Keep changes minimal, cohesive, and traceable to acceptance criteria.
5. Generate:
   - Unit tests for all new logic
   - Integration tests for endpoint/data flow changes
6. Update API docs/OpenAPI annotations if endpoint contract changes.
7. Ensure backward compatibility unless Jira explicitly allows breaking changes.
8. Produce clear commit messages referencing Jira key.
9. Open PR with summary:
   - Business context
   - Technical changes
   - Test evidence
   - Risk/rollback notes

## Definition of Done (Agent)
- Build passes
- Tests pass
- Static checks pass
- Acceptance criteria mapped to code + tests
- PR created and ready for code owner review
