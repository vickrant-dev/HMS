# Issue 005: Phase 0–4 Completion Audit — Gaps vs Plan & CONTEXT

**Status:** Resolved (all 8 findings fixed)
**Date:** 2026-06-17  
**Found in:** Cross-phase audit (Phase 0–4 completion review)  
**Root Cause:** Plan drift — the `plan/README.md` and `code_rules.md` were not kept synchronized with implementation decisions made during development, and some deliverables were deferred or partially completed.

---

## Files Audited (45)

| Phase | Files | Scope |
|-------|-------|-------|
| 0 | 9 | lib/, build config, Constants, exceptions, DatabaseConnection, HotelManagementApp, MainWindow |
| 1 | 2 | schema.sql, seed.sql |
| 2 | 8 | Guest, Room, Reservation, Billing, Staff, Service, ServiceBooking, RoomAssignment |
| 3 | 8 | GuestDAO, RoomDAO, ReservationDAO, BillingDAO, StaffDAO, ServiceDAO, ServiceBookingDAO, RoomAssignmentDAO |
| 4 | 10 | ValidationUtil, DateUtil, StringUtil, PasswordUtil, IconUtil (5) + PricingStrategy, NormalPricingStrategy, SeasonalPricingStrategy, CorporatePricingStrategy, ReportUtil (pending) (5) |
| Follow-up | 3 | code_rules.md, PRD.md, plan/README.md |

---

## 1. CRITICAL — Blocking

### 1.1 JasperReports 7.0.6 JAR missing from `lib/`

**Plan reference:** Phase 0, Task 0.2 (line 13)
> *"Place JARs: FlatLaf 3.5.1, MySQL Connector 9.7.0, JasperReports 7.0.6, jBCrypt"*

**Current state:**
```
lib/
├── flatlaf-3.5.1.jar            ✅ Present
├── mysql-connector-j-9.7.0.jar  ✅ Present
├── jbcrypt-0.4.jar              ✅ Present
├── jasperreports-7.0.6.jar      ✅ Present (from JasperStudio)
├── jasperreports-pdf-7.0.6.jar  ✅ Present
├── jasperreports-fonts-7.0.6.jar ✅ Present
├── commons-collections4-4.5.0.jar ✅ Present
├── commons-logging-1.3.5.jar    ✅ Present
├── commons-beanutils-1.11.0.jar ✅ Present
└── commons-digester-2.1.jar     ✅ Present
```

**Impact (resolved):** Phase 4.7 (ReportUtil.java) can now be implemented. Phase 14 reports are unblocked.

**Fix:** Copied `jasperreports-7.0.6.jar` (and transitive deps: `commons-collections4`, `commons-logging`, `commons-beanutils`, `commons-digester`) from JasperStudio installation at `%USERPROFILE%\Downloads\js-studiocomm_7.0.6_windows_x86_64`. Note: iText is NOT required — JasperReports 7.0.6 uses `jasperreports-pdf-7.0.6.jar` for PDF export instead.

---

## 2. MODERATE — Spec Compliance

### 2.1 Zero JavaDoc on all 8 model classes (Phase 2 deliverable)

**Plan reference:** Phase 2 deliverable (line 50)
> *"8 immutable POJO classes following code_rules.md standards (private final fields, getters, no setters, **JavaDoc on constructors/getters**)."*

**Code_rules §10.1 (line 946):**
> *"JavaDoc for public methods and public classes only"*

**Current state:** All 8 model classes (`Guest.java`, `Room.java`, `Reservation.java`, `Billing.java`, `Staff.java`, `Service.java`, `ServiceBooking.java`, `RoomAssignment.java`) have **zero** JavaDoc comments.

| Model | Constructors | Getters | With JavaDoc |
|-------|-------------|---------|-------------|
| Guest | 2 | 10 | 0 |
| Room | 2 | 8 | 0 |
| Reservation | 2 | 13 | 0 |
| Billing | 2 | 11 | 0 |
| Staff | 2 | 12 | 0 |
| Service | 2 | 7 | 0 |
| ServiceBooking | 2 | 9 | 0 |
| RoomAssignment | 2 | 9 | 0 |
| **Total** | **16** | **79** | **0** |

**Impact:** Code review difficulty — constructors with many parameters have no documentation of parameter meaning. IDEs show no tooltips on these constructors during Phase 5 (Controller) development.

**Fix:** Add JavaDoc to 16 constructors and 79 getters across 8 files. Constructor JavaDoc should document each parameter (e.g., `@param firstName The guest's first name`). Getter JavaDoc can use the simple form per §11.3 (e.g., `/** Returns the guest's first name. */`).

---

## 3. MINOR — Plan Inaccuracy

### 3.1 Plan states root package `ead_cw`, actual is `hms`

**Plan reference:** Key Architectural Decisions (line 204)
> *"Root package: `ead_cw` with sub-packages per layer"*

