# HI04 — GuestController.searchGuests() Dedup Broken

**Severity:** HIGH — Duplicate results  
**File:** `src/hms/controller/GuestController.java`

## Problem

`searchGuests()` at lines 110 and 116–117 uses `List.contains()` for deduplication:
```java
if (byEmail != null && !results.contains(byEmail)) {
    results.add(byEmail);
}
```

The `Guest` class does **not** override `equals()` or `hashCode()`, so `List.contains()` falls back to `Object.equals()` — **reference equality**. If `searchByName()` and `searchByPhone()` return the same guest as different object instances, they appear as separate entries in the result list.

**Impact:** Search results may contain duplicate Guest entries confusing the UI.

## Fix

Either:
1. Add `equals()` and `hashCode()` to `Guest.java` (compare by `guestId`), or
2. Replace dedup logic with a `Set<Guest>` keyed by `guestId`, or
3. Collect all results and deduplicate by `guestId` using a stream's `distinct()` after implementing `equals`/`hashCode`.
