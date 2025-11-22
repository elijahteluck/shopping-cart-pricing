Technical test for Java Senior Developer interview
=======
# Shopping Cart Pricing Service

A clean and maintainable Java 21 / Spring Boot 3 backend service implementing a pricing engine for an online product sales company. The project demonstrates a senior-level approach with a focus on clean code, domain-driven design principles, immutability, and comprehensive test coverage.

---

## Features

- Two client types supported: Individual and Professional.
- Professional pricing dynamically changes based on annual revenue.
- Three product types with predefined business rules.
- Clean separation of responsibilities using Hexagonal Architecture:
    - Domain layer with pure business logic (records, policies, domain services).
    - Application layer implementing use-case orchestration.
    - Inbound adapter exposing a REST API with transport-specific request/response models.
- No framework dependencies inside the domain and application layers.
- Unit tests validating pricing rules and integration tests covering the REST interface.

---

## Architecture Overview

The project follows principles inspired by Hexagonal Architecture and Domain-Driven Design:

- **Domain layer**: pure immutable business logic implemented using Java records.
- **Application layer**: exposes use cases and orchestrates domain operations.
- **Adapter Layer (Inbound)**: provides the external interface for the application.
- Price policies are encapsulated within the domain, reflecting business rules without framework dependencies.

This approach ensures maintainability, testability, and a clear expression of business rules.

---

## Running the Application

Start the service:

```bash
./gradlew bootRun
```

Run the test suite:
```bash
./gradlew test
```

The service will be available at:
```bash
http://localhost:8080/api/carts/total
```

## REST API

POST /api/carts/total

Request example:

```bash
{
  "client": {
    "type": "PROFESSIONAL",
    "id": "P1",
    "companyName": "Big Corp",
    "annualRevenue": 15000000
  },
  "items": [
    { "productType": "HIGH_END_PHONE", "quantity": 2 },
    { "productType": "LAPTOP", "quantity": 1 }
  ]
}
```

Response example:

```bash
{
  "total": 2900,
  "currency": "EUR"
}
```
## Error Handling Strategy

The inbound Web adapter provides a centralized exception handling mechanism implemented using `@ControllerAdvice`.

## Pricing Rules

### Individual Clients
- High-end phone: **1500 EUR**
- Mid-range phone: **800 EUR**
- Laptop: **1200 EUR**

### Professional Clients (Revenue > 10M EUR)
- High-end phone: **1000 EUR**
- Mid-range phone: **550 EUR**
- Laptop: **900 EUR**

### Professional Clients (Revenue <= 10M EUR)
- High-end phone: **1150 EUR**
- Mid-range phone: **600 EUR**
- Laptop: **1000 EUR**

---

## Build Configuration
- Java **21**
- Spring Boot **3.3**
- Gradle
- Spring Web, Validation, Spring Boot Test

---

## Notes
Persistence or messaging components are not included, as they were not required by the exercise. The focus is on domain correctness, clean architecture, and test coverage.