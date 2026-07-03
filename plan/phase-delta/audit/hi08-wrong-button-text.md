# HI08 — NewRoomAssignmentDialog Button Says "Save Staff Profile"

**Severity:** HIGH — UX error  
**File:** `src/hms/view/dialogs/NewRoomAssignmentDialog.java` (line 136)

## Problem

The "Save" button on the New Room Assignment dialog has the wrong text:
```
saveBtn.setText("Save Staff Profile");
```

It should say `"Save Assignment"` or `"Create Assignment"`. This is likely a copy-paste error from `AddNewStaffDialog`.

**Impact:** Confusing UX — user creates a room assignment but the button says they're saving a staff profile.

## Fix

Change the button text to `"Save Assignment"`.
