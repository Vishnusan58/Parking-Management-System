You are Codex. Generate a complete, runnable monorepo for:

Project: CorpPark 360 — Intelligent Employee Parking & Navigation System
Stack: Angular (latest stable) + Spring Boot (Java 17) + MySQL 8
Goal: Minimalist MVP, clean code, no overengineering, but race-condition-safe slot allocation.

========================
1) REPO STRUCTURE
========================
Create this structure:

corppark-360/
  backend/   (Spring Boot)
  frontend/  (Angular)
  db/
    schema.sql
    seed.sql
  README.md

README must include:
- prerequisites
- how to run MySQL, backend, frontend
- environment variables
- sample test flows (curl + UI steps)

========================
2) DATABASE (MySQL)
========================
Provide db/schema.sql and db/seed.sql.

Tables (minimal but complete):
- employees(
    emp_id VARCHAR(20) PK,
    name VARCHAR(100),
    dept VARCHAR(50),
    role ENUM('EMPLOYEE','ADMIN') DEFAULT 'EMPLOYEE',
    vehicle_type ENUM('CAR','BIKE','EV') NOT NULL,
    sustainability_points INT DEFAULT 0
  )

- zones(
    zone_id INT PK AUTO_INCREMENT,
    zone_name VARCHAR(50),
    access_level ENUM('VISITOR','JUNIOR','SENIOR','EV') NOT NULL
  )

- slots(
    slot_id INT PK AUTO_INCREMENT,
    floor_no INT NOT NULL,
    zone_id INT NOT NULL FK->zones,
    slot_code VARCHAR(20) UNIQUE,   -- e.g., F2-S015
    status ENUM('FREE','OCCUPIED') DEFAULT 'FREE',
    has_ev_charger BOOLEAN DEFAULT FALSE
  )

- reservations(
    res_id BIGINT PK AUTO_INCREMENT,
    emp_id VARCHAR(20) NOT NULL FK->employees,
    slot_id INT NOT NULL FK->slots,
    entry_ts TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    exit_ts TIMESTAMP NULL,
    pinned_pillar_id VARCHAR(20) NULL,   -- e.g., P-12
    active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uniq_active_emp (emp_id, active),
    UNIQUE KEY uniq_active_slot (slot_id, active)
  )

Sprint2 minimal:
- visitor_invites(
    invite_id BIGINT PK AUTO_INCREMENT,
    host_emp_id VARCHAR(20) FK->employees,
    guest_name VARCHAR(100),
    guest_email VARCHAR(150),
    qr_token VARCHAR(64) UNIQUE,
    valid_until TIMESTAMP,
    used BOOLEAN DEFAULT FALSE
  )

- violations(
    violation_id BIGINT PK AUTO_INCREMENT,
    emp_id VARCHAR(20) FK->employees,
    slot_id INT FK->slots,
    reason VARCHAR(200),
    created_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  )

Seed data:
- 3 zones: EV/Visitor, Senior, General
- 2-3 floors, ~30 slots total with mixed zones; some EV charger slots
- 8 employees with different depts, roles, and vehicle types

========================
3) BACKEND (Spring Boot)
========================
Create backend as a runnable Spring Boot app with:
- Spring Web
- Spring Data JPA
- MySQL Driver
- Validation
Keep config in application.yml with env overrides.

Core requirements:
A) ENTRY + ALLOCATION (Sprint 1)
- Endpoint: POST /api/entry
  Body: { "empId": "E001" }
  Behavior:
    1) Validate employee exists.
    2) Determine eligible zone by rules:
       - vehicle_type == EV -> prefer EV zone (or any zone with has_ev_charger=true) first
       - role == ADMIN or dept == "Management" -> can use Senior zone
       - else -> General zone
       - Visitors are handled by /api/visitor/entry (below)
    3) Allocate FIRST available FREE slot in eligible zone(s) in a race-condition-safe way.
    4) Create reservation(active=true) and mark slot OCCUPIED.
  Response: { floorNo, slotId, slotCode, zoneName, reservationId }

