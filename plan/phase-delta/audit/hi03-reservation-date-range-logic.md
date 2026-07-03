# HI03 — ReservationDAO.getByDateRange() Wrong Overlap Logic

**Severity:** HIGH — Incorrect query results  
**File:** `src/hms/dao/ReservationDAO.java`

## Problem

```sql
WHERE (r.check_in_date BETWEEN ? AND ?
    OR r.check_out_date BETWEEN ? AND ?)
```

This only catches reservations where **at least one endpoint** falls inside the search range. It **misses** reservations that span entirely across the range, e.g.:
- Reservation: check_in Jan 1 → check_out Jan 31
- Search range: Jan 15 → Jan 20
- Neither Jan 1 nor Jan 31 is BETWEEN Jan 15 AND Jan 20 → **not returned**

**Impact:** Date-range filtered reports and searches miss reservations that overlap the range without having an endpoint inside it.

## Fix

Replace with the standard date-overlap pattern:
```sql
WHERE r.check_in_date < ? AND r.check_out_date > ?
```
(Second pair of `?` replaced with `end_date` and `start_date` respectively, or reuse the same parameters swapped.)
