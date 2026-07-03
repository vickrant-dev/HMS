# LO02 — GuestHistorySubPanel Still a UI Shell

**Severity:** LOW (was D5 scope)  
**File:** `src/hms/view/panels/GuestHistorySubPanel.java`

## Problem

Despite D5 (PR #22) being merged, `GuestHistorySubPanel` remains a bare UI shell:
- Constructor only calls `initComponents()` — no data loading
- No `loadGuestHistory()` method, no controller calls, no DAO queries
- All summary labels have hardcoded placeholders: `"John Scott"`, `"$9,450.00"`, `"12"`, `"45"`
- Table model has 4 empty rows with column headers but no data
- `backToGuestListBtnActionPerformed` handler is empty (`// TODO`)
- No pagination logic despite `pageNumber` label showing `"Page 1 of 5"`
- No `guestHistoryTable` — uses `recordsTable` but no table name suggests it displays reservations

**Impact:** Guest history feature is non-functional.

## Fix

Wire the panel to:
1. Accept a `Guest` parameter in constructor or via a setter
2. Query reservations for that guest via `ReservationController.getByGuestId()`
3. Populate summary labels (total spent, total nights, total stays, average per night)
4. Populate the records table
5. Wire the back button to navigate to guest list
