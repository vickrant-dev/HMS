# Issue 006: Phase 5 Controllers — Audit vs Plan, PRD & code_rules

**Status:** Open  
**Date:** 2026-06-18  
**Found in:** Phase 5 completion audit  
**Root Cause:** Controllers were implemented from a task-level understanding of CRUD operations without cross-referencing against `plan/README.md` dependency specifications, `PRD.md` functional requirements, and `code_rules.md` documentation standards. The result is a mixture of architectural contradictions (missing dependency integration), PRD feature gaps, and documentation violations.

---

## Files Audited (6)

| Controller | Lines | Public Methods | Dependencies Used | Dependencies Per Plan |
|------------|-------|----------------|-------------------|----------------------|
| `GuestController.java` | 145 | 7 | GuestDAO | 3.1, 4.1 |
| `RoomController.java` | 135 | 8 | RoomDAO | 3.2, 4.1 |
| `ReservationController.java` | 256 | 12 | ReservationDAO, RoomController | 3.3, 3.2, 3.4, 4.2, 4.6 |
| `BillingController.java` | 110 | 6 | BillingDAO, ServiceBookingDAO | 3.4, 3.7 |
| `StaffController.java` | 199 | 11 | StaffDAO, RoomAssignmentDAO | 3.5, 3.8 |
| `ServiceController.java` | 178 | 10 | ServiceDAO, ServiceBookingDAO | 3.6, 3.7 |

---

## Table of Contents

