
# BookingMx

Minimal Vanilla JS + Spring Boot project to practice **unit tests** and **documentation**.

---

## Project Overview

**Backend (Java/Spring Boot)** — Reservations module with endpoints to list, create, update, and cancel reservations. The service layer enforces date validation and status rules; data is kept in an in-memory repository.

**Frontend (Vanilla JS)** — Two features:
- **Nearby Cities Graph**: pure functions (`js/graph.js`) to validate datasets, build a graph, and compute nearby cities within a maximum distance, sorted ascending.
- **Reservations UI**: simple list/form that calls the backend API.

---

## Install & Run

### Backend
**Prereqs:** JDK 18 (or compatible), Maven.
```bash
cd backend
mvn spring-boot:run
# API: http://localhost:8080/api/reservations
````

Run tests + coverage (Sprint 1):

```bash
cd backend
mvn clean verify
# Coverage (HTML): backend/target/site/jacoco/index.html
```

### Frontend

**Prereqs:** Node.js 18+ and npm.

```bash
cd frontend
npm i
npm run serve
# http://localhost:5173
```

Run tests + coverage (Sprint 2):

```bash
cd frontend
npm run test
npm run test:coverage
# Coverage (HTML): frontend/coverage/lcov-report/index.html
```

---

## Sprint 1 — Backend Unit Tests (JUnit + JaCoCo)

**Scope**

* Unit tests for the **reservations module** (service layer).
* Behaviors: **create**, **update**, **cancel**, **list**; date **validation**.
* Positive & negative scenarios (`BadRequestException`, `NotFoundException`).
* Coverage measured with **JaCoCo**; **target ≥ 90%** on service layer.
* Evidence and short issue log included.

**Test locations**

* `backend/src/test/java/com/bookingmx/reservations/...`
* Main file: `ReservationServiceTest.java`

**Sample expected output (terminal)**

```
[INFO] --- surefire:test ---
Tests run: N, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- jacoco:report ---
```

---

## Sprint 2 — Frontend Unit Tests (Jest + Coverage)

**What was tested**

* Pure functions in `js/graph.js`: `Graph`, `validateGraphData`, `buildGraph`, `getNearbyCities`.
* Invalid datasets, unknown cities, invalid distances, filtering, sorting, default max distance, and non-Graph inputs.

**Environment**

* Native ESM via `"type": "module"`.
* Jest configured for Node environment; coverage limited to `js/graph.js` (this sprint’s scope).

**Sample expected output (terminal)**

```
PASS js/__tests__/graph.test.js
All files | % Stmts > 90 | % Branch > 90 | % Funcs > 90 | % Lines > 90
```

---

## Sprint 3 — Documentation & Diagrams

**What’s included**

* Javadoc/JsDoc added to core classes/functions
* Diagrams

---

## Repository Structure

```
backend/
  src/
    main/java/com/bookingmx/reservations/...
    test/java/com/bookingmx/reservations/...
  target/site/jacoco/index.html

frontend/
  js/graph.js
  js/__tests__/graph.test.js
  coverage/lcov-report/index.html
  index.html, app.js, js/api.js, styles.css

docs/
  coverage-sprint1.png
  tests-passing-sprint1.png
  coverage-sprint2.png
  tests-passing-sprint2.png
  diagrams-sprint3.pdf
  issues-sprint1.md
  issues-sprint2.md
```

---

## Examples (for quick verification)

**Backend — Create (valid)**

```
POST /api/reservations → 200 OK
{ "id": 1, "guestName": "...", "hotelName": "...", "status": "ACTIVE", ... }
```

**Backend — Create (invalid dates)**

```
POST /api/reservations → 400 Bad Request
{ "status": 400, "message": "Check-out must be after check-in" }
```

**Frontend — Nearby ≤ 50 km**

```
Destination: Guadalajara → Results: [Tlaquepaque (10), Zapopan (12)]
```

**Frontend — Unknown destination**

```
Destination: Foo City → Results: []
```
