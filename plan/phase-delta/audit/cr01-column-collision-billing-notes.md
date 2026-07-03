# CR01 — BillingDAO Column Collision: `b.notes` shadowed by `r.notes`

**Severity:** CRITICAL — Data corruption  
**File:** `src/hms/dao/BillingDAO.java`  
**Affected:** All billing read operations

## Problem

`SELECT_JOIN` (lines 22–40) selects both `b.notes` and `r.notes` without column aliases. Both columns resolve to `"notes"` in the JDBC result set. In standard JDBC behaviour `rs.getString("notes")` returns the **last** matching column in projection order.

In `mapResultSetToBilling()`:
- Line 290: `rs.getString("notes")` for `Billing.notes` — actually returns `r.notes` (Reservation's notes), not `b.notes`
- Line 274: `rs.getString("notes")` for `Reservation.notes` — also returns `r.notes` (correct, but by accident)

**Impact:** The Billing object's `notes` field is populated with the **Reservation's notes** instead of the Billing's own notes. Billing notes are always lost on read.

## Fix

1. In `SELECT_JOIN`: change `b.notes,` to `b.notes AS billing_notes,`
2. In `mapResultSetToBilling()` line 290: change `rs.getString("notes")` to `rs.getString("billing_notes")`
