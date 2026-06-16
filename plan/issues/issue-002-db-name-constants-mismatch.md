# Issue 002: `Constants.java` — `DB_NAME` Mismatch with Schema

**Status:** Fixed  
**Date:** 2026-06-16  
**Found in:** Phase 0 / Phase 3 transition  
**Root Cause:** `Constants.java` defined `DB_NAME = "hms"` but `database/schema.sql` creates `hotel_management_system`.

---

## Discovery

During database-to-DAO interaction audit (Phase 3 completion), `DatabaseConnection.java` was found to build a JDBC URL targeting database `hms`, but the schema creates and uses `hotel_management_system`. Every DAO call would fail with `Unknown database 'hms'`.

---

## Schema Definition (`database/schema.sql`)

```sql
CREATE DATABASE IF NOT EXISTS hotel_management_system;
USE hotel_management_system;

-- All 8 tables created inside this database
```

---

## Constant Definition (`src/hms/config/Constants.java`)

```java
public static final String DB_NAME = "hms";   // line 12 — WRONG
```

---

## Impact

| Layer | What Happens |
|-------|-------------|
| `DatabaseConnection.connectToDatabase()` | Builds URL `jdbc:mysql://localhost:3306/hms` |
| MySQL | Rejects — database `hms` does not exist |
| All 8 DAOs (70 methods) | Throw `DatabaseException` on every call |
| Application | Completely non-functional at runtime |

**Severity:** Critical — zero database operations work.

---

## Root Cause

The constant was set to `"hms"` (an abbreviation) early in Phase 0, but the schema was later written with the full descriptive name `hotel_management_system`. No cross-reference was done between `Constants.java` and `schema.sql`.

Additionally, `code_rules.md §7.1` (line 515) documents the correct value:

```java
public static final String DB_NAME = "hotel_management_system";
```

The implementation drifted from both the rules and the schema.

---

## Fix Applied

**File:** `src/hms/config/Constants.java`  
**Change:** `public static final String DB_NAME = "hms";` → `public static final String DB_NAME = "hotel_management_system";`  
**Rationale:** Aligns with `database/schema.sql` (source of truth) and `code_rules.md §7.1`.

---

## Verification

- [x] Clean & Build — *BUILD SUCCESSFUL* (25 source files)
- [x] No other hardcoded `"hms"` as a database name found in Java sources
- [x] All 8 DAOs use table names directly (not database-qualified), so no further changes needed

---

## Lessons Learned

1. Constants.java must be audited against schema.sql at creation time — the DB_NAME value must match the `CREATE DATABASE` statement exactly.
2. code_rules.md §7.1 contains a canonical reference — implementation must match it.
3. A grep for the wrong DB_NAME across Java sources should be part of every Phase-0-style setup review.