**Actual:** Root package is `hms` (e.g., `hms.model`, `hms.dao`, `hms.config`, etc.)

**code_rules.md reference:** Consistently uses `com.hotelms.*` throughout all examples (§§4.1, 7.1, 11.2, 11.3, 12.1, 12.2, 12.4).

**History:** This was a deliberate rename in early Phase 0 to match the `ead_cw` project folder name while using cleaner package names. The plan (`plan/README.md`) and `code_rules.md` were never updated to reflect this.

**Impact:** None on functionality. Future developers reading the plan alongside code will see a mismatch.

**Fix:** Update `plan/README.md` line 204 and `code_rules.md` §4.1 examples to reflect `hms.*`.

---

### 3.2 Plan says `GuestDAO.delete` = "soft delete" — code has hard DELETE

**Plan reference:** Phase 3, Task 3.1 (line 58)
> *"CRUD: save, getById, getAll, update, **delete (soft)** , searchByName/Email/Phone"*

**Actual:** `GuestDAO.delete(int id)` executes:
```sql
DELETE FROM guests WHERE guest_id = ?
```

This is a **hard delete**. The `guests` table in `schema.sql` has no `is_active` or `deleted_at` column to support soft deletion.

**Impact:** If a guest has dependent rows (reservations, billing, etc.), the `ON DELETE RESTRICT` foreign key constraint prevents deletion. The calling controller receives `DatabaseException` instead of a graceful "guest deactivated" message. This is functional but different from the plan's stated design.

**Fix (two options):**
- **Option A (code):** Add `is_active BOOLEAN DEFAULT TRUE` column to `guests` table and change `delete()` to `UPDATE guests SET is_active = FALSE WHERE guest_id = ?`. Requires schema migration.
- **Option B (plan):** Update the plan to say `delete (hard)` instead of `delete (soft)` — the current behavior is correct for the existing schema.

**Recommendation:** Option B — the schema has no soft-delete column, and adding one would require a schema change in Phase 1 which is already finalized.

---

### 3.3 `code_rules.md` uses `com.hotelms.*` — codebase uses `hms.*`

**code_rules.md** every code example uses `com.hotelms.*` package references:
| Section | Example Reference | Actual Package |
|---------|------------------|---------------|
| §4.1 (line 197+) | `com.hotelms.config.Constants` | `hms.config.Constants` |
| §4.1 (line 248+) | `com.hotelms.util.ValidationUtil` | `hms.util.ValidationUtil` |
| §11.2 (line 1098) | `com.hotelms.util.PasswordUtil` | `hms.util.PasswordUtil` |
| §11.3 (line 1155) | `com.hotelms.util.ValidationUtil` | `hms.util.ValidationUtil` |
| §12.1 (line 1228) | `com.hotelms.database.DatabaseConnection` | `hms.database.DatabaseConnection` |
| §12.2 (line 1337) | `com.hotelms.dao.ReservationDAO` | `hms.dao.ReservationDAO` |

**Impact:** New developers following `code_rules.md` examples would create files in the wrong package. This is a documentation hygiene issue.

**Fix:** Global find-and-replace `com.hotelms` → `hms` in `code_rules.md`. Or update the plan's Key Architectural Decisions to acknowledge the deviation.

---

### 3.4 Plan states JasperReports in `lib/` at Phase 0 (Task 0.2) but PRD implies Phase 14

**PRD reference:** No explicit mention of lib setup timing for JasperReports.

**Plan inconsistency:**
- Task 0.2 says: *"Place JARs: ... JasperReports 7.0.6"* — implying Phase 0 action
- Phase 14.1 says: *"Place `.jrxml` report templates in `reports/`, compile to `.jasper`"* — implying Phase 14 setup
- Phase 4.7 depends on: *"JasperReports lib"* — implying it should exist by Phase 4

The JAR cannot be in `lib/` at Phase 0 if it hasn't been downloaded yet. The plan should explicitly acknowledge the JAR will be added when reports are developed (Phase 14), not in Phase 0.

**Fix:** Update Task 0.2 to remove JasperReports from the list (or mark it `[deferred to Phase 14]`).

---

## 4. Summary

| # | Severity | Item | Status |
|---|----------|------|--------|
| 1.1 | **CRITICAL** | JasperReports 7.0.6 JAR missing | **Fixed** |
| 2.1 | **MODERATE** | Zero JavaDoc on 16 constructors + 79 getters across 8 models | **Fixed** |
| 3.1 | **MINOR** | Plan says root package `ead_cw`, actual is `hms` | **Fixed** |
| 3.2 | **MINOR** | Plan says `GuestDAO.delete` = soft delete, code has hard delete | **Fixed** |
| 3.3 | **MINOR** | `code_rules.md` uses `com.hotelms.*` everywhere, codebase uses `hms.*` | **Fixed** |
| 3.4 | **MINOR** | JasperReports lib deferred but not documented in plan | **Fixed** |
| 4.1 | **MINOR** | `code_rules.md` uses `com/hotelms/` (slash) in comments, directory tree, and JASPER_REPORT_PATH | **Fixed** |
| 4.2 | **MINOR** | `PRD.md` references Java 21 instead of Java 24 | **Fixed** |

