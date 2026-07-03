# CR04 — 5 Filter Combo Boxes Contain Room Type Names Instead of Correct Values

**Severity:** CRITICAL — All five filters return zero results  
**Root Cause:** NetBeans Form Editor defaults (room type values) were never replaced with correct entity values

## Problem

Five combo boxes across 4 panels use room type names (`"Deluxe King Suite"`, `"Standard Double"`, `"Single Economy"`) instead of the values appropriate for their label and purpose:

| Panel | Combo Box | Label | Current Values | Should Be |
|-------|-----------|-------|----------------|-----------|
| `BillingManagementPanel` | `paymentStatusCmb` | "Payment Status" | Room types | `"All"`, `"Pending"`, `"Paid"`, `"Partial"`, `"Refunded"` |
| `ReservationPanel` | `statusCmb` | "Status" | Room types | `"All"`, `"pending"`, `"confirmed"`, `"checked_in"`, `"checked_out"`, `"cancelled"` |
| `StaffPanel` | `positionCmb` | "Position" | `"Available"`, `"Unavailable"` (status concepts) | `"All"`, `"Receptionist"`, `"Manager"`, … |
| `StaffPanel` | `departmentCmb` | "Department" | Room types | `"All"`, `"Front Desk"`, `"Kitchen"`, `"Housekeeping"`, … |
| `ServicePanel` | `serviceTypeCmb` | "Service type" | Room types | `"All"`, `"Food"`, `"Laundry"`, `"Spa"`, `"Conference"` |

Additionally, `RoomManagementPanel.statusCmb` also contains room type values and is labelled "Status" but its handler actually compares against `r.getRoomType()` — the label is misleading. Either rename to "Room Type" or replace with actual room statuses matching the label.

## Fix

For each combo box:
1. Replace the hardcoded `setModel()` values with correct entity values (use `Constants.java` constants where available)
2. Update the action listener/handler to use the correct comparison field
3. Use `"All"` as the first option for all-filter
