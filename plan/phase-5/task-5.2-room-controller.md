# Task 5.2 — RoomController.java

**Package:** `hms.controller`
**File:** `src/hms/controller/RoomController.java`

## Description
Orchestrates room CRUD, availability check, and status transitions.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `createRoom(Room)` | `Room` | Validate, check room number uniqueness, DAO.save |
| 2 | `getRoomById(int)` | `Room` | DAO.getById |
| 3 | `getAllRooms()` | `List<Room>` | DAO.getAll |
| 4 | `updateRoom(Room)` | `void` | Validate, check uniqueness, DAO.update |
| 5 | `deleteRoom(int)` | `void` | DAO.delete |
| 6 | `filterByStatus(String)` | `List<Room>` | DAO.filterByStatus |
| 7 | `filterByType(String)` | `List<Room>` | DAO.filterByType |
| 8 | `filterByPriceRange(double, double)` | `List<Room>` | DAO.filterByPriceRange |
| 9 | `checkAvailability(LocalDate, LocalDate)` | `List<Room>` | DAO.checkAvailability |
| 10 | `updateRoomStatus(int, String)` | `void` | Fetch, validate, reconstruct immutable Room, DAO.update |
| 11 | `markForMaintenance(int)` | `void` | updateRoomStatus(id, MAINTENANCE) |

## Dependencies
- RoomDAO (Phase 3.2)
- ValidationUtil (Phase 4.1)
- Constants (Phase 0.5)

## Checklist
- [x] Create checklist file
- [x] Write `RoomController.java`
- [x] Verify Clean & Build
