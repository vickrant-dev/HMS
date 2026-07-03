# ME05 — AddNewRoomDialog Description Text Field Never Read

**Severity:** MEDIUM  
**File:** `src/hms/view/dialogs/AddNewRoomDialog.java`

## Problem

The dialog UI has a `description` text field (line 75) but its content is **never read** in the save handler. The `Room` model has no `description` field — the data is silently discarded.

**Impact:** Room descriptions cannot be entered or persisted. The text field in the UI serves no purpose.

## Fix

Either:
1. Add a `description` field to the `Room` model (requires schema change), or
2. Remove the description text field from the dialog UI, or
3. Disable/hide the field if rooms don't support descriptions.
