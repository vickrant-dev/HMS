# Task D3 — Fix `ValidationUtil.isValidPhone()` Bug

**Area:** Phase 4.1 — Validation Utilities (bugfix)  
**Severity:** MEDIUM — invalid phone numbers silently pass validation

## Problem

`ValidationUtil.isValidPhone()` at `src/hms/util/ValidationUtil.java:29`:

```java
public static boolean isValidPhone(String phone) {
    if (phone == null || phone.trim().isEmpty()) {
        return false;
    }
    return phone.replaceAll("[^0-9]", "").length() <= Constants.MAX_PHONE_LENGTH;
}
```

**Bug:** `replaceAll("[^0-9]", "")` strips all non-digit characters. A string with **no digits** (e.g., `"abc"`, `"hello world"`) yields an empty string whose `.length()` is `0`. Since `0 <= 15` (MAX_PHONE_LENGTH) evaluates to `true`, the method returns `true` for strings containing **zero digits**.

**Affected controllers** that rely on this method for validation:
- `GuestController.validateGuest()` — line 141
- `StaffController.validateStaff()` — line 179

## Fix

```java
public static boolean isValidPhone(String phone) {
    if (phone == null || phone.trim().isEmpty()) {
        return false;
    }
    int digitCount = phone.replaceAll("[^0-9]", "").length();
    return digitCount > 0 && digitCount <= Constants.MAX_PHONE_LENGTH;
}
```

The single change is adding `digitCount > 0 &&`.

## Verification

| Input | Before | After |
|-------|--------|-------|
| `"1234567890"` | true | true |
| `""` | false | false |
| `"abc"` | **true** | **false** |
| `"123-456-7890"` | true | true |
| `"(555) 123-4567"` | true (10 digits) | true |
| `null` | false | false |

## Checklist

- [ ] Add `digitCount > 0 &&` guard to `isValidPhone()`
- [ ] Verify Clean & Build
