# Task 5.5 — StaffController.java

**Package:** `hms.controller`
**File:** `src/hms/controller/StaffController.java`

## Description
Orchestrates staff CRUD, filtering, and room assignment management.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `createStaff(Staff)` | `Staff` | Validate, hash password, DAO.save |
| 2 | `getStaffById(int)` | `Staff` | DAO.getById |
| 3 | `getAllStaff()` | `List<Staff>` | DAO.getAll |
| 4 | `updateStaff(Staff)` | `void` | Validate, handle password hash, DAO.update |
| 5 | `deleteStaff(int)` | `void` | DAO.delete |
| 6 | `filterByDepartment(String)` | `List<Staff>` | DAO.filterByDepartment |
| 7 | `filterByPosition(String)` | `List<Staff>` | DAO.filterByPosition |
| 8 | `filterByStatus(String)` | `List<Staff>` | DAO.filterByStatus |
| 9 | `createRoomAssignment(Room, Staff, LocalDate, String, String)` | `RoomAssignment` | Validate, DAO.save |
| 10 | `getAssignmentById(int)` | `RoomAssignment` | DAO.getById |
| 11 | `getAllAssignments()` | `List<RoomAssignment>` | DAO.getAll |
| 12 | `updateAssignment(RoomAssignment)` | `void` | DAO.update |
| 13 | `deleteAssignment(int)` | `void` | DAO.delete |
| 14 | `getAssignmentsByRoom(int)` | `List<RoomAssignment>` | DAO.getByRoomId |
| 15 | `getAssignmentsByStaff(int)` | `List<RoomAssignment>` | DAO.getByStaffId |
| 16 | `updateAssignmentStatus(int, String)` | `void` | DAO.updateStatus |

## Dependencies
- StaffDAO (Phase 3.5)
- RoomAssignmentDAO (Phase 3.8)
- PasswordUtil (Phase 4.4)
- ValidationUtil (Phase 4.1)
- Constants (Phase 0.5)

## Known Limitation
**Staff authentication (login):** Per project summary, staff authentication is out of scope. The `passwordHash` field is stored but no `authenticateStaff(email, password)` method is implemented. This should be added when login functionality is required in the future, using `PasswordUtil.verifyPassword`.

## Checklist
- [x] Create checklist file
- [x] Write `StaffController.java`
- [x] Verify Clean & Build
