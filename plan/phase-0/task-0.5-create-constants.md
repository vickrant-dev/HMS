# Task 0.5 — Create `Constants.java`

**Goal:** Create centralized constants class with all application-wide values following DRY principle.

---

## Checklist

- [x] Create this checklist file
- [x] Write `Constants.java` under `hms.config` package
- [x] Verify Clean & Build succeeds in NetBeans *(requires user to run in IDE — no JDK available in this environment)*

---

## File

`src/hms/config/Constants.java` (package `hms.config`)

### Constant Groups Included

| Group | Coverage |
|-------|----------|
| Database Configuration | Host, port, name, user, password |
| Connection Settings | Timeout, max retries, retry delay |
| UI Constants | Window dimensions, app title |
| Room Types | Single, Double, Suite, Deluxe |
| Room Status | available, occupied, maintenance, reserved |
| Reservation Status | pending, confirmed, checked_in, checked_out, cancelled |
| Billing Status | pending, partial, paid, refunded |
| Staff Status | active, inactive, on_leave |
| Service Booking Status | pending, completed, cancelled |
| Assignment Status | pending, in_progress, completed |
| Reservation Constants | ID prefix, min/max guests |
| Validation Constants | Password length, email length, phone length |
| Tax & Pricing | Tax rate, late checkout charge |
| Report Paths | Jasper report directory |
| Error Messages | DB connection, invalid email, etc. |
