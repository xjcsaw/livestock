# Insurance WebFlux Backend

This module demonstrates the advantages of Spring WebFlux for streaming real-time insurance damage statistics.

## Features

- **Reactive Programming Model**: Uses Project Reactor's `Flux` and `Mono` types for non-blocking, reactive streams
- **Server-Sent Events (SSE)**: Streams real-time updates to clients using SSE
- **Non-blocking I/O**: Handles many concurrent connections with fewer resources
- **Backpressure Handling**: Manages flow control between producer and consumer

## Insurance Damage Statistics

The application simulates an insurance company's damage statistics system that streams:
- Number of new damage reports per hour
- Current payout amounts (total and average)
- Pending and processed claims counts

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/insurance` | GET | Get all insurance damage statistics |
| `/api/insurance/{type}` | GET | Get statistics for a specific damage type |
| `/api/insurance/stream` | GET | Stream all statistics as Server-Sent Events |
| `/api/insurance/stream/{type}` | GET | Stream statistics for a specific damage type |

## Running the Application

The WebFlux backend runs on port 8081 by default.

```bash
# From the project root
mvn spring-boot:run -pl webflux-backend
```

## Testing with cURL

```bash
# Get all statistics
curl http://localhost:8081/api/insurance

# Get statistics for a specific type
curl http://localhost:8081/api/insurance/Fire

# Stream all statistics (keep connection open)
curl -N http://localhost:8081/api/insurance/stream

# Stream statistics for a specific type
curl -N http://localhost:8081/api/insurance/stream/Fire
```

## Advantages of WebFlux Over Traditional Spring MVC

1. **Scalability**: Handles more concurrent connections with fewer threads
2. **Resource Efficiency**: Uses less memory per connection
3. **Backpressure**: Built-in mechanisms to handle fast producers and slow consumers
4. **Functional Programming Style**: Enables composable, declarative code
5. **Reactive End-to-End**: Maintains reactivity throughout the entire request processing chain

## Use Cases

Spring WebFlux is particularly well-suited for:
- Real-time dashboards and monitoring
- High-throughput data streaming
- Applications with many concurrent users
- Microservices that need to be resilient and responsive
