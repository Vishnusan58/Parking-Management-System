### CorpPark 360 — Complete Flow + Project Documentation Summary (MVP)

This is the full end-to-end story of how your system works, from gate entry to finding the car, plus what each module/API/UI does. Think of this as your “one doc to rule them all” for your team + presentation.

---

## 1) What the system solves (big picture)

CorpPark 360 is an employee parking automation system that:

* **Recognizes the user at entry** (Employee ID or Visitor QR)
* **Auto-assigns a slot** based on rules (dept/role/vehicle type)
* **Shows real-time occupancy** on a live parking grid
* **Lets people find their car later** (my slot + optional pillar pin)
* **Supports visitors** via QR-based entry into visitor zone
* **Allows admins to log violations** (wrong zone/vehicle misuse)

---

## 2) Actors (Who uses what)

### Employee

* Enters parking
* Gets slot assignment
* Checks “My Car”
* Pins pillar if parked elsewhere (open parking)
* Exits parking

### Visitor (Guest)

* Receives QR token from host employee
* Uses token at gate
* Gets assigned slot from visitor zone

### Admin

* Everything employee can do
* Can log violations
* Can view violation list
* (In full version: analytics dashboard, peak times, etc.)

---

## 3) Data Model Summary (MySQL)

### Zones

Defines policy areas:

* Visitor Zone
* EV Zone
* Senior Zone
* General Zone

### Slots

Each slot belongs to a zone + floor and has status:

* `FREE` or `OCCUPIED`
* `has_ev_charger` boolean
* `slot_code` like `F2-S015`

### Employees

Stores:

* Emp ID, name, dept
* Role: `EMPLOYEE` or `ADMIN`
* Vehicle type: `CAR/BIKE/EV`
* Sustainability points (future use for preferred parking)

### Reservations (Core engine)

One active reservation at a time:

* `emp_id`, `slot_id`, `entry_ts`, `exit_ts`
* `active=true/false`
* `pinned_pillar_id` for “last seen”

### Visitor Invites

* Host creates invite
* Token generated (`qr_token`)
* Validity window
* Marked used on entry

### Violations

* Admin logs wrong parking behavior
* Stores emp + slot + reason + time

---

## 4) Complete Flow (End-to-End)

### Flow A — Employee Entry → Slot Allocation → Navigation

**Step 1: Employee identification**

* At gate, employee enters/scans `empId` (MVP: typed in app/UI).
* Backend validates employee exists.

**Step 2: Eligibility rules decide zone priority**
Rules (minimal MVP logic):

1. If `vehicle_type=EV` → prefer EV charger slots / EV zone
2. If `dept=Management` or role qualifies → allow Senior zone
3. Else → General zone
4. Visitor zone is only for guest entry endpoint

**Step 3: Slot allocation (race-condition safe)**
Backend runs a transaction:

* Finds first `FREE` slot in eligible zones using **row lock**:

  * `SELECT … FOR UPDATE SKIP LOCKED`
* Marks slot `OCCUPIED`
* Creates `reservation(active=true)`

**Result:**
Employee gets:

* Floor number
* Slot code
* Zone name
  This becomes their navigation destination.

---

### Flow B — Live Lot View (Real-Time Occupancy)

**Step 1: Frontend requests floors**

* `GET /api/floors`

**Step 2: Load slot map for a floor**

* `GET /api/slots?floorNo=2`

**Step 3: Angular polls**

* Every 2 seconds, it re-fetches and re-renders:

  * Green = FREE
  * Red = OCCUPIED
  * Shows slot code + zone

This gives the “Live Lot” control-room vibe.

---

### Flow C — Find My Car (Return to Vehicle)

After 9 hours, employee forgets where they parked.

**Step 1: Employee opens dashboard**

* `GET /api/mycar?empId=E001`

**Backend returns:**

* Active reservation
* Slot details (floor/slot_code)
* pinned_pillar_id (optional)

**Step 2: UI shows “Return to Vehicle Pin”**

* If pillar pin exists, show it as a “precision marker”
* Else show slot itself as default

---

### Flow D — “Last Seen” / Set My Spot (Pillar Pin)

Used when employee parks in non-assigned/open areas.

