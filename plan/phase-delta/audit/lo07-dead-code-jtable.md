# LO07 — NewReservationDialog jTable2 Dead Code

**Severity:** LOW  
**File:** `src/hms/view/dialogs/NewReservationDialog.java`

## Problem

`jTable2` is declared (line 206), configured with a table model (lines 393–412), and placed inside `jScrollPane3`, but the component is **never used** anywhere in the dialog's logic. It is a leftover artifact from the GUI builder.

**Impact:** Dead component, wastes screen space if visible, or confuses maintainers.

## Fix

Remove `jTable2`, `jScrollPane3`, and associated setup from `initComponents()` (requires Form Editor).
