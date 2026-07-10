# ME02 — GuestCheckInDialog checkInNotes Never Saved

**Severity:** MEDIUM  
**File:** `src/hms/view/dialogs/GuestCheckInDialog.java`

## Problem

The dialog has a `checkInNotes` text area, but the `confirmCheckInBtnActionPerformed` handler never reads or saves its content. It only calls:
```java
reservationController.checkIn(reservation.getReservationId());
```

**Impact:** Any notes entered during check-in are lost.

## Fix

Either:
1. Add a `notes` parameter to `checkIn()`, or
2. Update the reservation's `notes` field with check-in notes.
