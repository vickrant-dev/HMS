# ME09 — RoomAssignmentPanel / ReportsPanel Combo Boxes Incomplete

**Severity:** MEDIUM  
**Files:** `src/hms/view/panels/RoomAssignmentPanel.java`, `src/hms/view/panels/ReportsPanel.java`

## Problem

Several combo boxes in panels are incomplete:

**RoomAssignmentPanel:**
- `statusCmb`: Only has `"All"` — missing `"pending"`, `"in_progress"`, `"completed"` (defined in `Constants.java` lines 62–64)
- `staffCmb`: Only has `"All"` — should be dynamically populated from `staffController.getAllStaff()` in the constructor

**ReportsPanel:**
- `roomTypeCmb`: Only has `"All"`, `"Deluxe Suite"` — missing `"Single"`, `"Double"`, `"Suite"`, `"Deluxe"` (defined in `Constants.java` lines 27–30)
- `statusCmb`: Has `"Active"`, `"Inactive"` — unclear what entity this filters (reservation? room? staff?). Ambiguous and handler is empty anyway.

**Impact:** Users cannot filter by actual values; partial/incomplete filter options.

## Fix

1. `RoomAssignmentPanel`: Populate `statusCmb` from `Constants`, populate `staffCmb` from `StaffController`
2. `ReportsPanel`: Replace `roomTypeCmb` values with full set; clarify `statusCmb` purpose (add label or rename)
