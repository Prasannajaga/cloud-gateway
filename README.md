# Cloud Gateway with Resilience4j

## Overview

This project implements a Cloud Gateway using **Spring Cloud Gateway** with resilience patterns such as **Retry**, **Rate Limiter**, and **Circuit Breaker** powered by **Resilience4j**. The gateway acts as an entry point for client requests, routing them to downstream services while providing fault tolerance and load management.

## Features

- **Retry:** Automatically retries failed requests to downstream services.
- **Rate Limiter:** Limits the number of requests to prevent overloading downstream services.
- **Circuit Breaker:** Prevents cascading failures by temporarily halting requests to unhealthy services.
- **Spring Cloud Gateway:** Routes incoming requests to appropriate services based on defined rules.

## Prerequisites

- Java 17 or later
- Maven (for dependency management)
- Spring Boot (version 3.x)
- A running downstream service for testing (e.g., a simple REST API)

## Installation

Clone the repository:
```sh
git clone https://github.com/your-repo/cloud-gateway.git
cd cloud-gateway
```

Build the project:
```sh
mvn clean install
```

Run the application:
```sh
mvn spring-boot:run
```

## Configuration

The gateway is configured in `application.yml`. Below is an example configuration:

```yaml
 spring:

  redis:
    host: localhost
    port: 6379

  application:
    name: gateway-service

  cloud:
    gateway:
      default-filters:
        - name: CircuitBreaker
          args:
            name: userServiceCircuitBreaker
            fallbackUri: forward:/fallback/users
        - name: RequestRateLimiter
          args:
            key-resolver: "#{@ipKeyResolver}"
            redis-rate-limiter:
              replenishRate: 10
              burstCapacity: 20
              requestedTokens: 1
        - name: Retry
          args:
            retries: 3
            backoff:
              firstBackoff: 50ms
              maxBackoff: 500ms

      routes:

#        user routes
        - id: user-routes
          uri: lb://USER-SERVICE
          predicates:
            - Path=/user/**
          filters:
            - AddResponseHeader=X-genre,Goku
#        express routes
        - id: express-routes
          uri: lb://EXPRESS-SERVICE
          predicates:
            - Path=/api/**
          filters:
            - AddResponseHeader=x-type,cloud

#         fast api routes
        - id: fastapi-service-route
          uri: lb://FASTAPI-SERVICE
          predicates:
            - Path=/notifications/**,/chat/**
          filters:
            - AddResponseHeader=x-type,cloud

#        post routes
        - id: post-routes
          uri: lb://POST-SERVICE
          predicates: Path=/post/**
          filters:
            - AddResponseHeader=x-type,json

      discovery:
        locator:
          enabled: true
          lower-case-service-id: true

server:
  port: 8090

logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    org.springframework.data.redis: DEBUG
    io.lettuce.core: DEBUG


resilience4j:
  circuitbreaker:
    instances:
      userServiceCircuitBreaker:
        registerHealthIndicator: true
        slidingWindowSize: 5
        failureRateThreshold: 50
        waitDurationInOpenState: 10s
        permittedNumberOfCallsInHalfOpenState: 3

```

## Explanation of Resilience Features

- **Retry:** Configured to retry up to 3 times for failed GET/POST requests with an exponential backoff starting at 100ms.
- **Rate Limiter:** Limits to 10 requests per second with a burst capacity of 20, using Redis for distributed rate limiting.
- **Circuit Breaker:** Opens the circuit if 50% of the last 10 requests fail, waits 10 seconds before transitioning to half-open.

## Usage

1. Start the downstream service (e.g., a REST API at `http://localhost:8081`).
2. Access the gateway:
   - Send requests to `http://localhost:8080/api/endpoint`.
   - The gateway will route requests to the downstream service, applying retry, rate limiting, and circuit breaker logic.

**Fallback endpoint:**  
If the circuit breaker opens or a request fails, the gateway redirects to `/fallback/users`, which can be customized.

## Testing

- **Retry:** Simulate a downstream service failure (e.g., stop the service) and send a request to `/api/endpoint`. The gateway will retry 3 times.
- **Rate Limiter:** Send rapid requests to `/api/endpoint`. After exceeding 10 requests/sec, the gateway will return a `429 Too Many Requests` status.
- **Circuit Breaker:** Cause repeated failures (e.g., by targeting an unavailable service). After the failure threshold, the circuit breaker will open, and requests will route to the fallback.

## Dependencies

- `spring-cloud-starter-gateway`
- `resilience4j-spring-boot3`
- `spring-boot-starter-data-redis` (for distributed rate limiting)

Add these to your `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
        <groupId>io.github.resilience4j</groupId>
        <artifactId>resilience4j-spring-boot3</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency> 
</dependencies>
```
<!-- 
## Monitoring

Enable Actuator endpoints to monitor Resilience4j metrics:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,circuitbreakers
``` -->

<!-- Access metrics at [http://localhost:8080/actuator/circuitbreakers](http://localhost:8080/actuator/circuitbreakers). -->

<!-- ## Contributing

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request. -->

<!-- ## License

This project is licensed under the MIT License. -->
