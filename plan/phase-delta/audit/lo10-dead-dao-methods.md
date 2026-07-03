# LO10 — ReservationDAO Dead Methods Not Exposed via Controller

**Severity:** LOW  
**File:** `src/hms/dao/ReservationDAO.java`

## Problem

Two methods exist in `ReservationDAO` but are not exposed through `ReservationController`:
- `getByRoomId(int roomId)` — queries reservations by room ID
- `delete(int reservationId)` — deletes a reservation from the DB

No controller method calls these, making them dead code.

**Impact:** Unused code that may confuse maintainers. Also, `delete()` bypassing the controller means any future delete logic (e.g., validation, cascading) would be missed.

## Fix

Either:
1. Expose them via `ReservationController` if the functionality is needed, or
2. Remove them if not needed.
