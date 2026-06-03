# GrocifyNear 🛒

A location-aware grocery backend that helps users find nearby shops, compare product prices across stores, and place orders with real payment integration — powered by Spring Boot 4, JWT security, Razorpay, and an AI recommendation engine.

---

## What it does

- **Find nearby shops** — user shares their location, the app returns all grocery shops within a given radius sorted by distance (Haversine formula)
- **Compare prices** — search for a product by name and see which shop sells it cheapest
- **Cart & Orders** — add products to cart, place orders with Razorpay payment, stock is decremented and shop wallets are credited automatically
- **AI Assistant** — ask natural language questions like *"which shop has the cheapest milk?"* and get an intelligent answer based on live product data
- **Role-based access** — USER, SHOP_OWNER, and ADMIN roles enforced via JWT

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.6 |
| Language | Java 17 |
| Database | MySQL + Spring Data JPA |
| Security | Spring Security + JWT (jjwt) |
| Payments | Razorpay Java SDK |
| AI | Spring AI 2.0 + Ollama (local LLM) |
| Validation | Jakarta Bean Validation |
| Build | Maven |

---

## Project Structure

```
src/main/java/Grocify/GrocifyNear/
├── configuration/       # Security config, JWT filter
├── controller/          # REST controllers
├── dto/
│   ├── RequestDto/      # Incoming request bodies
│   └── ResponseDto/     # Outgoing response shapes
├── enums/               # Role enum (USER, SHOP_OWNER, ADMIN)
├── handler/             # Global exception handler, custom exceptions
├── mapper/              # Entity ↔ DTO converters
├── model/               # JPA entities
├── repository/          # Spring Data repositories
├── security/            # JwtService, UserDetailsServiceImpl
├── service/             # Business logic (interfaces + impls)
└── util/                # DistanceUtil (Haversine)
```

---

## Getting Started

### Prerequisites

- Java 17+
- MySQL running locally (or Docker)
- Maven
- [Ollama](https://ollama.com) installed and running (`ollama serve`)
- A Razorpay test account

### Quick Start (Local)

1. **Clone the repo**
   ```bash
   git clone https://github.com/YOUR_USERNAME/GrocifyNear.git
   cd GrocifyNear
   ```

2. **Configure the app**
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
   Fill in your database credentials, Razorpay keys, and JWT secret.

3. **Pull an Ollama model**
   ```bash
   ollama pull phi3
   ```

4. **Create the database**
   ```sql
   CREATE DATABASE Grocify;
   ```

5. **Run the app**
   ```bash
   ./mvnw spring-boot:run
   ```
   App starts on `http://localhost:8080`

---

### Docker Deployment

Run the entire stack (MySQL + Ollama + Spring Boot app) with a single command:

1. **Set up environment**
   ```bash
   cp .env.example .env
   ```
   Edit `.env` and add your Razorpay keys and JWT secret.

2. **Start all services**
   ```bash
   docker-compose up --build
   ```

3. **Pull the Ollama model** (one-time, in a new terminal)
   ```bash
   docker exec grocifynear-ollama ollama pull phi3
   ```

4. **Access the app**
   - API: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

**To stop:**
```bash
docker-compose down
```

**To rebuild the image:**
```bash
docker-compose build --no-cache
```

---

## API Reference

### Auth (Public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/user/register` | Register a new user |
| POST | `/api/v1/user/login` | Login and get JWT token |

**Register body:**
```json
{
  "userName": "john",
  "email": "john@example.com",
  "password": "secret123",
  "role": "USER"
}
```
Roles: `USER`, `SHOP_OWNER`, `ADMIN`

**Login response:**
```json
{
  "message": "Login successful",
  "status": true,
  "data": "<JWT_TOKEN>"
}
```

Use the token in all subsequent requests:
```
Authorization: Bearer <JWT_TOKEN>
```

---

### Shops (Public GET / SHOP_OWNER write)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/shop` | List all shops |
| POST | `/api/v1/shop` | Add a shop 🔒 SHOP_OWNER |
| PUT | `/api/v1/shop/{id}` | Update a shop 🔒 SHOP_OWNER |
| DELETE | `/api/v1/shop/{id}` | Delete a shop 🔒 SHOP_OWNER |

**Add shop body:**
```json
{
  "shopName": "Fresh Mart",
  "address": "123 MG Road",
  "latitude": 28.6139,
  "longitude": 77.2090
}
```

---

### Products (Public GET / SHOP_OWNER write)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/product` | List all products |
| POST | `/api/v1/product/{shopId}` | Add product to shop 🔒 SHOP_OWNER |
| PUT | `/api/v1/product/{id}` | Update product 🔒 SHOP_OWNER |
| DELETE | `/api/v1/product/{id}` | Delete product 🔒 SHOP_OWNER |

---

### User Features 🔒 USER

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/user/shop/nearby?lat=&lng=&radius=` | Find shops within radius (km) |
| GET | `/api/v1/user/products/compare?item=` | Compare product prices across shops |

**Nearby shops response** — sorted by distance ascending.

---

### Cart 🔒 USER

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/cart/{userId}` | Get cart with items and total |
| POST | `/api/v1/cart/{productId}?userId=` | Add product to cart |

---

### Payment 🔒 USER

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/payment/create/order?userId=` | Create Razorpay order |
| POST | `/api/v1/payment/verify` | Verify payment signature and place order |

**Verify payment body:**
```json
{
  "userId": 1,
  "razorpayOrderId": "order_xxx",
  "razorpayPaymentId": "pay_xxx",
  "razorpaySignature": "signature_xxx"
}
```

On successful verification:
- Order is saved with payment ID
- Product stock is decremented
- Shop wallet is credited

---

### AI Assistant 🔒 Authenticated

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/ai/ask?question=` | Ask anything about products and shops |

**Example:**
```
GET /api/v1/ai/ask?question=which shop has cheapest milk
```
The AI fetches live product data from the database and answers intelligently using a local Ollama model.

---

## How Payment Works

```
User → Create Razorpay Order → Pay on frontend
     → Send payment IDs to /verify
     → Backend verifies HMAC signature
     → Order placed, stock updated, shop wallet credited
```

---

## How Nearby Search Works

The app uses the **Haversine formula** to calculate the great-circle distance between the user's coordinates and each shop's coordinates, then returns shops within the requested radius sorted by distance.

---

## Environment Variables

See `application.properties.example` for all required configuration. Never commit your real `application.properties`.

To generate a JWT secret:
```bash
openssl rand -base64 32
```
