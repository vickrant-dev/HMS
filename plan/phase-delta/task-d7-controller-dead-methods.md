# Task D7 — Controller Dead Method Cleanup

**Area:** Phase 5, 9, 12, 13 — Controller & Panel Filter Methods  
**Severity:** MEDIUM — dead code clutters API and creates maintenance burden

## Problem

Several controller filter/search methods are fully implemented but **never called from any panel**. They represent designed-in functionality that was never wired to UI controls.

| Controller | Unused Methods | Notes |
|---|---|---|
| `RoomController.java` | `filterByType(String)` | Panel has room type combo — should wire |
|  | `filterByPriceRange(double, double)` | No price range UI exists |
|  | `filterByStatus(String)` | Panel has status combo — should wire |
| `StaffController.java` | `filterByDepartment(String)` | Panel has department combo — should wire |
|  | `filterByPosition(String)` | Panel has position combo — should wire |
|  | `filterByStatus(String)` | Panel has status combo — should wire |
| `ServiceController.java` | `filterByType(String)` | Panel has service type combo — should wire |
|  | `filterByAvailability(boolean)` | Panel has availability filter — should wire |
|  | `toggleAvailability(int)` | Panel has toggle button — should wire |
| `BillingController.java` | `getRevenueByDateRange(LocalDate, LocalDate)` | No UI calls this |
|  | `calculateServiceCharges(int)` | Used by `generateBill()` internally but as a standalone method it has no caller |
| `GuestController.java` | `searchByEmail(String)` | Called by `searchGuests()` internally — could be made private |
|  | `searchByPhone(String)` | Same as above |

## Approach: Wire or Remove

For each method, determine the best approach:

### Wire to existing UI controls

The panels were designed with filter combo boxes and text fields but their action listeners were never connected:

| Panel UI Control | Controller Method to Wire |
|---|---|
| `RoomManagementPanel` → roomTypeCombo | `RoomController.filterByType()` |
| `RoomManagementPanel` → statusComboBox | `RoomController.filterByStatus()` |
| `StaffPanel` → departmentCombo | `StaffController.filterByDepartment()` |
| `StaffPanel` → positionCombo | `StaffController.filterByPosition()` |
| `StaffPanel` → statusCombo | `StaffController.filterByStatus()` |
| `ServicePanel` → serviceTypeCombo | `ServiceController.filterByType()` |
| `ServicePanel` → availabilityToggle | `ServiceController.filterByAvailability()` |

### Make internal methods private

- `GuestController.searchByEmail()` / `searchByPhone()` — only called by `searchGuests()`, should be `private`

### Keep as-is (available for future use)

- `BillingController.getRevenueByDateRange()` — no date range UI yet, but useful for dashboard/reports
- `RoomController.filterByPriceRange()` — no price range slider UI

## Verification

1. Each wired filter control triggers the correct controller method
2. Room panel filter by type works
3. Staff panel filter by department works
4. Service panel filter by type works
5. No compilation errors after access modifier changes

## Checklist

- [ ] Wire `RoomManagementPanel` filter controls to controller methods
- [ ] Wire `StaffPanel` filter controls to controller methods
- [ ] Wire `ServicePanel` filter controls to controller methods
- [ ] Make `GuestController.searchByEmail` / `searchByPhone` private
- [ ] Verify Clean & Build
