# HI07 — AddNewStaffDialog Hardcoded Default Password

**Severity:** HIGH — Security issue  
**File:** `src/hms/view/dialogs/AddNewStaffDialog.java` (line 302)

## Problem

```java
new Staff(fname, lname, email, phone, pos, dept, salary, joinDate, status, "password123")
```

Every new staff member is created with the same hardcoded password `"password123"`. This is a security vulnerability — anyone who knows a staff member's email can log in with this guessable default password.

**Impact:** Unauthorized access to the system via default credentials.

## Fix

Either:
1. Add a password field to the dialog UI and let the user set an initial password, or
2. Generate a random temporary password and display it to the admin, or
3. At minimum, use a less predictable default and force password change on first login (requires login system).
