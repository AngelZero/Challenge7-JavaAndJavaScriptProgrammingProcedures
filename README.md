# BookingMx

Minimal vanilla JS + Spring Boot project to practice unit tests.

---

## Sprint 1 — Backend Unit Tests (JUnit + JaCoCo)

**Scope**
- Implement unit tests for the **reservations module** (service layer).
- Cover main behaviors: **create**, **update**, **cancel**, **list**, and date **validation**.
- Include **positive and negative scenarios** (e.g., `BadRequestException`, `NotFoundException`).
- Measure coverage with **JaCoCo**; **target ≥ 90%**.  
  - Achieved **≥ 90% on the service layer** for this sprint.
- Provide evidence and a short issue log.

**Test locations**
- Unit tests: `src/test/java/com/bookingmx/reservations/...`
- Service tests file: `ReservationServiceTest.java`

**Evidence (added to repo)**
- `docs/tests-passing-sprint1.png` — screenshot of passing tests.
- `docs/coverage-sprint1.png` — screenshot of JaCoCo summary page.
- `docs/issues-sprint1.md` — brief log of issues found and how they were resolved.

---

## How to run the backend
```bash
cd backend
mvn spring-boot:run
````

## How to run tests and generate coverage

```bash
cd backend
mvn clean verify      # runs tests + generates JaCoCo HTML report
```

**Open coverage report**

```
backend/target/site/jacoco/index.html
```
---

## Repository notes (Sprint 1)

* Standard Maven layout:

  ```
  backend/
    src/
      main/
        java/...
        resources/...
      test/
        java/...
  ```
---

## Frontend (for later sprints)

```bash
cd frontend
npm i
npm run serve
# http://localhost:5173
```

> Frontend unit tests with Jest and additional documentation will be added in **Sprint 2** and **Sprint 3**, respectively.

```
```
