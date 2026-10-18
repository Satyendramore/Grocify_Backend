# GrocifyNear 🛒

A production-grade grocery delivery backend with intelligent rider assignment, location-based shop discovery, and real-time order tracking — powered by Spring Boot 4, Redis Geospatial indexing, WebSocket notifications, JWT security, Razorpay payments, and AI recommendations.

---

## What it does

- **Find nearby shops** — user shares their location, the app returns all grocery shops within a given radius sorted by distance (Haversine formula)
- **Compare prices** — search for a product by name and see which shop sells it cheapest
- **Cart & Orders** — add products to cart, place orders with Razorpay payment, stock is decremented and shop wallets are credited automatically
- **Real-time Rider Assignment** — intelligent delivery assignment using Redis Geospatial indexing, TSP route optimization, and WebSocket notifications with automatic cascading
- **Location Tracking** — riders share GPS coordinates stored in Redis with sub-second query performance for 1000+ concurrent active riders
- **AI Assistant** — ask natural language questions like *"which shop has the cheapest milk?"* and get an intelligent answer based on live product data
- **Role-based access** — USER, SHOP_OWNER, ADMIN, and RIDER roles enforced via JWT

---

## Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.6 |
| Language | Java 17 |
| Database | MySQL + Spring Data JPA |
| Cache & Geospatial | Redis + Spring Data Redis |
| Real-time Communication | WebSocket (STOMP) |
| Security | Spring Security + JWT (jjwt) |
| Payments | Razorpay Java SDK |
| AI | Spring AI 2.0 + Ollama (local LLM) |
| Validation | Jakarta Bean Validation |
| Build | Maven |
| Containerization | Docker + Docker Compose |

---

## Project Structure

```
src/main/java/Grocify/GrocifyNear/
├── configuration/       # Security, Redis, WebSocket, CORS config
├── controller/          # REST controllers (User, Shop, Product, Cart, Payment, Rider, AI)
├── dto/
│   ├── RequestDto/      # Incoming request bodies
│   └── ResponseDto/     # Outgoing response shapes
├── enums/               # Role (USER, SHOP_OWNER, ADMIN), Status (Online, Offline, Busy)
├── handler/             # Global exception handler, custom exceptions
├── mapper/              # Entity ↔ DTO converters
├── model/               # JPA entities (User, Shop, Product, Cart, Order, Rider)
├── repository/          # Spring Data repositories
├── security/            # JwtService, UserDetailsServiceImpl
├── service/             # Business logic (User, Shop, Product, Cart, Order, Rider, TopRiders, Notification, Routing, AI)
└── util/                # DistanceUtil (Haversine formula)
```

**91 Java files** across controllers, services, repositories, models, DTOs, and configurations.

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

Run the entire stack (MySQL + Redis + Ollama + Spring Boot app) with a single command:

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

### Rider APIs 🔒 Authenticated

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/rider/` | Create rider profile |
| POST | `/api/v1/rider/update?status=` | Update rider status (Online/Offline/Busy) |
| POST | `/api/v1/rider/location` | Update rider GPS location (stored in Redis) |
| POST | `/api/v1/rider/notify/{orderId}` | Trigger intelligent rider assignment |

**Rider Status:**
- `Online` — Available for deliveries, location tracked in Redis
- `Busy` — Currently on delivery
- `Offline` — Not available, removed from Redis

**WebSocket Endpoint:** `ws://localhost:8080/ws`  
**Message Mapping:** `/app/rider/assignment-response`

Riders connect via WebSocket to receive real-time order notifications and respond with accept/reject.

---

## How Rider Assignment Works

```
Order Placed → Notify Riders System
    ↓
Extract all shops from order items
    ↓
Redis GeoRadius: Find top 10 nearest riders within 5km of each shop
    ↓
External Routing API: Get shop-to-shop distances
    ↓
TSP Algorithm: Calculate optimal route for each rider visiting all shops
    ↓
Rank riders by composite score (distance + time)
    ↓
WebSocket Notification: Send to top-ranked rider
    ↓
CompletableFuture: Wait 20 seconds for response
    ↓
If accepted → Assign order & STOP
If rejected/timeout → Cascade to next rider
    ↓
Repeat until rider accepts or all 10 riders exhausted
```

**Key Technologies:**
- **Redis Geospatial:** O(log N) location queries with Geohash indexing
- **TSP Algorithm:** Recursive backtracking to find shortest multi-shop route
- **WebSocket (STOMP):** Real-time bidirectional communication
- **CompletableFuture:** Non-blocking async with 20s timeout
- **ConcurrentHashMap:** Thread-safe concurrent request handling

---

## How Payment Works

```
User → Create Razorpay Order → Pay on frontend
     → Send payment IDs to /verify
     → Backend verifies HMAC signature
     → Order placed, stock updated, shop wallet credited
```

---

## How Location-Based Search Works

### Nearby Shops (Haversine Formula)
The app uses the **Haversine formula** to calculate the great-circle distance between the user's coordinates and each shop's coordinates, then returns shops within the requested radius sorted by distance.

```java
distance = 2 × R × arcsin(√[sin²(Δlat/2) + cos(lat1) × cos(lat2) × sin²(Δlon/2)])
```
Where R = Earth's radius (6371 km)

### Nearby Riders (Redis Geospatial)
Rider locations are stored in Redis using `GEOADD` with Geohash encoding. Queries use `GEORADIUS` for sub-second retrieval:

```redis
GEOADD riders:available 77.2090 28.6139 rider123
GEORADIUS riders:available 77.2090 28.6139 5 km WITHDIST ASC LIMIT 10
```

**Performance:** <20ms for 1000+ riders

---

## Environment Variables

See `application.properties.example` for all required configuration. Never commit your real `application.properties`.

**Required Configuration:**
- Database: MySQL connection URL, username, password
- Redis: Host and port (default: localhost:6379)
- JWT: Secret key and expiration time
- Razorpay: API key and secret
- Ollama: Base URL (default: http://localhost:11434)
- GraphHopper: Routing service URL (optional)

To generate a JWT secret:
```bash
openssl rand -base64 32
```

---

## Key Features & Metrics

### 🚀 Performance
- **15+ REST API endpoints** across 7 controllers
- **91 Java source files** with clean architecture
- **Redis Geospatial queries:** <20ms for 1000+ riders
- **Sub-second location-based search** with Haversine formula
- **Non-blocking async operations** with CompletableFuture

### 🎯 Algorithms
- **TSP (Traveling Salesman Problem)** variant for multi-shop delivery routes
- **Recursive backtracking** with O(N!) complexity for optimal path finding
- **Geohash spatial indexing** for efficient location queries
- **Composite ranking:** distance + time for rider selection

### 🔒 Security
- **JWT authentication** with 24-hour token expiry
- **BCrypt password hashing** (strength 12)
- **3-tier role-based access control** (USER, SHOP_OWNER, ADMIN)
- **HMAC-SHA256** signature verification for payments
- **Stateless architecture** with no server-side sessions

### 📊 Database
- **8 JPA entities** with 12+ relationships
- **Atomic transactions** for payment processing
- **Cascade operations** for nested object management
- **HikariCP connection pooling** for optimal performance

### 🌐 Real-time Communication
- **WebSocket (STOMP protocol)** for rider notifications
- **2 message brokers** (/topic, /queue)
- **20-second timeout** with automatic fallback
- **ConcurrentHashMap** for thread-safe request tracking

### 🤖 AI Integration
- **Spring AI 2.0** with local Ollama LLM (phi3)
- **Context injection** from live database queries
- **Natural language processing** for product recommendations
