# ME06 — NewRoomAssignmentDialog assignmentTypeCmb Has Staff Name

**Severity:** MEDIUM  
**File:** `src/hms/view/dialogs/NewRoomAssignmentDialog.java` (line 126)

## Problem

The `assignmentTypeCmb` combo box is hardcoded with:
```java
new String[] { "Carol Lee - Housekeeping" }
```

This is a **staff name**, not an assignment type. The save handler uses `assignmentTypeCmb.getSelectedItem().toString()` as the `assignType`, meaning this string literal would be saved to the `assignment_type` database column. Data inconsistency across spelling: `staffCmb` has `"Caroll Lee"` (double-l) but this has `"Carol"` (single-l).

**Impact:** Assignment types are meaningless strings in DB. The actual assignment type (Cleaning, Maintenance, Inspection) is never set.

## Fix

Replace the hardcoded model with valid assignment types from `Constants.java`:
```java
new String[] { "Cleaning", "Maintenance", "Inspection" }
```