## 5. Recommended Fix Order

| Order | Issue | Effort | Impact |
|-------|-------|--------|--------|
| 1 | 2.1 — Model JavaDoc | ~95 blocks across 8 files | Compliance + Phase 5 IDE developer experience |
| 2 | 3.3 — Fix `code_rules.md` package names | Global replace `com.hotelms` → `hms` | Documentation accuracy |
| 3 | 3.1 — Fix plan package name | 1 line in plan/README.md | Plan accuracy |
| 4 | 3.2 — Fix plan soft-delete claim | 1 word in plan/README.md | Plan accuracy |
| 5 | 3.4 — Mark JasperReports deferred | 1 line in plan/README.md | Plan accuracy |
| 6 | 1.1 — Download JasperReports | Download JAR + transitive deps | Unblock Phase 4.7, 14 |
| 7 | 4.1 — Fix `com/hotelms/` (slash) in code_rules.md | 14 occurrences across comments and directory tree | Documentation accuracy |
| 8 | 4.2 — Fix Java version in PRD.md | 2 occurrences in PRD.md | Documentation accuracy |

## 6. Verification

- [x] Fix 2.1 — JavaDoc on all 8 model classes (16 constructors + 79 getters)
- [x] Fix 3.3 — Replace `com.hotelms` with `hms` in `code_rules.md`
- [x] Fix 3.1 — Update `plan/README.md` line 204: `ead_cw` → `hms`
- [x] Fix 3.2 — Update `plan/README.md` line 58: `delete (soft)` → `delete (hard)`
- [x] Fix 3.4 — Mark JasperReports as deferred in Task 0.2
- [x] Fix 1.1 — Download JasperReports 7.0.6 + transitive deps into `lib/`
- [x] Fix 4.1 — Replace `com/hotelms/` (slash) with `hms/` in code_rules.md
- [x] Fix 4.2 — Update Java 21 → Java 24 in PRD.md
- [x] Clean & Build

## 7. Follow-up Audit (2026-06-18)

After resolving issues 1.1–3.4, a follow-up audit of CONTEXT (code_rules.md, PRD.md) and plan/README.md found additional stale references:

### 7.1 `code_rules.md` — 14 stale `com/hotelms/` (slash-separated) references

The original global replace `com.hotelms`→`hms` only caught DOT-separated Java package references. Slash-separated file path references in comments and the directory tree were missed:

| Section | Line(s) | Before | After |
|---------|---------|--------|-------|
| §4.1 directory tree | 35-36 | `com/` → `hotelms/` (2 levels) | `hms/` (1 level) |
| §4.1 Constants example | 549 | `// com/hotelms/config/Constants.java` | `// hms/config/Constants.java` |
| §4.1 JASPER_REPORT_PATH | 549 | `"/com/hotelms/reports/"` | `"/hms/reports/"` |
| §9.1 Exception examples | 811,822,829,840,847 | `// com/hotelms/exception/*.java` | `// hms/exception/*.java` |
| §11.2 PasswordUtil | 1098 | `// com/hotelms/util/PasswordUtil.java` | `// hms/util/PasswordUtil.java` |
| §11.3 ValidationUtil | 1155 | `// com/hotelms/util/ValidationUtil.java` | `// hms/util/ValidationUtil.java` |
| §12.1 DatabaseConnection | 1228 | `// com/hotelms/database/DatabaseConnection.java` | `// hms/database/DatabaseConnection.java` |
| §12.2 ReservationDAO | 1337 | `// com/hotelms/dao/ReservationDAO.java` | `// hms/dao/ReservationDAO.java` |
| §13.1 HotelManagementApp | 1589 | `// com/hotelms/HotelManagementApp.java` | `// hms/HotelManagementApp.java` |
| §13.3 ReservationDialog | 1698 | `// com/hotelms/view/dialogs/ReservationDialog.java` | `// hms/view/dialogs/ReservationDialog.java` |

### 7.2 `PRD.md` — 2 stale Java version references

| Line | Before | After |
|------|--------|-------|
| 165 (table) | `Java 21 (Latest)` | `Java 24 (Latest)` |
| 618 (technical constraints) | `- Java 21 (latest version)` | `- Java 24 (latest version)` |
| 701 (success criteria) | `Java 21 system` → already fixed in Issue 3.3 pass | Already correct |

All 16 references were fixed in a single pass. Build confirmed: 34 source files, BUILD SUCCESSFUL.
