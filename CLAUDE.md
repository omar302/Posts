# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot 3.5.13 web application (`dev.ot:claudecode`) built with Java 25 and Maven.

## Commands

```bash
# Run the application
mvnw.cmd spring-boot:run

# Run all tests
mvnw.cmd test

# Run a single test class
mvnw.cmd test -Dtest=ClaudecodeApplicationTests

# Build executable JAR
mvnw.cmd clean package

# Build Docker/OCI image
mvnw.cmd spring-boot:build-image
```

## Architecture

This is a starter Spring Boot application with no business logic yet implemented. The base package is `dev.ot.claudecode`.

**Included starters:**
- `spring-boot-starter-web` — Spring MVC / REST API support
- `spring-boot-starter-cache` — Spring Cache abstraction (not yet configured)

**Key locations:**
- Main class: `src/main/java/dev/ot/claudecode/ClaudecodeApplication.java`
- Config: `src/main/resources/application.properties`
- Static assets: `src/main/resources/static/`
- Templates: `src/main/resources/templates/`
- Tests: `src/test/java/dev/ot/claudecode/`


## Project Structure

* **Packaging:** Strongly prefer a **package-by-feature** structure over package-by-layer. This means grouping all code related to a specific feature or domain concept (like "posts", "users", or "orders") together in the same package hierarchy. Avoid structuring packages based solely on technical layers (like "controllers", "services", "repositories").

  * **Why Package-by-Feature?** It improves modularity, makes navigating code related to a single feature easier, reduces coupling between features, and simplifies refactoring or potentially extracting the feature into a microservice later.

  * **Example:**

    **PREFER THIS (Package-by-Feature):**
      ```
      com.example.application
      ├── posts                     # Feature: Posts
      │   ├── PostController.java   # Controller for Posts
      │   ├── PostService.java      # Service logic for Posts
      │   ├── PostRepository.java   # Data access for Posts
      │   ├── Post.java             # Domain/Entity for Posts
      │   └── dto                   # Data Transfer Objects specific to Posts
      │       ├── PostCreateRequest.java
      │       └── PostSummaryResponse.java
      │
      ├── users                     # Feature: Users
      │   ├── UserController.java
      │   ├── UserService.java
      │   ├── UserRepository.java
      │   └── User.java
      │
      └── common                    # Optional: Truly shared utilities/config
          └── exception
              └── ResourceNotFoundException.java
      ```

    **AVOID THIS (Package-by-Layer):**
      ```
      com.example.application
      ├── controller
      │   ├── PostController.java
      │   └── UserController.java
      │
      ├── service
      │   ├── PostService.java
      │   └── UserService.java
      │
      ├── repository
      │   ├── PostRepository.java
      │   └── UserRepository.java
      │
      └── model  (or domain/entity)
          ├── Post.java
          └── User.java
      ```


## Data Access

* **No Persistence Required:** For applications that don't need data persistence across restarts (prototypes, demos, simple services with temporary data), use **in-memory data structures** like `Map`, `List`, or `Set` within your service classes. Don't introduce database complexity unless you specifically need persistent storage.

* **Simple Applications/Queries:** For applications primarily dealing with straightforward, single-table CRUD operations or when direct SQL control is beneficial *without complex object mapping*, prefer using the Spring Framework **`JdbcClient`**. Avoid the older `JdbcTemplate`.

* **Standard/Complex Applications:** For applications with domain models involving relationships, complex queries, or where JPA features (caching, dirty checking, repository abstractions) provide significant benefits, use **Spring Data JPA**.

* **Default Decision Tree:**
  1. If data doesn't need to persist across application restarts → Use in-memory collections
  2. If unsure about persistence needs → Start with in-memory, migrate to database when needed
  3. If persistence is required but queries are simple → Use `JdbcClient`
  4. If persistence is required with complex domain models → Use Spring Data JPA

* **In-Memory Best Practices:**
  - Use `ConcurrentHashMap` instead of `HashMap` for thread-safe operations in multi-threaded environments
  - Consider using `@PostConstruct` to initialize sample data for development/testing
  - Document clearly that data is not persistent to avoid confusion
  - For more sophisticated in-memory needs, consider using an embedded H2 database in memory mode (`jdbc:h2:mem:testdb`)

## HTTP Clients

* **Outgoing HTTP Requests:** Use the Spring Framework 6+ **`RestClient`** for making synchronous or asynchronous HTTP calls. Avoid using the legacy `RestTemplate` in new code.

## Java Language Features

* **Data Carriers:** Use Java **Records** (`record`) for immutable data transfer objects (DTOs), value objects, or simple data aggregates whenever possible. Prefer records over traditional classes with getters, setters, `equals()`, `hashCode()`, and `toString()` for these use cases.
* **Immutability:** Favor immutability for objects where appropriate, especially for DTOs and configuration properties.

## Spring Framework Best Practices

* **Dependency Injection:** Use **constructor injection** for mandatory dependencies. Avoid field injection.
* **Configuration:** Use `application.properties` or `application.yml` for application configuration. Leverage Spring Boot's externalized configuration mechanisms (profiles, environment variables, etc.). Use `@ConfigurationProperties` for type-safe configuration binding.
* **Error Handling:** Implement consistent exception handling, potentially using `@ControllerAdvice` and custom exception classes. Provide meaningful error responses.
* **Logging:** Use SLF4j with a suitable backend (like Logback, included by default in Spring Boot starters) for logging. Write clear and informative log messages.

## Testing

* **Unit Tests:** Write unit tests for services and components using JUnit 5 and Mockito.
* **Integration Tests:** Write integration tests using `@SpringBootTest`. For database interactions, consider using Testcontainers or an in-memory database (like H2) configured only for the test profile. Ensure integration tests cover the controller layer and key application flows.
* **Test Location:** Place tests in the standard `src/test/java` directory, mirroring the source package structure.

## General Code Quality

* **Readability:** Write clean, readable, and maintainable code.
* **Comments:** Add comments only where necessary to explain complex logic or non-obvious decisions. Code should be self-documenting where possible.
* **API Design:** Design RESTful APIs with clear resource naming, proper HTTP methods, and consistent request/response formats.