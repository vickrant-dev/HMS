# ME07 — AdjustmentDialog "New Total" Label Never Recalculated

**Severity:** MEDIUM  
**File:** `src/hms/view/dialogs/AdjustmentDialog.java` (line 109)

## Problem

The "New Total" preview label (`jLabel10`) is set once in the initComponents with hardcoded text `"LKR 1,450.00"` and is **never updated** when the user modifies adjustment values. The label should dynamically recalculate: `currentTotal + discountAmount - lateCharge + otherCharges`.

**Impact:** User sees a stale placeholder value, not the real calculated total.

## Fix

Add a document listener or key listener to the amount fields that recalculates and updates `jLabel10` in real time.
