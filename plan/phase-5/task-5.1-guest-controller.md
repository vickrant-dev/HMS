# Task 5.1 — GuestController.java

**Package:** `hms.controller`
**File:** `src/hms/controller/GuestController.java`

## Description
Orchestrates guest CRUD with validation before DAO calls and search coordination.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `createGuest(Guest)` | `Guest` | Validate, check email uniqueness, DAO.save |
| 2 | `getGuestById(int)` | `Guest` | DAO.getById |
| 3 | `getAllGuests()` | `List<Guest>` | DAO.getAll |
| 4 | `updateGuest(Guest)` | `void` | Validate editable fields, DAO.update |
| 5 | `deleteGuest(int)` | `void` | DAO.delete |
| 6 | `searchByName(String)` | `List<Guest>` | DAO.searchByName |
| 7 | `searchByEmail(String)` | `Guest` | DAO.searchByEmail |
| 8 | `searchByPhone(String)` | `List<Guest>` | DAO.searchByPhone |
| 9 | `searchGuests(String)` | `List<Guest>` | Multi-field keyword search |

## Dependencies
- GuestDAO (Phase 3.1)
- ValidationUtil (Phase 4.1)
- Constants (Phase 0.5)

## Checklist
- [x] Create checklist file
- [x] Write `GuestController.java`
- [x] Verify Clean & Build
