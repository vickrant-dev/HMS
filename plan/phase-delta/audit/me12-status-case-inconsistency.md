# ME12 — AddNewStaffDialog Status Combo Title Case vs DB Lowercase

**Severity:** MEDIUM  
**File:** `src/hms/view/dialogs/AddNewStaffDialog.java`

## Problem

The `statusCmb` uses Title Case values:
```java
new String[] { "Active", "Inactive" }
```

But the database and `Constants.java` (lines 52–54) use lowercase:
```java
public static final String STAFF_ACTIVE = "active";
public static final String STAFF_INACTIVE = "inactive";
public static final String STAFF_ON_LEAVE = "on_leave";
```

The `StaffController.createStaff()` passes the value directly to the DAO without case normalization. Title Case values would be stored as-is, causing comparison failures when filtering by status later.

**Impact:** May cause inconsistent data in the DB if mixed with values entered via other means. Also missing `"on_leave"` status.

## Fix

Change the combo model values to match `Constants.java`:
```java
new String[] { "active", "inactive", "on_leave" }
```
