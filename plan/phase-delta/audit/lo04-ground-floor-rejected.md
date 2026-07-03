# LO04 — RoomController Rejects Ground Floor (Floor 0)

**Severity:** LOW  
**File:** `src/hms/controller/RoomController.java` (line 138)

## Problem

The `validateRoom()` method uses:
```java
ValidationUtil.isPositive(room.getFloor())
```

`isPositive()` checks for `> 0`. Many buildings number the ground floor as 0. Floor 0 is rejected.

**Impact:** Cannot register rooms on the ground floor.

## Fix

Change validation to `>= 0` (or use a different validation method).
