# HI01 — GuestDAO.update() Missing `email = ?` in SET Clause

**Severity:** HIGH — Silent data loss  
**File:** `src/hms/dao/GuestDAO.java` (lines 110–113)  
**Affected:** Guest email updates

## Problem

```sql
UPDATE guests SET first_name = ?, last_name = ?, phone = ?,
address = ?, id_proof_type = ?, id_proof_number = ?,
date_of_birth = ?, guest_type = ?, nationality = ?
WHERE guest_id = ?
```

The `email` column is **not included** in the SET clause. The controller (`GuestController.updateGuest()`) validates the new email for uniqueness and passes it in the `Guest` object, but the DAO silently ignores it.

**Impact:** Guest emails can never be changed. The controller believes the update succeeded, but the old email remains in the database.

## Fix

Add `email = ?,` to the SET clause after `last_name = ?,` and adjust parameter indices in `pstmt.setString()` calls accordingly.
