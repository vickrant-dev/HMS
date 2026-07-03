# LO06 — ServiceBooking.totalPrice Is Boxed Double (Nullable)

**Severity:** LOW  
**File:** `src/hms/model/ServiceBooking.java`

## Problem

The `totalPrice` field is declared as `Double` (boxed, nullable):
```java
private final Double totalPrice;
```

All other monetary fields in the system use primitive `double`:
- `Billing.roomCharge`, `serviceCharge`, `otherCharges`, `taxAmount`, `totalBill` — all `double`
- `Room.basePrice` — `double`
- `Service.price` — `double`
- `Staff.salary` — `Double` (this one is also boxed, for the same reason — salary can be null)

**Impact:** Inconsistent typing. Boxing/unboxing may cause unexpected NPE if `totalPrice` is null during arithmetic operations without null guards.

## Fix

Change to primitive `double` if the field is never null, or keep `Double` but add explicit null guards.
