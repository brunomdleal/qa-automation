# QA Automation Framework (ATF)

A modern, standalone Automated Test Framework (ATF) built in Java 21 to provide automated API contract, regression, and functional end-to-end verification across the portfolio microservices ecosystem.

---

## Architecture & Tech Stack

| Component             | Technology            | Purpose                                               |
|:----------------------|:----------------------|:------------------------------------------------------|
| **Language**          | Java 21 (Temurin)     | Modern LTS runtime matching backend microservices     |
| **Build System**      | Maven (`mvnw`)        | Dependency management and test execution harness      |
| **API Testing**       | REST-Assured          | Fluent REST API assertion and HTTP payload validation |
| **Runner / Engine**   | JUnit 5 (Jupiter)     | Test lifecycle, assertions, and parameterized tests   |
| **Schema Validation** | JSON Schema Validator | Strict contract validation against API schemas        |

---

## Target Services

The ATF executes contract and functional regression suites against running instances of:
* **`order-service`**: Order creation, status queries, pagination, and health actuator validation.
* **`platform-delivery-engine`**: Local Docker Compose stack and Kubernetes ingress endpoints.

---

## Getting Started

### Prerequisites
* Java 21+
* Docker / Docker Compose (for target services)

### Configuration
Set the target service endpoint via environment variable or system property:

```bash
export BASE_URL=http://localhost:8080
```

Running Tests
Execute the full automated test suite:

```bash
./mvnw clean test
```

Execute a specific test class or tag:

```bash
./mvnw test -Dtest=OrderApiRegressionTest
```

## Test Scenarios & Coverage
- Happy Path: End-to-end entity lifecycles (POST / GET / LIST).
- Schema Validation: Conformance checking of JSON payloads against schema definitions.
- Error Gating: Validating 400 Bad Request, 404 Not Found, and standard error envelopes.
- Platform Health: Verifying `/actuator/health` liveness and readiness states.
