# ME03 — GuestCheckOutDialog paymentMethod/notes Never Saved

**Severity:** MEDIUM  
**File:** `src/hms/view/dialogs/GuestCheckOutDialog.java`

## Problem

The dialog collects `paymentMethodCmb` selection and a notes text area, but the save handler only calls:
```java
billingController.recordPayment(billing.getBillingId(), "paid");
```

The payment method and notes are never passed. Same root cause as HI06 — `recordPayment()` only accepts billing ID and status.

**Impact:** Payment method and notes from checkout are lost.

## Fix

Same as HI06 — extend the payment recording to accept and persist payment method and notes.
