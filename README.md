# 📦 OrderUp - Safe Ordering System

OrderUp is a **Spring Boot REST API** that processes product orders safely and consistently.  
It ensures **thread-safety** while updating stock and provides **centralized exception handling** for predictable error responses.  
The project includes **unit & integration tests** with **JaCoCo coverage reports** (80%+ on business logic and exception handling).

---

## 🚀 Features
- ✅ Place an order for a product.
- ✅ Thread-safe stock updates (prevents overselling).
- ✅ Centralized error handling with clean JSON responses.
- ✅ DTOs for clean separation between persistence and API.
- ✅ Unit tests, integration tests, and JaCoCo coverage.

---

## 🏗 Design Overview

### 1. **Controller Layer**
- `OrderController` handles incoming HTTP requests.
- Accepts `OrderRequest` JSON, delegates to `OrderService`.
- Returns `OrderResponse` DTO.

### 2. **Service Layer**
- `OrderService` contains business logic:
    - Check product existence.
    - Verify stock availability.
    - Deduct stock safely.
- **Thread-Safety Choice:**  
  Stock deduction is wrapped in a `synchronized` block (or DB transaction if persistence layer is used).  
  This ensures two threads cannot reduce stock simultaneously, preventing **overselling**.

### 3. **Entity & DTO**
- **Entity:** Represents data stored in DB (e.g., `Product`).
- **DTOs:**
    - `OrderRequest` → client input (`productId`, `quantity`).
    - `OrderResponse` → server output (`orderId`, `status`, etc.).

### 4. **Global Exception Handling**
- `GlobalExceptionHandler` is annotated with `@RestControllerAdvice`.
- Uses **Spring AOP** under the hood to intercept exceptions globally.
- Maps exceptions to HTTP responses:
    - `NotFoundException` → `404 Not Found`.
    - `InsufficientStockException` → `400 Bad Request`.
    - Any other `Exception` → `500 Internal Server Error`.

---

## 🔄 Flow of an Order

```plaintext
Client → OrderController → OrderService → Product DB → Response
```

1. Client sends:
   ```http
   POST /orders
   Content-Type: application/json

   {
     "productId": 1,
     "quantity": 2
   }
   ```

2. `OrderController` receives request → calls `OrderService`.

3. `OrderService`:
    - Looks up product.
    - Validates stock.
    - Deducts stock safely.
    - Returns `OrderResponse`.

4. Response back to client:
   ```json
   {
     "orderId": 101,
     "productId": 1,
     "quantity": 2,
     "status": "CONFIRMED"
   }
   ```

5. If error occurs, `GlobalExceptionHandler` sends structured JSON:
   ```json
   {
     "timestamp": "2025-09-14T10:30:45Z",
     "status": 404,
     "error": "Not Found",
     "message": "Product missing"
   }
   ```

---

## 🧪 Testing

### ✅ Unit Tests
- Cover business logic (`OrderService`).
- Cover exception handling (`GlobalExceptionHandler`).

### ✅ Integration Tests
- Use **MockMvc** to simulate HTTP requests.
- Verify successful orders and error cases.

### ✅ JaCoCo Coverage
Run:
```bash
  mvn clean test
  mvn jacoco:report
```
Open coverage report at:
```
target/site/jacoco/index.html
```

Expected: **80%+ coverage** for `OrderService` and `GlobalExceptionHandler`.

---

## 📌 How to Run

1. Build and run:
   ```bash
   mvn spring-boot:run
   ```

2. API is available at:
   ```
   http://localhost:8080/orders
   ```

---

## 🔥 How to Test with curl

### ✅ Successful Order
```bash
  curl -X POST http://localhost:8080/orders   -H "Content-Type: application/json"   -d '{"productId": 1, "quantity": 2}'
```

**Response:**
```json
{
  "orderId": 101,
  "productId": 1,
  "quantity": 2,
  "status": "CONFIRMED"
}
```

---

### ❌ Product Not Found
```bash
  curl -X POST http://localhost:8080/orders   -H "Content-Type: application/json"   -d '{"productId": 999, "quantity": 1}'
```

**Response:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Product missing"
}
```

---

### ❌ Insufficient Stock
```bash
  curl -X POST http://localhost:8080/orders   -H "Content-Type: application/json"   -d '{"productId": 1, "quantity": 1000}'
```

**Response:**
```json
{
  "status": 400,
  "error": "Insufficient Stock",
  "message": "Out of stock"
}
```

---

## 🛠 Technologies
- Java 17+
- Spring Boot 3.x
- Spring Web / REST
- JUnit 5 & MockMvc
- Mockito
- JaCoCo (coverage)

---

## 📊 Summary
OrderUp provides:
- A clean separation of concerns (Controller, Service, Exception Handling).
- Safe stock updates with **thread-safety mechanisms**.
- Centralized, AOP-powered exception handling.
- Strong test coverage with JaCoCo.

