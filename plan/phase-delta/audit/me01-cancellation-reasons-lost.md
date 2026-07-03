# ME01 — CancellationDialog Reasons and Notes Never Persisted

**Severity:** MEDIUM  
**File:** `src/hms/view/dialogs/CancellationDialog.java`

## Problem

The dialog collects cancellation reasons via checkboxes and a `cancellationNotes` text area, but the save handler only calls:
```java
reservationController.cancelReservation(reservation.getReservationId());
```

The selected reasons and notes are **never passed** to the controller or persisted. The `cancelReservation()` signature takes only a reservation ID.

**Impact:** Cancellation reasons and notes are lost. Audit trail of why a reservation was cancelled is missing.

## Fix

Either:
1. Add a `notes` parameter to `ReservationController.cancelReservation()` and `ReservationDAO.updateStatus()`, or
2. Store cancellation data in a separate `cancellation_log` table, or
3. Update the reservation's `notes` field with the cancellation reason before marking as cancelled.
