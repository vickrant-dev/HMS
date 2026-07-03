# LO09 — GuestCheckOutDialog changeDueAmount Set Then Hidden by dispose()

**Severity:** LOW  
**File:** `src/hms/view/dialogs/GuestCheckOutDialog.java` (lines 522–524)

## Problem

```java
changeDueAmount.setText(...);          // line 522 — user never sees this
JOptionPane.showMessageDialog(...);     // line 523
dispose();                              // line 524
```

The `changeDueAmount` label is updated on line 522, but the user never sees the new value because:
1. An information dialog appears immediately (line 523), covering the main dialog
2. The dialog is disposed (line 524) as soon as the info dialog closes

**Impact:** The change-due display is effectively invisible.

## Fix

Either:
1. Show the change-due in the information dialog's message instead, or
2. Remove the label update and compute it only for the info dialog text.
