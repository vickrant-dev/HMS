# Task 5.4 — BillingController.java

**Package:** `hms.controller`
**File:** `src/hms/controller/BillingController.java`

## Description
Orchestrates bill generation (room+service+tax), payment recording, and revenue reporting.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `generateBill(Reservation, double, String)` | `Billing` | Calculate room/service/tax/total, DAO.save |
| 2 | `getBillById(int)` | `Billing` | DAO.getById |
| 3 | `getBillByReservationId(int)` | `Billing` | DAO.getByReservationId |
| 4 | `getAllBills()` | `List<Billing>` | DAO.getAll |
| 5 | `recordPayment(int, String)` | `void` | Validate status, DAO.updatePaymentStatus |
| 6 | `getRevenueByDateRange(LocalDate, LocalDate)` | `double` | DAO.getRevenueByDateRange |
| 7 | `calculateServiceCharges(int)` | `double` | ServiceBookingDAO.calculateServiceCharges |

## Dependencies
- BillingDAO (Phase 3.4)
- ServiceBookingDAO (Phase 3.7)
- DateUtil (Phase 4.2)
- Constants (Phase 0.5)

## Known Limitation
**FR-B3 (Bill Adjustments):** BillingDAO has no general `update` method — only `save` (INSERT) and `updatePaymentStatus`. Adjusting `otherCharges`, `notes`, or recalculating totals requires adding an `update` method to BillingDAO in a future task.

## Checklist
- [x] Create checklist file
- [x] Write `BillingController.java`
- [x] Verify Clean & Build
