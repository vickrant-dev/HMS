# Task 1.1 — Write `schema.sql`

**Goal:** Create the complete MySQL database schema — 8 tables, constraints, indexes, ENUMs.

---

## Checklist

- [x] Create this checklist file
- [x] Write `database/schema.sql` with all 8 tables (6644 bytes)
- [x] Verify file is created and valid SQL

---

## Tables

| # | Table | FK Dependencies | ENUM Status |
|---|-------|----------------|-------------|
| 1 | `guests` | — | — |
| 2 | `rooms` | — | `available`, `occupied`, `maintenance`, `reserved` |
| 3 | `staff` | — | `active`, `inactive`, `on_leave` |
| 4 | `services` | — | — |
| 5 | `reservations` | guests, rooms | `pending`, `confirmed`, `checked_in`, `checked_out`, `cancelled` |
| 6 | `billing` | reservations | `pending`, `partial`, `paid`, `refunded` |
| 7 | `service_bookings` | reservations, services | `pending`, `completed`, `cancelled` |
| 8 | `room_assignments` | rooms, staff | `pending`, `in_progress`, `completed` |

## Indexes

17 indexes total across 6 tables (guests and services have UNIQUE constraints only).
