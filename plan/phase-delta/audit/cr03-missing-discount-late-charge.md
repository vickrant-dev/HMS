# CR03 — Billing Model Missing `discount_amount` and `late_charge` Fields

**Severity:** CRITICAL — Data loss  
**Files:** `src/hms/model/Billing.java`, `src/hms/dao/BillingDAO.java`  
**Note:** Regression — Issue-001 claimed this fixed, but `discount_amount`/`late_charge` appear to have been added to the schema after the fix

## Problem

The `billing` table schema includes:
- `discount_amount DECIMAL(12,2) DEFAULT 0`
- `late_charge DECIMAL(12,2) DEFAULT 0`

`BillingController.adjustBill()` writes these values via `BillingDAO.updateCharges()` which has the correct SQL, but:
1. `Billing.java` model has **no** `discountAmount` or `lateCharge` fields
2. `BillingDAO.SELECT_JOIN` does **not** select `b.discount_amount` or `b.late_charge`

**Impact:** Discount and late-charge adjustments are written to DB but **can never be read back** into a `Billing` object. Every `getById()` or `getAll()` returns objects with these values at default (0).

## Fix

1. Add `private final double discountAmount` and `private final double lateCharge` fields to `Billing.java`
2. Update both constructors
3. Add getters
4. Add `b.discount_amount, b.late_charge,` to `BillingDAO.SELECT_JOIN`
5. Update `mapResultSetToBilling()` to read them
