# CR02 — ServiceBookingDAO Column Collision: `sb.booking_date` shadowed by `r.booking_date`

**Severity:** CRITICAL — Data corruption  
**File:** `src/hms/dao/ServiceBookingDAO.java`  
**Affected:** All service booking read operations

## Problem

`SELECT_JOIN` (lines 22–42) selects both `sb.booking_date` and `r.booking_date` without column aliases. Both resolve to `"booking_date"` in the JDBC result set.

In `mapResultSetToServiceBooking()`:
- Line 271: `rs.getTimestamp("booking_date")` for `ServiceBooking.bookingDate` — returns `r.booking_date` (Reservation's booking date) instead of `sb.booking_date`
- Line 248: `rs.getTimestamp("booking_date")` for `Reservation.bookingDate` — also returns `r.booking_date`

**Impact:** The ServiceBooking object's `bookingDate` is populated with the **Reservation's booking date** instead of its own. Service booking dates are always lost on read.

## Fix

1. In `SELECT_JOIN`: change `sb.booking_date,` to `sb.booking_date AS svc_booking_date,`
2. In `mapResultSetToServiceBooking()` line 271: change `rs.getTimestamp("booking_date")` to `rs.getTimestamp("svc_booking_date")`
