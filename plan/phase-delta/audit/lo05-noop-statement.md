# LO05 — AddGuestDialog No-Op Statement

**Severity:** LOW  
**File:** `src/hms/view/dialogs/AddGuestDialog.java` (line 53)

## Problem

```java
nationality.getText();
```

This statement calls `getText()` on the `nationality` field but discards the return value. It has no side effects and does nothing.

**Impact:** Dead code, confusing to maintainers.

## Fix

Remove the no-op statement.
