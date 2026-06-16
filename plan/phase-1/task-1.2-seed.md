# Task 1.2 — Write `seed.sql`

**Goal:** Populate all 8 tables with realistic test data for development and demo.

---

## Checklist

- [x] Create this checklist file
- [x] Write `database/seed.sql` with all 8 tables seeded (10,079 bytes)
- [x] Verify SQL syntax and FK integrity

---

## Data Volumes

| Table | Rows | Purpose |
|-------|------|---------|
| `guests` | 12 | Mix of individual/couple/family guests |
| `rooms` | 24 | 6 Single, 8 Double, 6 Suite, 4 Deluxe |
| `staff` | 6 | Manager, Receptionists, Housekeepers, Maintenance |
| `services` | 8 | Meals, Laundry, Spa, Conference |
| `reservations` | 10 | checked_out, checked_in, confirmed, pending, cancelled |
| `billing` | 3 | Paid, partial, pending — linked to checked_out reservations |
| `service_bookings` | 5 | Meals + spa across active reservations |
| `room_assignments` | 4 | Cleaning + maintenance tasks |