Race condition safety (IMPORTANT):
- Use a transactional allocation method.
- Use SELECT ... FOR UPDATE SKIP LOCKED to pick a FREE slot safely:
  SELECT * FROM slots
  WHERE status='FREE' AND zone_id IN (...)
  ORDER BY floor_no, slot_id
  LIMIT 1
  FOR UPDATE SKIP LOCKED;
- If none available in preferred zone, fall back to next allowed zone.
- If still none, return 409 with message "Lot full for your eligibility".

B) EXIT
- Endpoint: POST /api/exit
  Body: { "empId": "E001" }
  Behavior:
    - Find active reservation for emp.
    - Set exit_ts, active=false.
    - Set slot status FREE.
  Response: { "status": "OK" }

C) LIVE LOT VIEW
- Endpoint: GET /api/slots?floorNo=2
  Response: list of slots with status, slotCode, zoneName, hasEvCharger
- Endpoint: GET /api/floors
  Response: available floor numbers

D) EMP PROFILE
- Endpoint: GET /api/employees/{empId}
- Endpoint: POST /api/employees (simple registration)
  Body: { empId, name, dept, vehicleType }
  role defaults EMPLOYEE

E) FIND MY CAR (Sprint 2 minimal)
- Endpoint: GET /api/mycar?empId=E001
  Response: active reservation info + slot + pinned_pillar_id
- Endpoint: POST /api/mycar/pin
  Body: { "empId":"E001", "pillarId":"P-12" }
  Saves pinned_pillar_id into active reservation.

F) VISITOR INVITE (simulated email)
- Endpoint: POST /api/visitor/invite
  Body: { "hostEmpId":"E001", "guestName":"Arun", "guestEmail":"a@x.com" }
  Generates qr_token random, valid_until +24h
  Response includes qr_token (simulate email by returning token)
- Endpoint: POST /api/visitor/entry
  Body: { "qrToken":"..." }
  Allocates a slot only from Visitor zone.
  Marks invite used=true.

G) ADMIN VIOLATIONS
- Endpoint: POST /api/admin/violations
  Header: X-EMP-ID (must be ADMIN)
  Body: { "empId":"E005", "slotId":12, "reason":"Bike in car spot" }
- Endpoint: GET /api/admin/violations (ADMIN only)

Auth (minimalist):
- No JWT. Use header X-EMP-ID for ADMIN endpoints only.
- For normal employee endpoints, empId in body/query is fine (MVP).

Return proper HTTP status codes and clear JSON errors.

Include:
- DTOs
- Services
- Repositories
- Entities
- Basic validation + exception handler

========================
4) FRONTEND (Angular)
========================
Create Angular app in frontend/ with:
Pages:
1) Login (enter empId)
2) Dashboard:
   - Show assigned slot (from /api/mycar)
   - Buttons: "Enter / Allocate", "Exit", "Set My Spot (Pin Pillar)"
3) Live Lot View:
   - Floor selector dropdown (from /api/floors)
   - Grid display of slots:
       GREEN = FREE, RED = OCCUPIED
       show slotCode and zone
   - Poll backend every 2 seconds (simple real-time)
4) Visitor Invite:
   - Form: guestName, guestEmail
   - On submit show returned qr_token big on screen (simulate QR text)
5) Admin (only if employee role ADMIN from /api/employees/{empId}):
   - Violations list
   - Create violation form

UI: keep it clean, minimal CSS. No material UI required. Basic responsive layout.

Angular services:
- api.service.ts for all HTTP
- state for current empId in localStorage

========================
5) QUALITY BAR
========================
- App should run with one command per part (backend and frontend).
- Backend must be transaction-safe for allocation.
- Provide sample curl commands in README.
- Keep code readable. No unnecessary abstractions.

Now generate ALL FILES with complete code.
Do NOT output explanations — output the full project code contents and file paths.
