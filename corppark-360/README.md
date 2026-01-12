# CorpPark 360 — Intelligent Employee Parking & Navigation System

Minimal MVP monorepo with Angular frontend, Spring Boot backend, and MySQL 8.

## Prerequisites

- Node.js 20+
- npm 9+
- Java 17
- Maven 3.9+
- MySQL 8

## Repository Structure

```
corppark-360/
  backend/
  frontend/
  db/
    schema.sql
    seed.sql
  README.md
```

## Environment Variables

Backend (Spring Boot) reads:

- `DB_URL` (default: `jdbc:mysql://localhost:3306/corppark360?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (default: `password`)
- `PORT` (default: `8080`)

Frontend expects API base at `http://localhost:8080`.

## Run MySQL

```bash
mysql -u root -p
CREATE DATABASE corppark360;
USE corppark360;
SOURCE db/schema.sql;
SOURCE db/seed.sql;
```

## Run Backend

```bash
cd backend
mvn spring-boot:run
```

## Run Frontend

```bash
cd frontend
npm install
npm start
```

Then open `http://localhost:4200`.

## Sample Test Flows

### Curl API flow

```bash
curl -X POST http://localhost:8080/api/entry \
  -H "Content-Type: application/json" \
  -d '{"empId":"E001"}'
```

```bash
curl "http://localhost:8080/api/mycar?empId=E001"
```

```bash
curl -X POST http://localhost:8080/api/mycar/pin \
  -H "Content-Type: application/json" \
  -d '{"empId":"E001","pillarId":"P-12"}'
```

```bash
curl -X POST http://localhost:8080/api/exit \
  -H "Content-Type: application/json" \
  -d '{"empId":"E001"}'
```

Visitor invite:

```bash
curl -X POST http://localhost:8080/api/visitor/invite \
  -H "Content-Type: application/json" \
  -d '{"hostEmpId":"E001","guestName":"Arun","guestEmail":"a@x.com"}'
```

Visitor entry:

```bash
curl -X POST http://localhost:8080/api/visitor/entry \
  -H "Content-Type: application/json" \
  -d '{"qrToken":"<token from invite>"}'
```

Admin violations:

```bash
curl -X POST http://localhost:8080/api/admin/violations \
  -H "X-EMP-ID: E003" \
  -H "Content-Type: application/json" \
  -d '{"empId":"E005","slotId":12,"reason":"Bike in car spot"}'
```

```bash
curl -H "X-EMP-ID: E003" http://localhost:8080/api/admin/violations
```

### UI flow

1. Login with `E001`.
2. Click **Enter / Allocate** to get a slot.
3. Click **Set My Spot** and enter `P-12`.
4. Open **Live Lot** to view slot statuses.
5. For visitor invites, open **Visitor Invite** and generate a token.
6. Admin users (`E003`, `E008`) can open **Admin** to log violations.
