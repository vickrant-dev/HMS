# ME11 — RoomManagementPanel Capacity Combo Trailing Space

**Severity:** MEDIUM  
**File:** `src/hms/view/panels/RoomManagementPanel.java` (line 190)

## Problem

The `capacityCmb` model includes `" "` (a single space) as its last value:
```java
new String[] { "Any", "1", "2", "3", "4", " " }
```

The filter handler checks `capacity.isEmpty()` to handle this, but the value `" "` (space, non-empty) would not be caught by `.isEmpty()`. If selected, it would try to parse `" ".trim()` as an integer, causing `NumberFormatException`.

**Impact:** If a user selects the blank-seeming last option, a parse error occurs.

## Fix

Remove the `" "` entry from the combo box model. The last valid entry should be `"4"`.
