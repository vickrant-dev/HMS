# Audit Findings — Phase Delta D10 Remediation

Comprehensive codebase audit conducted 2026-07-04 covering:
- All 8 models, 7 controllers, 8 DAOs
- All 8 main panels, 12 dialogs
- DB schema vs Java column comparison
- Combo box values vs Constants/DB validation

## Severity Key

| Label | Count | Definition |
|-------|-------|------------|
| **CRITICAL** | 4 | Data corruption or zero-result filters |
| **HIGH** | 8 | Silent data loss, broken UX workflows |
| **MEDIUM** | 12 | Missing data persistence, placeholder/incorrect values |
| **LOW** | 10 | Code quality, dead code, edge cases |
| **Total** | 34 | |

## Issue Index

| ID | File | Title | Severity |
|----|------|-------|----------|
| CR01 | `cr01-column-collision-billing-notes.md` | BillingDAO column collision — `b.notes` shadowed by `r.notes` | CRITICAL |
| CR02 | `cr02-column-collision-booking-date.md` | ServiceBookingDAO column collision — `sb.booking_date` shadowed by `r.booking_date` | CRITICAL |
| CR03 | `cr03-missing-discount-late-charge.md` | Billing model missing `discount_amount` / `late_charge` fields | CRITICAL |
| CR04 | `cr04-combo-box-wrong-entity-values.md` | 5 filter combo boxes contain room type names instead of correct entity values | CRITICAL |
| HI01 | `hi01-guest-update-missing-email.md` | GuestDAO.update() missing `email = ?` in SET clause | HIGH |
| HI02 | `hi02-staff-update-missing-email.md` | StaffDAO.update() missing `email = ?` in SET clause | HIGH |
| HI03 | `hi03-reservation-date-range-logic.md` | ReservationDAO.getByDateRange() wrong overlap logic | HIGH |
| HI04 | `hi04-guest-search-dedup-broken.md` | GuestController.searchGuests() dedup broken (no equals/hashCode) | HIGH |
| HI05 | `hi05-payment-summary-broken.md` | PaymentDialog alreadyPaid/balanceDue both set to totalBill | HIGH |
| HI06 | `hi06-payment-data-lost.md` | PaymentDialog collects data but never passes to recordPayment() | HIGH |
| HI07 | `hi07-hardcoded-password.md` | AddNewStaffDialog hardcoded default password "password123" | HIGH |
| HI08 | `hi08-wrong-button-text.md` | NewRoomAssignmentDialog button says "Save Staff Profile" | HIGH |
| ME01 | `me01-cancellation-reasons-lost.md` | Cancellation reasons + notes never persisted | MEDIUM |
| ME02 | `me02-checkin-notes-lost.md` | GuestCheckInDialog checkInNotes never saved | MEDIUM |
| ME03 | `me03-checkout-data-lost.md` | GuestCheckOutDialog paymentMethod/notes never saved | MEDIUM |
| ME04 | `me04-service-booking-notes-lost.md` | ServiceBookingDialog notes never saved | MEDIUM |
| ME05 | `me05-room-description-lost.md` | AddNewRoomDialog description text field never read | MEDIUM |
| ME06 | `me06-assignment-type-wrong-value.md` | NewRoomAssignmentDialog assignmentTypeCmb has staff name | MEDIUM |
| ME07 | `me07-new-total-preview-not-updated.md` | AdjustmentDialog "New Total" label never recalculated | MEDIUM |
| ME08 | `me08-placeholder-combo-values.md` | exportCmb / reportTypeCmb show "Item 1–4" placeholders | MEDIUM |
| ME09 | `me09-incomplete-panel-combos.md` | RoomAssignmentPanel/ReportsPanel combo boxes incomplete | MEDIUM |
| ME10 | `me10-incomplete-dialog-combos.md` | AddNewRoom/AddNewService/AddNewStaff combo boxes incomplete | MEDIUM |
| ME11 | `me11-capacity-trailing-space.md` | RoomManagementPanel capacityCmb trailing space " " | MEDIUM |
| ME12 | `me12-status-case-inconsistency.md` | AddNewStaffDialog statusCmb Title Case vs DB lowercase | MEDIUM |
| LO01 | `lo01-no-parent-refresh.md` | All dialogs fail to refresh parent panel after save | LOW |
| LO02 | `lo02-guest-history-not-wired.md` | GuestHistorySubPanel still a UI shell (D5 incomplete) | LOW |
| LO03 | `lo03-same-day-reservation-blocked.md` | ReservationController prevents today's walk-in check-ins | LOW |
| LO04 | `lo04-ground-floor-rejected.md` | RoomController rejects floor 0 (ground floor) | LOW |
| LO05 | `lo05-noop-statement.md` | AddGuestDialog line 53 — discarded nationality.getText() | LOW |
| LO06 | `lo06-boxed-double-inconsistent.md` | ServiceBooking.totalPrice boxed Double vs primitive double | LOW |
| LO07 | `lo07-dead-code-jtable.md` | NewReservationDialog jTable2 unused | LOW |
| LO08 | `lo08-empty-report-handlers.md` | ReportsPanel handler bodies empty (TODO) | LOW |
| LO09 | `lo09-change-due-invisible.md` | GuestCheckOutDialog changeDueAmount set then hidden by dispose() | LOW |
| LO10 | `lo10-dead-dao-methods.md` | ReservationDAO.delete() / getByRoomId() not exposed via controller | LOW |
