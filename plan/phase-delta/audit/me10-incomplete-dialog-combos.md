# ME10 — AddNewRoom / AddNewService / AddNewStaff Combo Boxes Incomplete

**Severity:** MEDIUM  
**Files:**
- `src/hms/view/dialogs/AddNewRoomDialog.java`
- `src/hms/view/dialogs/AddNewService.java`
- `src/hms/view/dialogs/AddNewStaffDialog.java`

## Problem

**AddNewRoomDialog:**
- `roomTypeCmb`: `{"Single", "Double"}` — missing `"Suite"`, `"Deluxe"` (defined in `Constants.java` lines 27–30)

**AddNewService:**
- `serviceTypeCmb`: `{"Food", "Personal Care", "Transport"}` — `"Personal Care"` and `"Transport"` are not in `Constants.java`; missing `"Laundry"`, `"Spa"`, `"Conference"` (defined in `Constants.java` lines 81–84)

**AddNewStaffDialog:**
- `positionCmb`: `{"Receptionist", "Manager"}` — likely incomplete for a real hotel
- `departmentCmb`: `{"Front Desk", "Kitchen"}` — missing `"Housekeeping"`, `"Maintenance"`, etc.
- `statusCmb`: `{"Active", "Inactive"}` — missing `"on_leave"` (defined in `Constants.java` line 54)

**Impact:** Users can only create entities with a limited subset of valid values.

## Fix

Replace hardcoded combo models with values from `Constants.java`:
- `AddNewRoomDialog.roomTypeCmb` → `new String[]{"Single", "Double", "Suite", "Deluxe"}`
- `AddNewService.serviceTypeCmb` → `new String[]{"Food", "Laundry", "Spa", "Conference"}` (or load from a service)
- `AddNewStaffDialog.*` → extend with full sets
