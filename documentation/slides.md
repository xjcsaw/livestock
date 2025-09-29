---
theme: default
background: https://source.unsplash.com/collection/94734566/1920x1080
class: text-center
highlighter: shiki
lineNumbers: false
info: |
  ## Livestock Project Documentation
  
  Presentation slides for the Livestock project.
drawings:
  persist: false
transition: slide-left
title: Livestock Project Documentation
---

# Livestock Project

A real-time stock tracking application

---

# Project Structure

The project is organized into multiple modules:

- **Backend**: Java Spring Boot application
- **Frontend**: Angular application
- **Documentation**: This presentation (Slidev)

---

# Backend

- Built with Spring Boot
- Provides real-time stock data via Server-Sent Events (SSE)
- RESTful API endpoints for stock data

```java
@RestController
@RequestMapping("/api/stocks")
public class StockController {
    @GetMapping(path = "/stream")
    public SseEmitter streamStockPrices() {
        // Implementation for real-time stock updates
    }
}
```

---

# Frontend

- Built with Angular
- Real-time stock price updates
- Custom Web Components using Lit

```typescript
// Example of a component
@Component({
  selector: 'app-stock-table',
  templateUrl: './stock-table.component.html'
})
export class StockTableComponent {
  // Implementation
}
```

---

# Stock Model

The core data model represents stock information:

```java
public class Stock {
    private String symbol;
    private BigDecimal price;
    private BigDecimal priceChange;
    private BigDecimal priceChangePercent;
    private BigDecimal high24h;
    private BigDecimal low24h;
    private BigDecimal volume;
    private LocalDateTime lastUpdate;
    
    // Getters and setters
}
```

---

# Getting Started

1. Clone the repository
2. Start the backend: `cd backend && ./mvnw spring-boot:run`
3. Start the frontend: `cd frontend && npm start`
4. View the documentation: `cd documentation && npm run dev`

---

# Thank You!

[GitHub Repository](https://github.com/yourusername/livestock)
