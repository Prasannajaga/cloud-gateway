# Cloud Gateway

This project implements a Cloud Gateway using Spring Cloud Gateway, integrated with Eureka for service discovery and Resilience4j for fault tolerance. It serves as the entry point for client requests in a microservices architecture, providing features like retry mechanisms, rate limiting, and circuit breakers to enhance reliability and manage load effectively.

## Features

- **Service Discovery**: Utilizes Eureka for discovering and routing to microservices.
- **Retry Mechanism**: Automatically retries failed requests to improve success rates.
- **Rate Limiting**: Controls the number of requests to prevent overloading services.
- **Circuit Breaker**: Prevents cascading failures by halting requests to unhealthy services.
- **Spring Cloud Gateway**: Routes requests based on defined rules and filters.

## Prerequisites

- Java 17 or later
- Maven for dependency management
- Spring Boot 3.x
- A running downstream service for testing (e.g., a simple REST API)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Prasannajaga/cloud-gateway.git
   ```
2. Navigate to the project directory:
   ```bash
   cd cloud-gateway
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Configuration

The project is configured via the `application.yml` file. Key configurations include:

- **Redis**: Host and port for rate limiting (default: `localhost:6379`).
- **Circuit Breaker**: Settings like sliding window size (`5`), failure rate threshold (`50%`), wait duration in open state (`10s`), and permitted calls in half-open state (`3`).
- **Rate Limiter**: Replenish rate (`10`), burst capacity (`20`), and requested tokens (`1`).
- **Retry**: Number of retries (`3`), first backoff (`50ms`), and max backoff (`500ms`).
- **Routes**: Definitions for routing to different services.
- **Server Port**: The gateway runs on port `8090`.

For detailed configuration, refer to the `application.yml` file in the project.

## Resilience Features

### Retry

- **Configuration**: Up to 3 retries with exponential backoff starting at 50ms.
- **Purpose**: Automatically retries failed requests to handle transient failures.

### Rate Limiter

- **Configuration**: Allows 10 requests per second with a burst capacity of 20.
- **Purpose**: Prevents system overload by limiting the request rate.

### Circuit Breaker

- **Configuration**: Opens if 50% of the last 5 requests fail, waits 10 seconds before attempting recovery.
- **Purpose**: Protects the system by stopping requests to failing services and providing fallback responses.

## Usage

1. Ensure a downstream service is running (e.g., a REST API).
2. Access the gateway by sending requests to `http://localhost:8090/api/endpoint`.
3. In case of failures, the gateway will attempt retries, apply rate limiting, or trigger circuit breakers as configured.

## Testing

### Retry Testing

- Simulate a failure in the downstream service and observe the gateway retrying the request up to 3 times.

### Rate Limiter Testing

- Send more than 10 requests per second to see the rate limiter in action, returning 429 Too Many Requests.

### Circuit Breaker Testing

- Cause repeated failures in the downstream service to trigger the circuit breaker, which will then route to the fallback endpoint.

## Dependencies

Key dependencies include:

- `spring-cloud-starter-gateway`
- `resilience4j spring-boot3`
- `spring-boot-starter-data-redis`

These are managed via Maven in the `pom.xml` file.