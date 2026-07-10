# Task D1 — Wire Check-In / Check-Out / Cancel Dialogs

**Area:** Phase 10 — Reservation UI (remediation)  
**Severity:** HIGH — users get no confirmation/feedback flow for critical reservation state transitions

## Problem

`ReservationPanel` calls reservation controller methods directly without any dialog:

| Action | Current Behavior | Expected Behavior |
|--------|-----------------|-------------------|
| Check-In | `reservationController.checkIn(id)` directly | `GuestCheckInDialog(this, true, reservation).setVisible(true)` |
| Check-Out | `reservationController.checkOut(id)` directly | `GuestCheckOutDialog(this, true, reservation).setVisible(true)` |
| Cancel | `reservationController.cancelReservation(id)` directly | `CancellationDialog(this, true, reservation).setVisible(true)` |

The dialogs (`GuestCheckInDialog`, `GuestCheckOutDialog`, `CancellationDialog`) are fully built and wired internally with validators, controller calls, and UI flows. They are **never instantiated** — they are dead code.

## Changes Required

### `src/hms/view/panels/ReservationPanel.java`

Three handler methods need to be updated:

1. **`checkInBtnActionPerformed()`** (~line 452)
   - Replace: `reservationController.checkIn(selected.getReservationId())`
   - With: `GuestCheckInDialog dialog = new GuestCheckInDialog(this, true, selected); dialog.setVisible(true);`

2. **`checkOutBtnActionPerformed()`** (~line 464)
   - Replace: `reservationController.checkOut(selected.getReservationId())` + manual billing call
   - With: `GuestCheckOutDialog dialog = new GuestCheckOutDialog(this, true, selected); dialog.setVisible(true);`

3. **`cancelResBtnActionPerformed()`** (~line 485)
   - Replace: direct cancel call
   - With: `CancellationDialog dialog = new CancellationDialog(this, true, selected); dialog.setVisible(true);`

### Import additions needed

```java
import hms.view.dialogs.GuestCheckInDialog;
import hms.view.dialogs.GuestCheckOutDialog;
import hms.view.dialogs.CancellationDialog;
```

## Verification

1. Open ReservationPanel, select a reservation with "confirmed" status
2. Click Check-In → GuestCheckInDialog opens → confirm → status changes to "checked_in"
3. Click Check-Out → GuestCheckOutDialog opens → enter payment → confirm → status changes to "checked_out"
4. Click Cancel → CancellationDialog opens → enter reason → confirm → status changes to "cancelled"

## Checklist

- [ ] Update `checkInBtnActionPerformed()` to launch `GuestCheckInDialog`
- [ ] Update `checkOutBtnActionPerformed()` to launch `GuestCheckOutDialog`
- [ ] Update `cancelResBtnActionPerformed()` to launch `CancellationDialog`
- [ ] Add missing imports
- [ ] Verify Clean & Build
