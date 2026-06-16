# Issue 001: `Billing.java` — Schema- Model Mismatch

**Status:** Fixed  
**Date:** 2026-06-16  
**Found in:** Phase 2, Task 2.4  
**Root Cause:** Model was implemented with a different field design than the `billing` table schema.

---

## Discovery

During schema-to-model audit (Phase 2 → Phase 3 transition), `Billing.java` was found to have **10 mismatches** against the `database/schema.sql` `billing` table.

---

## Schema Definition (`database/schema.sql`)

```sql
CREATE TABLE billing (
    billing_id    INT             PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT            NOT NULL,
    room_charge   DECIMAL(12, 2)  NOT NULL,
    service_charge DECIMAL(12, 2) DEFAULT 0,
    other_charges DECIMAL(12, 2)  DEFAULT 0,
    tax_amount    DECIMAL(12, 2)  DEFAULT 0,
    total_bill    DECIMAL(12, 2)  NOT NULL,
    payment_status ENUM('pending','partial','paid','refunded') NOT NULL DEFAULT 'pending',
    payment_date  TIMESTAMP       NULL,
    notes         TEXT,

    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);
```

---

## Mismatches Found

### Missing Fields (5)

| Schema Column | Impact |
|---------------|--------|
| `room_charge` | DAO cannot persist room charge |
| `service_charge` | DAO cannot persist service charge |
| `other_charges` | DAO cannot persist extra charges (late checkout, damages) |
| `tax_amount` | DAO cannot persist tax — bill totals wrong |
| `notes` | No billing notes/adjustments field |

### Name Mismatch (1)

| Schema | Old Model | Issue |
|--------|-----------|-------|
| `total_bill` | `totalAmount` | Wrong column name — DAO mapping fails |

### Phantom Fields (4)

| Old Field | Why Wrong |
|-----------|-----------|
| `Guest guest` | No `guest_id` column exists in `billing` table |
| `double amountPaid` | No `amount_paid` column in schema |
| `String paymentMethod` | No `payment_method` column in schema |
| `LocalDateTime createdAt` | No `created_at` column in `billing` table |

---

## Fix Applied

Rewrote `Billing.java` to match the schema exactly:

### Corrected Fields

| Field | Java Type | Schema Column |
|-------|-----------|---------------|
| `billingId` | `int` | `billing_id` |
| `reservation` | `Reservation` | `reservation_id` (FK) |
| `roomCharge` | `double` | `room_charge` |
| `serviceCharge` | `double` | `service_charge` |
| `otherCharges` | `double` | `other_charges` |
| `taxAmount` | `double` | `tax_amount` |
| `totalBill` | `double` | `total_bill` |
| `paymentStatus` | `String` | `payment_status` |
| `paymentDate` | `LocalDateTime` | `payment_date` (nullable) |
| `notes` | `String` | `notes` (nullable) |

### Constructors

1. **New billing** — `Billing(Reservation, double roomCharge, double serviceCharge, double otherCharges, double taxAmount, double totalBill, String paymentStatus, LocalDateTime paymentDate, String notes)` — sets `billingId = 0`
2. **DB retrieval** — Full constructor including `billingId`

### Removed

- `Guest guest` field
- `double amountPaid` field
- `String paymentMethod` field
- `LocalDateTime createdAt` field
- `getGuestId()` convenience helper

---

## Verification

- [x] Clean & Build — *BUILD SUCCESSFUL* (17 source files)
- [x] Field count matches schema (10 fields vs 10 columns)
- [x] All FK references use object references per code_rules §5.3
- [x] Nullable columns (`payment_date`, `notes`) use nullable-capable types

---

## Lessons Learned

1. Always cross-reference model fields against `schema.sql` before implementation — a 1:1 column-to-field mapping is required.
2. The `plan/README.md` phase descriptions are the authoritative spec for model fields.
3. Run a schema-vs-model audit before starting each new phase to catch drift early.
