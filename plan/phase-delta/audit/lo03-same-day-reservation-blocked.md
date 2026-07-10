# LO03 — ReservationController Prevents Same-Day Check-In

**Severity:** LOW  
**File:** `src/hms/controller/ReservationController.java` (lines 215–219)

## Problem

The `validateReservationInput()` method requires both `checkInDate` and `checkOutDate` to be future dates via `ValidationUtil.isFutureDate()`:
```java
if (!ValidationUtil.isFutureDate(checkInDate)) {
    throw new ValidationException("Check-in date must be in the future");
}
```

This means a walk-in guest arriving today cannot be registered through the system. The reservation creation itself is blocked for today's date.

**Impact:** Hotels cannot register same-day walk-in guests.

## Fix

Change the validation to:
- `checkInDate` >= today (allow today)
- `checkOutDate` > checkInDate (after check-in)
