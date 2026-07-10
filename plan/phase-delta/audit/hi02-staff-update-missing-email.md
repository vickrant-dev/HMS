# HI02 — StaffDAO.update() Missing `email = ?` in SET Clause

**Severity:** HIGH — Silent data loss  
**File:** `src/hms/dao/StaffDAO.java` (lines 107–109)

## Problem

```sql
UPDATE staff SET first_name = ?, last_name = ?, phone = ?,
position = ?, department = ?, salary = ?, joining_date = ?,
status = ?, password_hash = ? WHERE staff_id = ?
```

Identical pattern to HI01 — the `email` column is missing from the SET clause. The controller validates email changes but the DAO ignores them.

**Impact:** Staff emails can never be changed.

## Fix

Add `email = ?,` after `last_name = ?,` and adjust parameter indices.