1. [CRITICAL — BUG: Reservation Display ID Not Generated](#1-critical--bug-reservation-display-id-not-generated)
2. [CRITICAL — Plan Contradiction: Missing BillingDAO Integration in ReservationController](#2-critical--plan-contradiction-missing-billingdao-integration-in-reservationcontroller)
3. [CRITICAL — PRD Mismatch: No Pre-deletion Business Checks (Guest & Room)](#3-critical--prd-mismatch-no-pre-deletion-business-checks-guest--room)
4. [MODERATE — PRD Mismatch: Cancel Reservation Has No Reason Parameter](#4-moderate--prd-mismatch-cancel-reservation-has-no-reason-parameter)
5. [MODERATE — PRD/Schema Contradiction: No paymentMethod on Billing (Reversion of Issue 001)](#5-moderate--prdschema-contradiction-no-paymentmethod-on-billing-reversion-of-issue-001)
6. [MODERATE — code_rules Contradiction: Exception-Handling Pattern Not Followed](#6-moderate--code_rules-contradiction-exception-handling-pattern-not-followed)
7. [MODERATE — code_rules Violation: Zero JavaDoc on All 6 Controllers (§10.1)](#7-moderate--code_rules-violation-zero-javadoc-on-all-6-controllers-101)
8. [MODERATE — Plan Mismatch: No Receipt Data Structure in BillingController](#8-moderate--plan-mismatch-no-receipt-data-structure-in-billingcontroller)
9. [LIMITATIONS — Known/Deferred](#9-limitations--knowndeferred)
10. [MINOR — Observations & Quality](#10-minor--observations--quality)
11. [Cross-cutting: Cascade Effects of Fixes](#11-cross-cutting-cascade-effects-of-fixes)
12. [Summary](#12-summary)
13. [Recommended Fix Order](#13-recommended-fix-order)

---

## 1. CRITICAL — BUG: Reservation Display ID Not Generated

**PRD Reference:** FR-RES1 §6.3 (line 373):
> *"Auto-generate reservation ID (format: RES-YYYYMMDD-XXXXX)"*

**PRD Reference:** §9.3 Data Constraints (line 639):
> *"Reservation ID format: RES-YYYYMMDD-XXXXX"*

### Discovery

`StringUtil.generateReservationId()` exists at `src/hms/util/StringUtil.java:75–81` — implemented correctly in Phase 4.3:

```java
public static String generateReservationId() {
    String datePart = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    int randomPart = ThreadLocalRandom.current().nextInt(100000);
    return Constants.RES_ID_PREFIX + "-" + datePart + "-"
            + String.format("%05d", randomPart);
}
```

However, this method is **never called anywhere** in the entire codebase. A grep for `generateReservationId` returns exactly one hit: the definition itself. `ReservationController.createReservation()` at line 41–72 never invokes it.

### Impact

| Concern | Detail |
|---------|--------|
| **PRD compliance** | FR-RES1 explicitly requires this format — missing entirely |
| **Display/UX** | Users see integer `reservation_id` (e.g., "42") instead of a formatted display ID (e.g., "RES-20260618-00345") |
| **Search/Filter** | UI panels (Phase 10) intended to search by reservation ID will have no display ID to query |
| **Reports (Phase 14)** | Guest invoice report would show numeric DB ID instead of the canonical RES-format ID |

### Why It Exists

`StringUtil` was created in Phase 4.3 with `generateReservationId()` as specified, but `ReservationController` was implemented in Phase 5.3 without integrating it. The task checklist for 5.3 (`task-5.3-reservation-controller.md`) did not include "call StringUtil.generateReservationId()" as a subtask.

### Fix Options

**Option A — Store in DB (full solution):**

Add `display_id VARCHAR(20)` column to `reservations` table, add field to `Reservation.java`, update `ReservationDAO.save()` to store it, update `mapResultSetToReservation()` to read it. Call `StringUtil.generateReservationId()` in `ReservationController.createReservation()` before saving.

**Option B — Compute dynamically (lightweight):**

Compute display ID on-the-fly in a getter convenience method on Reservation or in the controller. No schema/model changes. But display IDs cannot be searched/filtered by in the DB, and Phase 10 UI panels cannot display them in tables without re-computing.

**Recommendation:** Option A, since PRD requires display IDs to be queryable and displayable.

---

## 2. CRITICAL — Plan Contradiction: Missing BillingDAO Integration in ReservationController

**Plan Reference:** Phase 5, Task 5.3 (line 95):
> *"Create/modify/cancel reservations, check-in/check-out workflow, room status updates, observer notifications"*

**Dependencies listed (plan line 95):**
> *"3.3, 3.2, 3.4, 4.2, 4.6"*

| Dependency | Artifact | Used? |
|------------|----------|-------|
| 3.3 | ReservationDAO | ✅ Used at line 21 |
| 3.2 | RoomDAO (via RoomController) | ✅ Used at line 22 |
| **3.4** | **BillingDAO** | **❌ NEVER imported or used** |
| 4.2 | DateUtil | ✅ Used at line 12 |
| 4.6 | PricingStrategy | ✅ Used at line 10 |

### The Contradiction

The plan explicitly states BillingDAO (dependency 3.4) is a dependency of ReservationController, but the controller has:

```java
private final ReservationDAO reservationDAO;   // line 21
private final RoomController roomController;   // line 22
private final List<DashboardObserver> observers;  // line 23
```

No `BillingDAO` field. No `BillingController` field. No billing import. The `checkOut()` method at line 160–178 does NOT generate or finalize billing:

```java
public void checkOut(int reservationId)
        throws ValidationException, DatabaseException {
    // ... validates reservation ...
    reservationDAO.updateStatus(reservationId, Constants.RES_STATUS_CHECKED_OUT);
    roomController.updateRoomStatus(
            reservation.getRoomId(), Constants.ROOM_STATUS_AVAILABLE);
    notifyCheckOut(reservation);
    // NO billing generation here!
}
```

### Impact

| Concern | Detail |
|---------|--------|
| **Check-out workflow incomplete** | PRD FR-RES4 §6.3: *"Finalize billing"* — the check-out in ReservationController does not generate the bill. Billing is handled separately through BillingController. The two workflows are disconnected. |
| **Plan vs code inconsistency** | Documentation says one thing, code does another. Future maintainers will add BillingDAO if trusting the plan, creating a duplicate. |
| **UI integration risk** | Phase 10.4 (CheckOutDialog) will need to call both `reservationController.checkOut()` AND `billingController.generateBill()` — but neither controller invokes the other. |

### Root Cause

The controller was written to the task checklist description without consulting the dependency column in `plan/README.md`. The dependency `3.4` was likely meant to indicate that ReservationController's `checkOut()` should integrate with billing, but the implementer treated 3.4 as BillingController's concern only.

### Fix Options

**Option A — Plan correction (simplest):**

Remove `3.4` from ReservationController's dependency list in `plan/README.md`. Acknowledge that check-out workflow is split: ReservationController handles status transitions, BillingController handles financial finalization. Update the task description to clarify this boundary.

**Option B — Full integration (architectural shift):**

Inject `BillingController` into `ReservationController` and call `generateBill()` inside `checkOut()`. This makes check-out truly finalize billing in one call. However, this creates a circular risk: BillingController uses ServiceBookingDAO which is also accessible through ServiceController. Tight coupling concern.

**Recommendation:** Option A — the split-controller design is valid MVC. The plan was simply imprecise about where billing integration lives. The `CheckOutDialog` (Phase 10.4) is the correct place to orchestrate both controllers.

---

## 3. CRITICAL — PRD Mismatch: No Pre-deletion Business Checks (Guest & Room)

### 3.1 Guest Delete — No Soft-Delete / Booking Check

**PRD Reference:** FR-G3 §6.1 (line 345):
> *"Cancel guest account (soft delete if guest has bookings)"*

**Current code** — `GuestController.java:73–75`:
```java
public void deleteGuest(int id) throws DatabaseException {
    guestDAO.delete(id);  // Hard DELETE — no check for bookings
}
```

`GuestDAO.delete()` at `GuestDAO.java:130–142`:
```sql
DELETE FROM guests WHERE guest_id = ?
```

**Impact:** If the guest has reservations, the `ON DELETE RESTRICT` foreign key on `reservations.guest_id` (schema.sql line 88) causes a MySQL constraint violation. The `SQLException` gets wrapped in a `DatabaseException`, and the user sees an unhelpful error: *"Failed to delete guest: Cannot delete or update a parent row: a foreign key constraint fails"*. No graceful messaging, no soft-delete fallback.

**PRD requirement:** "Soft delete if guest has bookings" implies the controller should check whether bookings exist first:
- **No bookings** → hard DELETE (simple removal)
- **Has bookings** → soft-delete (mark inactive, keep record for history)

### 3.2 Room Delete — No Active Reservation Check

**PRD Reference:** FR-R1 §6.2 (line 352):
> *"Delete rooms (only if no active reservations)"*

**Current code** — `RoomController.java:59–61`:
```java
public void deleteRoom(int id) throws DatabaseException {
    roomDAO.delete(id);  // Hard DELETE — no check for reservations
}
```

**Impact:** Same FK constraint failure as Guest. The `ON DELETE RESTRICT` on `reservations.room_id` (schema.sql line 89) causes an opaque `DatabaseException` if the room has any reservations (past or present).

**PRD requirement:** "Only if no active reservations" implies the controller should:
1. Check for reservations with status `pending`, `confirmed`, or `checked_in`
2. Only proceed with deletion if none exist
3. Return a clear error message if active reservations are found

### Combined Root Cause

Both controllers were implemented as thin CRUD wrappers over their DAOs. The plan's task descriptions (5.1: "Orchestrate guest CRUD", 5.2: "Orchestrate room CRUD") did not explicitly list "check for active reservations before delete" as a subtask. The plan's Phase 3 dependency column for GuestDAO (line 58) was also corrected from `delete (soft)` to `delete (hard)` in Issue 005, which may have signaled to the implementer that soft-delete was intentionally dropped — but the PRD still requires it.

### Cascade Effects of Fixing

**For Guest (soft-delete):**
- **Schema:** `guests` table needs `is_active BOOLEAN DEFAULT TRUE` column — this is a Phase 1 schema migration (already finalized). Issue 005 §3.2 already identified this and chose Option B (update plan to say "hard delete").
- **DAO:** `GuestDAO` needs a new method `getReservationsByGuestId()` or a `hasActiveReservations()` check. But `GuestDAO` currently has no `ReservationDAO` dependency — it would need one or the check must be in the controller.
- **Controller:** `GuestController.deleteGuest()` needs to choose between soft-delete (update is_active=false) and hard-delete (no reservations exist). This adds branching logic.
- **Re-adding `is_active`:** If we add `is_active`, all `getAll()` queries in GuestDAO must filter by `is_active = TRUE` (unless an "include inactive" option is added), else deleted guests would still appear in results.
- **Cascade to Phase 8:** GuestPanel UI would need to handle the inactive state (gray out, show "deactivated" badge).

**For Room (active-reservation check):**
- **Controller dependency change:** `RoomController` currently only depends on `RoomDAO` (3.2). To check active reservations, it needs `ReservationDAO` or a new helper. This changes the controller's architectural dependency footprint.
- **Plan update:** Task 5.2 dependencies would need to add `3.3 (ReservationDAO)`.
- **Ambiguity:** Check for *any* reservation or *only active* ones? "Active" means `pending`, `confirmed`, `checked_in`. The PRD says "only if no active reservations" — past reservations (checked_out, cancelled) should not block deletion. But `ON DELETE RESTRICT` doesn't distinguish — it blocks deletion if *any* FK reference exists, even historical ones. The controller check would be *more permissive* than the DB constraint, which means even after a controller check passes, a historical FK reference could still block the actual DELETE. The controller would need to delete dependent rows first or the check must check for *any* FK existence.

---

## 4. MODERATE — PRD Mismatch: Cancel Reservation Has No Reason Parameter

**PRD Reference:** FR-RES5 §6.3 (line 398):
> *"Allow cancellation with reason"*

**Current code** — `ReservationController.java:122`:
```java
public void cancelReservation(int reservationId)
        throws ValidationException, DatabaseException {
```

**The problem:** The method signature takes only `reservationId`. There is no `String reason` parameter. The underlying `ReservationDAO.updateStatus()` at `ReservationDAO.java:259–273` only updates the status column:

```java
public void updateStatus(int reservationId, String status) throws DatabaseException {
    String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
    // ...
}
```

No cancellation reason is persisted.

### Impact

| Concern | Detail |
|---------|--------|
| **PRD compliance** | FR-RES5 explicitly requires cancellation with reason — not implemented |
| **Audit trail** | No historical record of why a reservation was cancelled |
| **Phase 10.5 UI** | Cancellation dialog would need to collect reason but has nowhere to store it |
| **Reporting** | Phase 14 reports cannot show cancellation analysis (reasons, patterns) |

### Fix Options

**Option A — Store reason in DB:**
1. Add `cancellation_reason TEXT` column to `reservations` table (nullable, only set when cancelled)
2. Add `cancellationReason` field to `Reservation.java` model
3. Update `ReservationDAO.updateStatus()` to accept optional reason, or add `updateStatusWithReason()`
4. Update `mapResultSetToReservation()` to read the column
5. Update `ReservationController.cancelReservation(int, String)` to accept and pass reason

**Option B — In-memory only:**
Accept reason in the controller but don't persist it. Lost on app restart. Does not satisfy PRD requirement for audit trail.

**Recommendation:** Option A.

### Cascade Effects

- **Schema migration:** Adding `cancellation_reason TEXT` requires ALTER TABLE on `reservations`, already finalized in Phase 1.
- **Reservation model:** The new field must be added to both constructors, a new getter added. This adds complexity to the model (another nullable field).
- **ReservationDAO:**
  - `save()` — would need to pass null for new reservations (not cancelled yet)
  - `update()` — would need to include the field
  - `updateStatus()` — currently a focused status-only method. Adding reason would make it `updateStatusWithReason()` or we overload it.
  - `mapResultSetToReservation()` — read the nullable TEXT column
- **SELECT_JOIN constant** in `ReservationDAO` — already has a large join query; adding the column is trivial.
- **seed.sql:** Would need cancellation reasons for any sample cancelled reservations.

---

## 5. MODERATE — PRD/Schema Contradiction: No `paymentMethod` on Billing (Reversion of Issue 001)

**PRD Reference:** FR-B2 §6.4 (line 415):
> *"Record payment method (Cash, Card, Bank Transfer)"*

**Current schema** — `billing` table (`schema.sql` lines 100–115): No `payment_method` column.

**Current model** — `Billing.java`: No `paymentMethod` field.

**Current controller** — `BillingController.java:80–93`:
```java
public void recordPayment(int billingId, String paymentStatus)
        throws ValidationException, DatabaseException {
    // No paymentMethod parameter
```

### History: Issue 001 Reversion

This is a **reversion** of Issue 001 (`issue-001-billing-model-schema-mismatch.md`). In Issue 001, the `paymentMethod` field was identified as a "phantom field" (lines 61–62):

> *"Old Field | Why Wrong | `String paymentMethod` | No `payment_method` column in schema"*

And was removed from `Billing.java`. But the PRD (which is the higher-authority document) requires payment method recording. The schema was designed without it, and the model was corrected to match the schema — but this means the implementation matches the schema at the cost of contradicting the PRD.

### The Constraint

The `billing` table in `schema.sql` uses `ENUM('pending', 'partial', 'paid', 'refunded')` for `payment_status` — there is no room to encode payment method here. A separate column is needed.

### Impact

| Concern | Detail |
|---------|--------|
| **PRD compliance** | FR-B2 requirement explicitly missing — cannot record how payment was made |
| **Phase 11.2 UI** | Payment dialog would need a payment method dropdown but has no DB column to store it |
| **Reporting** | Cannot report revenue by payment method (cash vs card vs bank transfer) |
| **Seed data** | `seed.sql` sample payments cannot include payment method |

### Fix Options

**Option A — Add schema column (proper fix):**
1. Add `payment_method VARCHAR(50)` column to `billing` table (nullable, for backward compatibility with existing records)
2. Add `paymentMethod` field to `Billing.java` model
3. Update `BillingDAO.save()`, `updatePaymentStatus()`, `mapResultSetToBilling()`
4. Update `BillingController.recordPayment()` to accept `String paymentMethod`
5. Add `PAYMENT_METHOD_CASH`, `PAYMENT_METHOD_CARD`, `PAYMENT_METHOD_BANK_TRANSFER` constants

**Option B — Defer:** Accept that payment method recording is not in scope for the current schema. Update PRD to mark this as deferred.

### Cascade Effects (Option A)

- **Schema migration:** Adding `payment_method VARCHAR(50)` to `billing` — this is a Phase 1 schema change.
- **Billing model:** New field in both constructors, new getter. This increases constructor parameter count (currently 9, would become 10).
- **BillingDAO:**
  - `save()` INSERT query — add `payment_method` column and parameter
  - `updatePaymentStatus()` — currently only updates `payment_status` and `payment_date`. Would need a companion `recordPayment()` that sets all three.
  - `mapResultSetToBilling()` — read the column
- **BillingController.recordPayment():** Signature becomes `recordPayment(int billingId, String paymentStatus, String paymentMethod)`. Parameter count increases.
- **This reverts Issue 001's removal of `paymentMethod`.** The issue file must be updated to note the reversion and its rationale (PRD overrides schema-initial-design).

---

## 6. MODERATE — code_rules Contradiction: Exception-Handling Pattern Not Followed

**code_rules Reference:** §9.2 (lines 856–891):
> *"Controllers show error dialogs via showErrorDialog()"*

### The Pattern in code_rules

```java
// §9.2 example (lines 860–891)
public void createReservation(...) {
    try {
        // business logic
    } catch (ValidationException e) {
        showErrorDialog("Validation Error", e.getMessage());    // line 883
    } catch (ReservationException e) {
        showErrorDialog("Reservation Error", e.getMessage());   // line 885
    } catch (DatabaseException e) {
        showErrorDialog("Database Error", "Failed to save..."); // line 887
    }
}
```

### The Actual Implementation

All 6 controllers throw exceptions to the caller (view layer) instead of handling them with `showErrorDialog()`:

```java
// Actual pattern — e.g., GuestController.java:21–29
public Guest createGuest(Guest guest) throws ValidationException, DatabaseException {
    validateGuest(guest);
    if (guestDAO.existsByEmail(guest.getEmail())) {
        throw new ValidationException("Email already registered");
    }
    return guestDAO.save(guest);
}
```

No controller has a try-catch block that calls `showErrorDialog()`. All exceptions propagate upward.

### Analysis

This is not necessarily wrong — it is a **different architectural choice**. Two valid MVC approaches:

| Approach | code_rules §9.2 | Actual Code |
|----------|-----------------|-------------|
| **Where errors are handled** | Controller layer | View layer |
| **Controller signature** | `void` (void return, side-effect only) | `throws XxxException` (exceptions propagate) |
| **Dialog responsibility** | Controller calls `showErrorDialog()` | View catches exception and shows dialog |
| **Testability** | Controller harder to unit-test (UI coupling) | Controller easier to unit-test (no UI dependency) |

The actual code follows a **cleaner MVC separation** — controllers are pure business logic, views handle presentation concerns (error dialogs). The code_rules pattern couples controllers to Swing dialog methods, which is a testability anti-pattern.

### Fix Options

**Option A — Update code_rules (recommended):**
Update §9.2 to reflect the actual pattern: controllers throw typed exceptions, views catch and display dialogs. This is the architecturally superior approach.

**Option B — Rewrite controllers:**
Add try-catch-showErrorDialog to every controller method. Adversely impacts testability and adds ~30 lines of boilerplate per controller.

**Recommendation:** Option A. The actual code has a better architecture than the documented pattern.

---

## 7. MODERATE — code_rules Violation: Zero JavaDoc on All 6 Controllers (§10.1)

**code_rules Reference:** §10.1 (lines 945–981):
> *"JavaDoc for public methods and public classes only"*

### Current State

| Controller | Public Methods | With JavaDoc | Gap |
|------------|---------------|-------------|-----|
| `GuestController.java` | 7 (incl. constructor) | 0 | 7 |
| `RoomController.java` | 8 (incl. constructor) | 0 | 8 |
| `ReservationController.java` | 12 (incl. constructor + addObserver/removeObserver) | 0 | 12 |
| `BillingController.java` | 6 (incl. constructor) | 0 | 6 |
| `StaffController.java` | 11 (incl. constructor) | 0 | 11 |
| `ServiceController.java` | 10 (incl. constructor) | 0 | 10 |
| **Total** | **54** | **0** | **54** |

### Impact

- **IDE auto-complete:** No documentation tooltips when Phase 7–13 UI panels call controller methods
- **Code review:** Reviewers must read method bodies to understand contracts
- **Maintainability:** New developers cannot distinguish between validation-behavior and simple delegation without reading each method
- **Phase 15.7:** "Verify JavaDoc on public methods" — this will fail

### Cascade Effects

Adding JavaDoc is purely additive — no strategic decisions needed. Each method needs:
- `@param` for every parameter
- `@return` for non-void methods
- `@throws` for every declared exception
- A one-line description of what the method does

This follows the exact template shown in code_rules §11.2 (§§1103–1121) and §11.3 (§§1158–1188).

### Additional Note

The `ReservationController` methods `addObserver()` and `removeObserver()` at lines 31–39 also lack JavaDoc. These are public API methods for the Observer pattern — documenting their contract (what happens when observer is null) is important.

---

## 8. MODERATE — Plan Mismatch: No Receipt Data Structure in BillingController

**Plan Reference:** Phase 5, Task 5.4 (line 96):
> *"Bill generation (room+service+tax), payment recording, adjustments, receipt data"*

**Current code** — `BillingController.java`: No receipt-related code exists. No receipt generation, receipt data structure, or receipt storage.

**PRD Reference:** FR-B2 §6.4 (line 418):
> *"Generate payment receipts"*

**PRD Reference:** §3.1.4 (line 60):
> *"Receipt generation"*

### Impact

- **Plan deliverable incomplete:** "receipt data" is listed in the task description but not implemented
- **PRD requirement missing:** Receipts are an in-scope feature per §3.1.4
- **Phase 11 UI dependency:** BillingPanel and payment dialog (11.1, 11.2) may need receipt data to display

### Analysis

Receipt generation could mean different things:
1. **A structured data object** (e.g., `Receipt` class) holding itemized charges, payment details, and metadata
2. **A JasperReports template** for printable receipts (Phase 14 scope)
3. **An in-app dialog** showing a receipt summary after payment

The plan likely intended (1) — a receipt data structure that feeds into the Phase 11 UI and Phase 14 reports. Neither exists.

### Fix Options

**Option A — Simple `Receipt` model class:**
Create `hms.model.Receipt` with fields: receiptId, billing (Billing), generatedAt, itemizedCharges (List), paymentMethod, receiptNumber. Controllers generate it from Billing data. This is a new model class — no schema changes needed (receipt is computed from billing data).

**Option B — JasperReports-based:**
Defer to Phase 14 reporting module. The Jasper report templates (Guest Invoice Report) effectively serve as receipts. Update the plan task description to clarify.

**Recommendation:** Option A (lightweight). A `Receipt` model class that pulls data from `Billing` and formats it for display is low-effort and unblocks Phase 11 UI.

---

## 9. LIMITATIONS — Known/Deferred

### 9.1 Bill Adjustments Blocked (No BillingDAO.update())

**Plan Reference:** Phase 5, Task 5.4 (line 96):
> *"adjustments"*

**PRD Reference:** FR-B3 §6.4 (lines 421–424):
> *"Add discounts, late charges, complimentary charges, update billing notes"*

**Current state:** `BillingDAO` has no general `update()` method. Only `updatePaymentStatus()` exists at `BillingDAO.java:152–173`. The only way to adjust a billing record is through the payment status, which cannot modify charges, discounts, or notes.

**This was already identified** in the task checklist for Phase 5.4 as a DAO-level blocker.

**Cascade:** Adding a `BillingDAO.update(Billing)` method requires implementing it across the full JOIN query pattern (matching `mapResultSetToBilling`), which is sizable. It's not a controller-level fix.

### 9.2 Modification History Not Tracked (PRD FR-RES2)

**PRD Reference:** FR-RES2 §6.3 (line 381):
> *"Track modification history"*

**Current state:** No audit table, no changelog, no versioning on reservations. `ReservationDAO.update()` overwrites the record entirely. History is lost.

**Cascade:** This would require a new `reservation_history` table, a new model class (`ReservationHistory`), a new DAO, and controller integration. This is a significant feature addition beyond the current scope.

### 9.3 Staff Authentication/Login Deferred (Known/Acceptable)

**PRD Reference:** §3.2 (line 107):
> *"Staff authentication/login system (not required for coursework)"*

**Current state:** `StaffController` has `PasswordUtil.hashPassword()` at line 34, storing hashed passwords. But there is no login method, no session management. The infrastructure for future authentication is laid but unused.

This is in-scope only for hashing (PRD FR-ST1 line 448: "Store password hash"), not for the login workflow. Acceptable.

### 9.4 FK Constraint Failure Risks

**Controllers that could trigger opaque constraint errors:**

| Controller | Operation | FK Risk | Child Table |
|------------|-----------|---------|-------------|
| `GuestController.deleteGuest()` | DELETE guest | `ON DELETE RESTRICT` | reservations |
| `RoomController.deleteRoom()` | DELETE room | `ON DELETE RESTRICT` | reservations |
| `StaffController.deleteStaff()` | DELETE staff | `ON DELETE RESTRICT` | room_assignments |
| `ServiceController.deleteService()` | DELETE service | `ON DELETE RESTRICT` | service_bookings |

All four produce `DatabaseException` wrapping a MySQL `SQLException` with message: *"Cannot delete or update a parent row: a foreign key constraint fails"*. Users receive no actionable guidance.

---

## 10. MINOR — Observations & Quality

### 10.1 Weak Type/Status Validation in RoomController

`RoomController.createRoom()` and `updateRoom()` call `validateRoom()` which checks `isNotEmpty(roomType)` and `isNotEmpty(status)` but does **not** validate that the values match `Constants.ROOM_TYPE_*` or `Constants.ROOM_STATUS_*`:

```java
// RoomController.java:115–118
if (!ValidationUtil.isNotEmpty(room.getRoomType())) {
    throw new ValidationException("Room type is required");
}
```

A caller could pass `roomType = "INVALID_TYPE"` and it would pass validation. The database `rooms.room_type` column is `VARCHAR(50)` (not ENUM), so MySQL would accept it too. This means invalid room types can enter the system.

**Cascade:** If a Phase 9 UI panel filters by room type (e.g., "Single", "Double", "Suite"), a row with "invalid_type" would not appear in any filter, effectively creating invisible data.

**Fix:** Add explicit matching against `Constants.ROOM_TYPE_SINGLE`, `ROOM_TYPE_DOUBLE`, `ROOM_TYPE_SUITE`, `ROOM_TYPE_DELUXE` in validation. Same for `ROOM_STATUS_*` constants. Or add a `ValidationUtil.isIn(String value, String... validValues)` helper.

### 10.2 6-Parameter Method in ReservationController (code_rules §6.1)

`createReservation()` at line 41–43:
```java
public Reservation createReservation(Guest guest, Room room, LocalDate checkInDate,
                                     LocalDate checkOutDate, int numberOfGuests,
                                     Integer createdByStaffId)
```

**code_rules §6.1 (line 481–497):** Recommends 2–3 parameters maximum, suggests using object parameter for more.

**Impact:** Phase 10.2 (ReservationDialog) will need to assemble 6 individual values. Any reordering breaks callers. Adding more parameters (e.g., pricing strategy, discount) would make this worse.

**Fix:** Replace with `Reservation` object parameter:
```java
public Reservation createReservation(Reservation reservation)
```
The caller constructs the `Reservation` with appropriate values. The controller validates the pre-built object. This is already the pattern used by `updateReservation(reservation)` at line 82.

### 10.3 `StaffController.deleteStaff()` — No Active Assignment Check

Same pattern as Guest/Room deletion. `deleteStaff(int id)` at line 91–93 does not check for active `room_assignments` before deleting. The FK constraint (`ON DELETE RESTRICT` on `room_assignments.staff_id` at schema.sql line 144) will fail with an opaque error if the staff member has assignments.

### 10.4 `ServiceController.deleteService()` — No Active Booking Check

`deleteService(int id)` at line 65–67 does not check for active `service_bookings`. Same FK constraint issue (`schema.sql` line 128).

### 10.5 `ReservationController` — Observer Pattern Null Safety

`addObserver()` at line 31–34 handles null gracefully:
```java
if (observer != null) {
    observers.add(observer);
}
```

However, the notification methods (lines 239–255) do not guard against observer mutations during iteration. If an observer removes itself inside `onReservationCreated()`, a `ConcurrentModificationException` would occur. This is a standard concern with observer iteration over a mutable list, but since the project is single-threaded (Swing EDT), the risk is minimal.

---

## 11. Cross-cutting: Cascade Effects of Fixes

This section maps each fix to the full list of files/layers affected, enabling a decision on fix order priority.

### 11.1 Reservation Display ID (Fix A — Store in DB)

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Schema | `database/schema.sql` | ALTER TABLE reservations ADD display_id VARCHAR(20) |
| Seed | `database/seed.sql` | Add display_id values to sample data |
| Model | `src/hms/model/Reservation.java` | New field + both constructor variants + getter |
| DAO | `src/hms/dao/ReservationDAO.java` | `save()` inserts display_id; `mapResultSetToReservation()` reads it; SELECT_JOIN includes it |
| Controller | `src/hms/controller/ReservationController.java` | Call `StringUtil.generateReservationId()` in `createReservation()` |
| Tests | (none) | Pure additive, no regression |

**Total files touched:** 5  
**Breakage risk:** Low — additive change, all existing data has `display_id = NULL` (acceptable for old records)

### 11.2 Plan Contradiction: Remove BillingDAO from ReservationController Dependencies

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Plan | `plan/README.md` line 95 | Remove `3.4` from dependency list |

**Total files touched:** 1  
**Breakage risk:** None — pure documentation correction

### 11.3 Guest Soft-Delete / Room Active-Reservation Check

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Schema | `database/schema.sql` | Add `is_active BOOLEAN DEFAULT TRUE` to `guests` |
| Seed | `database/seed.sql` | Set `is_active` on sample data |
| Model | `src/hms/model/Guest.java` | New field + both constructors + getter |
| DAO | `src/hms/dao/GuestDAO.java` | `delete()` checks bookings → soft-delete; `getAll()` filters by `is_active = TRUE` |
| Controller | `src/hms/controller/GuestController.java` | `deleteGuest()` branches on booking existence |
| Controller | `src/hms/controller/RoomController.java` | New dependency on ReservationDAO to check active reservations |
| Plan | `plan/README.md` lines 58, 93 | Revert Issue 005's fix (change `delete (hard)` → back to business rule) |
| **New dependency** | RoomController now needs ReservationDAO | Architectural change |

**Total files touched:** 7+  
**Breakage risk:** Medium — filtering `getAll()` by `is_active` changes behavior for all Guest listings. RoomController dependency change is architectural.

### 11.4 Cancel Reservation with Reason

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Schema | `database/schema.sql` | ALTER TABLE reservations ADD cancellation_reason TEXT |
| Seed | `database/seed.sql` | Add cancellation_reason to sample cancelled reservations |
| Model | `src/hms/model/Reservation.java` | New field + both constructor variants + getter |
| DAO | `src/hms/dao/ReservationDAO.java` | `updateStatus()` extended with reason param (overload); SELECT_JOIN reads it |
| Controller | `src/hms/controller/ReservationController.java` | `cancelReservation(int, String)` signature change |

**Total files touched:** 5  
**Breakage risk:** Medium — signature change breaks any code that calls `cancelReservation(int)` (currently none outside this controller). Schema migration is additive.

### 11.5 Payment Method on Billing (Issue 001 Reversion)

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Schema | `database/schema.sql` | ALTER TABLE billing ADD payment_method VARCHAR(50) |
| Seed | `database/seed.sql` | Add payment_method to sample billing data |
| Model | `src/hms/model/Billing.java` | New field + both constructors + getter (re-adds what Issue 001 removed) |
| DAO | `src/hms/dao/BillingDAO.java` | `save()` inserts; `updatePaymentStatus()` optionally sets; mapResultSetToBilling reads |
| Controller | `src/hms/controller/BillingController.java` | `recordPayment()` gets new param |
| Plan | `plan/issues/issue-001-billing-model-schema-mismatch.md` | Add note about reversion with rationale |

**Total files touched:** 6  
**Breakage risk:** Medium — reverts a previous fix. Constructor parameter count increases (currently 9 → 10 for new-record constructor, same for full constructor).

### 11.6 code_rules §9.2 Pattern Update

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Docs | `CONTEXT/code_rules.md` §9.2 | Rewrite to show throw-to-view pattern instead of showErrorDialog |

**Total files touched:** 1  
**Breakage risk:** None — pure documentation correction

### 11.7 JavaDoc on Controllers

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Docs | All 6 controller `.java` files | Add 54 JavaDoc blocks |

**Total files touched:** 6  
**Breakage risk:** None — pure additive documentation

### 11.8 Receipt Data Structure

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Model | `src/hms/model/Receipt.java` | **New file** with fields: receiptId, billing, generatedAt, itemizedCharges |
| Controller | `src/hms/controller/BillingController.java` | New `generateReceipt(int billingId)` method |

**Total files touched:** 2 (1 new file)  
**Breakage risk:** Low — purely additive

### 11.9 RoomController Type Validation

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Controller | `src/hms/controller/RoomController.java` | Add type/status enum validation |
| Util (optional) | `src/hms/util/ValidationUtil.java` | Add `isIn(value, validValues...)` helper (if desired) |

**Total files touched:** 1–2  
**Breakage risk:** None — validation becomes stricter

### 11.10 6-Parameter Method Refactor

| Layer | File(s) Affected | Change Type |
|-------|------------------|-------------|
| Controller | `src/hms/controller/ReservationController.java` | Change `createReservation(6 params)` → `createReservation(Reservation)` |

**Total files touched:** 1  
**Breakage risk:** Low — but any caller (e.g., Phase 10.2 ReservationDialog) that uses the 6-param signature would break. Since no view layer exists yet (Phase 10 is not started), this is safe to change now.

---

## 12. Summary

| # | Severity | Item | Requires Schema Change? | Architectural Impact |
|---|----------|------|------------------------|---------------------|
| 1 | **BUG** | Reservation display ID not generated | Yes | Model + DAO changes |
| 2 | **CONTRADICTION** | Plan says BillingDAO dependency; not used | No | Documentation only |
| 3 | **MISMATCH** | No soft-delete / booking check on Guest delete | Yes (is_active) | Model + DAO + Controller |
| 4 | **MISMATCH** | No booking check on Room delete | No | Controller needs new dependency |
| 5 | **MISMATCH** | Cancel reservation no reason param | Yes (cancellation_reason) | Model + DAO + Controller |
| 6 | **MISMATCH** | No paymentMethod field on Billing | Yes (payment_method) | Reverts Issue 001 |
| 7 | **CONTRADICTION** | Exception pattern != code_rules §9.2 | No | Documentation only |
| 8 | **code_rules** | Zero JavaDoc on 54 methods | No | Additive only |
| 9 | **MISMATCH** | No receipt data structure | No | New model class |
| 10 | **LIMITATION** | Bill adjustments blocked (DAO) | No | Requires DAO work |
| 11 | **LIMITATION** | Modification history not tracked | Yes | New table + model + DAO |
| 12 | **MINOR** | Weak type validation on Room | No | Controller only |
| 13 | **MINOR** | 6-parameter method in ReservationController | No | Method signature change |

### Categorization by Resolution Path

| Resolution Path | Items |
|----------------|-------|
| **Documentation only** (fix plan/code_rules) | 2, 7 |
| **Pure additive** (no schema, no regression) | 8, 9, 12 |
| **Method signature change** (no schema) | 13 |
| **Schema migration required** | 1, 3, 5, 6, 11 |
| **Architectural dependency change** | 4 |
| **Blocked by DAO** | 10 |

---

## 13. Recommended Fix Order

| Order | Item | Effort | Risk | Impact |
|-------|------|--------|------|--------|
| 1 | **7** — Update code_rules §9.2 exception pattern | 5 min | None | Documentation accuracy |
| 2 | **2** — Fix plan dependency list for ReservationController | 1 min | None | Plan accuracy |
| 3 | **8** — Add JavaDoc to all 54 controller methods | 1–2 hrs | None | Compliance + developer experience |
| 4 | **12** — Add type/status validation to RoomController | 15 min | None | Data integrity |
| 5 | **13** — Refactor 6-param method to Reservation object | 10 min | Low (no callers yet) | Code quality |
| 6 | **9** — Create Receipt data structure | 30 min | Low | Unblocks Phase 11 |
| 7 | **1** — Reservation display ID (Option A: store in DB) | 1–2 hrs | Low | PRD compliance |
| 8 | **5** — Cancel with reason (Option A: store in DB) | 1–2 hrs | Medium | PRD compliance |
| 9 | **3 + 4** — Guest soft-delete + Room booking check | 2–3 hrs | Medium | PRD compliance + UX |
| 10 | **6** — Payment method on Billing (revert Issue 001) | 1–2 hrs | Medium | PRD compliance |
| 11 | **10** — Bill adjustments (BillingDAO.update) | 2–4 hrs | Medium | Blocked at DAO layer |
| 12 | **11** — Modification history | 4–8 hrs | High | New table + model + DAO |

### Priority Rationale

The first 6 items are low-risk, additive, or documentation-only changes that improve code quality without touching the DB schema. Items 1, 5, 6 all require schema migrations (Phase 1 is already finalized) — these should be done as a batch to minimize migration iterations. Items 3+4 and 10+11 are progressively higher effort and risk and could be deferred.

---

## 14. Verification

- [ ] Fix 1 — Reservation display ID: schema migration + model + DAO + controller
- [ ] Fix 2 — Plan dependency documentation update
- [ ] Fix 3 — Guest soft-delete with booking check
- [ ] Fix 4 — Room delete with active-reservation check
- [ ] Fix 5 — Cancel reservation with reason
- [ ] Fix 6 — Payment method on Billing
- [ ] Fix 7 — Update code_rules §9.2
- [ ] Fix 8 — JavaDoc on all 6 controllers (54 methods)
- [ ] Fix 9 — Receipt data structure
- [ ] Fix 12 — Room type/status validation
- [ ] Fix 13 — Refactor 6-param method
- [ ] Clean & Build — *BUILD SUCCESSFUL*

---

## 15. Lessons Learned

1. **Controller implementation must cross-reference plan/README.md dependency columns**, not just task descriptions. The BillingDAO gap was invisible from reading the task text alone.
2. **PRD functional requirements must be checked before considering a controller "complete"** — the PRD contains subtle requirements (cancel reason, payment method, soft-delete) that are not in the plan's task descriptions.
3. **Schema-first design creates tension with PRD requirements** — when the schema was designed in Phase 1 without `payment_method`, and Issue 001 then removed it from the model to match the schema, the PRD requirement was silently dropped. The higher-authority document (PRD) must be re-checked when schema-model mismatches are resolved.
4. **Fix cascade awareness is critical** — adding `is_active` for soft-delete changes the behavior of `getAll()` for every caller. A seemingly small schema addition can have broad query impact.
5. **JavaDoc is the most frequently violated code_rules requirement** — across Issues 004 (utilities), 005 (models), and now 006 (controllers), the same §10.1 violation recurs. Consider adding a pre-commit or build-time check.
