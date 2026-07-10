# Task D5 — GuestHistorySubPanel Wiring

**Area:** Phase 8.3 — Guest History View (completion)  
**Severity:** HIGH — "View History" button shows a placeholder instead of the subpanel

## Problem

`GuestHistorySubPanel` exists at `src/hms/view/panels/GuestHistorySubPanel.java` (305 lines) but is:

1. A **complete stub** — all data hardcoded ("John Scott", "$9,450.00", "45", "12", "Page 1 of 5")
2. **Never instantiated** — `GuestManagementPanel.viewHistoryBtnActionPerformed()` shows a `JOptionPane` message instead of launching the subpanel

```java
// Current behavior (GuestManagementPanel.java:342-347):
JOptionPane.showMessageDialog(this, "Guest history for ... will be shown here.");
```

## Changes Required

### 1. `src/hms/view/panels/GuestHistorySubPanel.java`

- Add constructor that accepts `Guest selectedGuest` + controllers (`ReservationController`, `GuestController`)
- Store the guest and controllers as fields
- Add a `loadHistory()` method that queries:
  - `ReservationController.getReservationsByGuestId(guest.getGuestId())` — past reservations
  - Count total visits, compute total spent, calculate average per stay
  - Populate the table model with real data rows
- Update display labels with real values instead of hardcoded strings
- Wire "Back to Guest List" button to navigate back to `GuestManagementPanel`

### 2. `src/hms/view/panels/GuestManagementPanel.java`

- Replace the `JOptionPane` in `viewHistoryBtnActionPerformed()` with:
  ```java
  GuestHistorySubPanel historyPanel = new GuestHistorySubPanel(selected, reservationController, guestController);
  // Either swap in parent container or open in dialog
  ```
- Determine navigation approach: swap panel in parent container (consistent with tab navigation), or open as modal dialog

### 3. Container/Parent management

- If using panel-swapping: the parent container needs a method to navigate back (e.g., `showPanel()`)
- `MainWindow` or the guest panel's parent must support swapping between `GuestManagementPanel` and `GuestHistorySubPanel`

## Verification

1. Select a guest with reservation history
2. Click "View History" → GuestHistorySubPanel opens with real data
3. Labels show correct: visit count, total spent, average per stay
4. Reservation table shows actual past reservations with dates, amounts, status
5. Click "Back to Guest List" → returns to GuestManagementPanel

## Checklist

- [ ] Update `GuestHistorySubPanel` constructor to accept Guest + controllers
- [ ] Add `loadHistory()` with real DAO queries
- [ ] Update all labels to use real data
- [ ] Wire "Back to Guest List" button
- [ ] Update `GuestManagementPanel.viewHistoryBtnActionPerformed()` to launch the subpanel
- [ ] Determine and implement navigation approach (swap or dialog)
- [ ] Verify Clean & Build