**Step 1: Employee scans pillar QR**
MVP: employee manually enters pillar id like `P-12`

**Step 2: Save pin**

* `POST /api/mycar/pin`

  * `{ empId, pillarId }`

**Backend:**

* Updates active reservation’s `pinned_pillar_id`

Now “Find My Car” shows that exact pillar marker.

---

### Flow E — Exit Flow (Slot release)

**Step 1: Employee exits**

* `POST /api/exit` `{ empId }`

**Backend:**

* Finds active reservation
* Sets `exit_ts`, `active=false`
* Sets slot status back to `FREE`

Live lot immediately updates because grid is polling.

---

### Flow F — Visitor Pre-Registration + Visitor Entry

**Step 1: Employee invites guest**

* `POST /api/visitor/invite`

  * `{ hostEmpId, guestName, guestEmail }`

**Backend:**

* Generates `qr_token`
* Sets `valid_until = now + 24h`
* Returns `qr_token` (simulated email)

**Step 2: Guest entry at gate**

* `POST /api/visitor/entry` `{ qrToken }`

**Backend:**

* Validates token not expired/not used
* Allocates slot only in Visitor zone
* Marks invite `used=true`

Visitor gets assigned slot like employee.

---

### Flow G — Violation Logging (Admin)

**Step 1: Admin logs violation**

* `POST /api/admin/violations`
* Header: `X-EMP-ID: <adminId>`
* Body: `{ empId, slotId, reason }`

**Backend:**

* Checks admin role via header employee lookup
* Saves violation record

**Step 2: Admin views list**

* `GET /api/admin/violations` with same header

---

## 5) Feature Summary (Sprint mapping)

### Sprint 1 — Allocation & Occupancy (Foundation)

✅ Employee entry recognition (empId)
✅ Zone-based assignment logic
✅ Race-condition safe allocation
✅ Reservations tracking
✅ Exit releasing slot
✅ Live lot grid in Angular

### Sprint 2 — Smart Assistant & Analytics (Polish)

✅ Find My Car (active reservation lookup)
✅ Last seen pillar pin
✅ Visitor pre-registration + token entry
✅ Violation logging + admin view
(Analytics “peak time” can be added next)

---

## 6) Backend API Summary (what exists)

Employee:

* `POST /api/employees` (register)
* `GET /api/employees/{empId}` (profile)
* `POST /api/entry` (allocate slot)
* `POST /api/exit` (release)
* `GET /api/mycar?empId=...` (find car)
* `POST /api/mycar/pin` (pillar pin)
* `GET /api/floors`
* `GET /api/slots?floorNo=...`

Visitor:

* `POST /api/visitor/invite`
* `POST /api/visitor/entry`

Admin:

* `POST /api/admin/violations` (header protected)
* `GET /api/admin/violations`

---

## 7) Frontend UI Summary (Angular screens)

1. **Login**

* Enter Emp ID (stored in localStorage)

2. **Dashboard**

* Shows assigned slot + zone + floor
* Buttons: Enter, Exit, Pin pillar

3. **Live Lot View**

* Floor dropdown
* Grid with real-time status

4. **Visitor Invite**

* Guest form
* Shows returned QR token big

5. **Admin**

* Create violation
* View violations list

---

## 8) Key Technical Notes (the “why this is good” part)

### Race condition solved

Two cars cannot be assigned same slot because:

* Allocation happens inside transaction
* Slot row is locked when picked (`FOR UPDATE SKIP LOCKED`)
* Active slot uniqueness is enforced (`uniq_active_slot`)

### Minimal auth (MVP)

No JWT, no complex login:

* Emp ID stored on frontend
* Admin endpoints protected using header `X-EMP-ID`

This keeps it hackathon/college-friendly but functional.

---

## 9) Future Enhancements (next-level ideas)

* Analytics “peak time” (GROUP BY hour, occupancy %)
* Preferred parking by sustainability points
* Real QR scanning (mobile camera)
* Push notifications (“Your car is at F2-S015”)
* Heatmap per floor
* ML prediction of availability

---

If you need it, I can also convert this into a **proper college-style SRS document format** (Abstract → Objectives → Scope → Modules → ER Diagram notes → DFD → API specs → Testing cases) in one shot.
