# HI06 — PaymentDialog Collects Data But Never Passes to recordPayment()

**Severity:** HIGH — Data loss  
**File:** `src/hms/view/dialogs/PaymentDialog.java`

## Problem

The dialog collects from the user:
- `amountToPay` (text field)
- `paymentMethodCmb` (combo box)
- `transactionId` (text field)
- `notes` (text area)

But the save handler only calls:
```java
billingController.recordPayment(billing.getBillingId(), "paid");
```

The hardcoded status `"paid"` is always passed. **None of the user-collected data** is forwarded to the controller. The `recordPayment()` method signature `recordPayment(int billingId, String paymentStatus)` doesn't even accept amount/method/notes — the DAO's `UPDATE billing SET payment_status = ?, payment_date = ?` only updates status and date.

**Impact:** Amount paid, payment method, transaction ID, and notes are all lost. The billing record always shows `"paid"` regardless of the actual amount entered.

## Fix

1. Extend `BillingDAO.recordPayment()` (or create a new DAO method) to persist `amount_paid`, `payment_method`, `transaction_id`, and `notes`
2. Add these columns to the `billing` table schema if they don't exist, or create a separate `payments` table
3. Update `PaymentDialog` to pass collected data to the controller
