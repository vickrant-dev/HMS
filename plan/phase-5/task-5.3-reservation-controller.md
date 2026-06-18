# Task 5.3 — ReservationController.java

**Package:** `hms.controller`
**Files:**
- `src/hms/controller/DashboardObserver.java` (interface)
- `src/hms/controller/ReservationController.java`

## Description
Orchestrates reservation CRUD, check-in/check-out workflow, room status transitions, and observer notifications.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `addObserver(DashboardObserver)` | `void` | Register observer |
| 2 | `removeObserver(DashboardObserver)` | `void` | Unregister observer |
| 3 | `createReservation(...)` | `Reservation` | Validate, check availability, calculate price, save, notify |
| 4 | `getReservationById(int)` | `Reservation` | DAO.getById |
| 5 | `getAllReservations()` | `List<Reservation>` | DAO.getAll |
| 6 | `updateReservation(Reservation)` | `void` | Re-validate, recalculate, DAO.update |
| 7 | `cancelReservation(int)` | `void` | Update status + room, notify |
| 8 | `checkIn(int)` | `void` | Update status + room, notify |
| 9 | `checkOut(int)` | `void` | Update status + room, notify |
| 10 | `getByGuestId(int)` | `List<Reservation>` | DAO.getByGuestId |
| 11 | `getByStatus(String)` | `List<Reservation>` | DAO.getByStatus |
| 12 | `getByDateRange(LocalDate, LocalDate)` | `List<Reservation>` | DAO.getByDateRange |
| 13 | `getTodayCheckIns()` | `List<Reservation>` | DAO.getTodayCheckIns |
| 14 | `getTodayCheckOuts()` | `List<Reservation>` | DAO.getTodayCheckOuts |

## Dependencies
- ReservationDAO (Phase 3.3)
- RoomDAO (Phase 3.2) — via RoomController
- DateUtil (Phase 4.2)
- PricingStrategy (Phase 4.6)
- ValidationUtil (Phase 4.1)
- Constants (Phase 0.5)

## Checklist
- [x] Create checklist file
- [x] Write `DashboardObserver.java`
- [x] Write `ReservationController.java`
- [x] Verify Clean & Build
