# HI05 — PaymentDialog Already-Paid / Balance-Due Both Set to Total Bill

**Severity:** HIGH — Broken billing workflow  
**File:** `src/hms/view/dialogs/PaymentDialog.java` (lines 45–46)

## Problem

```java
alreadyPaidAmount.setText(String.format("LKR %.2f", billing.getTotalBill()));
balanceDueAmount.setText(String.format("LKR %.2f", billing.getTotalBill()));
```

Both `alreadyPaidAmount` and `balanceDueAmount` labels are set to `billing.getTotalBill()`. This makes the billing summary contradictory — it looks like the full amount is both already paid and still due.

**Impact:** User cannot tell how much has already been paid versus how much is still outstanding.

## Fix

Calculate actually paid amount from payment history (if available) or default to 0.0, then:
- `alreadyPaidAmount` = amount already paid (from DB or 0)
- `balanceDueAmount` = `billing.getTotalBill() - alreadyPaid`
