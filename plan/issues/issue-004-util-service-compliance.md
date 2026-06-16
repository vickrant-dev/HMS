# Issue 004: `hms.util /*` & `hms.service/*` — Code Rules Compliance Audit

**Status:** Open  
**Date:** 2026-06-17  
**Found in:** Phase 4 completion audit  
**Root Cause:** Implementation drifted from `code_rules.md` standards (JavaDoc, error handling patterns) and introduced inconsistency bugs in utility method behavior.

---

## Files Audited (9)

| Package | File | Lines | Methods |
|---------|------|-------|---------|
| `hms.util` | `ValidationUtil.java` | 79 | 13 |
| `hms.util` | `DateUtil.java` | 75 | 8 |
| `hms.util` | `StringUtil.java` | 74 | 6 |
| `hms.util` | `PasswordUtil.java` | 18 | 2 |
| `hms.util` | `IconUtil.java` | 53 | 9 |
| `hms.service` | `PricingStrategy.java` | 9 | 1 (interface) |
| `hms.service` | `NormalPricingStrategy.java` | 11 | 1 |
| `hms.service` | `SeasonalPricingStrategy.java` | 17 | 1 |
| `hms.service` | `CorporatePricingStrategy.java` | 18 | 1 |

**Total: 42 public methods across 9 files, 0 have JavaDoc.**

---

## 1. CRITICAL — Incorrect Behavior

### 1.1 `DateUtil.calculateNights()` — Silent negative return on inverted dates

**File:** `src/hms/util/DateUtil.java:16`

```java
public static long calculateNights(LocalDate checkIn, LocalDate checkOut) {
    return ChronoUnit.DAYS.between(checkIn, checkOut);
    // checkIn=2026-06-20, checkOut=2026-06-17 → returns -3
}
```

**Impact:** If a caller (e.g., `ReservationController`) passes inverted dates, `calculateNights` returns a negative `long`. In billing: `room.getBasePrice() * (-3)` → negative charge. The reservation table `total_amount` column is `DECIMAL(12,2) NOT NULL` — a negative value silently corrupts financial data.

**Sibling inconsistency:** `DateUtil.getDateRange()` on line 65 handles the same scenario correctly:
```java
if (start == null || end == null || start.isAfter(end)) {
    return dates;  // Returns empty list — graceful
}
```

**Fix:** Guard with `start.isAfter(end) ? 0L : ChronoUnit.DAYS.between(start, end)`.

---

### 1.2 `StringUtil.truncate()` — Output exceeds `maxLength` by 3 characters

**File:** `src/hms/util/StringUtil.java:21`

```java
return value.substring(0, maxLength) + "...";  // maxLength=20 → 23 chars
```

**Impact:** If a UI text field has `setColumns(20)` or `setDocument(new PlainDocument())` with a 20-char limit, the truncated output `"Long string content..."` (23 chars) would be silently clipped or cause UI layout overflow.

**Industry standard:** Apache Commons `StringUtils.abbreviate()` truncates to `maxLength` inclusive of `"..."`, i.e. `value.substring(0, maxLength - 3) + "..."`.

**Fix:** `return value.substring(0, Math.max(0, maxLength - 3)) + "...";`

---

### 1.3 `DateUtil.parseDate(String)` — Throws unchecked exception while all sibling methods return null

**File:** `src/hms/util/DateUtil.java:52`

```java
// All other DateUtil methods handle null/bad input gracefully:
public static String formatDate(LocalDate date) {
    if (date == null) { return ""; }  // graceful
}
public static List<LocalDate> getDateRange(LocalDate start, LocalDate end) {
    if (start == null || end == null || start.isAfter(end)) { return new ArrayList<>(); }  // graceful
}

// But parseDate throws:
public static LocalDate parseDate(String dateStr) {
    if (dateStr == null || dateStr.isBlank()) { return null; }
    return LocalDate.parse(dateStr);  // DateTimeParseException — UNCHECKED
}
```

**Impact:** A controller calling `DateUtil.parseDate("invalid-date")` gets a `DateTimeParseException` at runtime, while `DateUtil.formatDate(null)` returns `""`. This inconsistency forces callers to remember which methods need try-catch and which don't, violating the **Principle of Least Astonishment**.

**Fix:** Wrap in try-catch and return `null`:
```java
try {
    return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
} catch (DateTimeParseException e) {
    return null;
}
```

---

## 2. MODERATE — Spec Compliance

### 2.1 Zero JavaDoc across 42 public methods (code_rules §10.1)

**code_rules §10.1 (line 944–946):**
> *"JavaDoc for public methods and public classes only"*

**code_rules §11.2 (PasswordUtil example, lines 1103–1122):** Every public method has `/** ... */` with `@param`, `@return` tags.

**code_rules §11.3 (ValidationUtil example, lines 1158–1188):** Every public method has `/** ... */` with description.

| File | Methods | With JavaDoc | Gap |
|------|---------|-------------|-----|
| `ValidationUtil.java` | 13 | 0 | 13 |
| `DateUtil.java` | 8 | 0 | 8 |
| `StringUtil.java` | 6 | 0 | 6 |
| `PasswordUtil.java` | 2 | 0 | 2 |
| `IconUtil.java` | 9 | 0 | 9 |
| `PricingStrategy.java` | 1 | 0 | 1 |
| `NormalPricingStrategy.java` | 1 | 0 | 1 |
| `SeasonalPricingStrategy.java` | 1 | 0 | 1 |
| `CorporatePricingStrategy.java` | 1 | 0 | 1 |
| **Total** | **42** | **0** | **42** |

**Impact:** Code review difficulty — reviewers must read method bodies to infer intent. IDE auto-complete shows no documentation. Future developers cannot understand method contracts without reading source.

**Fix:** Add JavaDoc to every public method following the template in code_rules §11.2–§11.3.

---

### 2.2 Missing `throws` declarations on public methods (code_rules §9.3, §10.1)

**code_rules §9.3 (line 913–937):** Spec shows throwing exceptions with meaningful messages.

**code_rules §10.1 (line 949–963):** JavaDoc includes `@throws` tag.

None of the 42 methods declare checked exceptions. The utility methods return booleans/results instead (defensive design). This is intentional per plan/README.md (controllers throw, not utilities), so the code correctly follows the spec's usage pattern at §11.3 lines 1191–1206. **Not a violation.** ✅

---

## 3. MINOR — Observations & Quality

### 3.1 `IconUtil.ICON_PATH` — Resource directory does not exist

**File:** `src/hms/util/IconUtil.java:8`

```java
private static final String ICON_PATH = "/hms/resources/icons/";
```

No `src/hms/resources/` directory exists. No icon files (add.png, edit.png, etc.) exist. All 9 convenience methods return `null`.

**Impact:** Any controller/view code that calls `IconUtil.getAddIcon()` today gets `null`. Swing `JButton` renders a button with no icon (text-only). Not a runtime error, but the utility provides no value until resource files are added.

**Fix considered:** Either create the resource directory with icon files (out of Phase 4 scope), or refactor to use `FlatLaf` built-in icons via `UIManager.getIcon()` which would work immediately without external files. The latter is recommended:
```java
public static ImageIcon getAddIcon() {
    Icon icon = UIManager.getIcon("List.addIcon");  // FlatLaf provides these
    return icon instanceof ImageIcon ? (ImageIcon) icon : null;
}
```

---

### 3.2 Hardcoded date format patterns (code_rules §7.1 — DRY principle)

**File:** `src/hms/util/DateUtil.java:38`

```java
DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")  // Hardcoded string
```

**code_rules §7.1 (line 506):**
> *"All constants must be in Constants.java following DRY principle"*

This pattern is only used once in the codebase currently. It becomes a maintenance concern when another class (e.g., a report or view panel) needs the same format. Currently `Constants.java` has zero date format patterns.

**Fix (deferred):** Add to `Constants.java` when Phase 9+ (reports/views) demonstrates reuse.

---

### 3.3 `StringUtil.capitalize()` — Lowercases entire rest of string

**File:** `src/hms/util/StringUtil.java:29`

```java
return Character.toUpperCase(trimmed.charAt(0)) + trimmed.substring(1).toLowerCase();
```

| Input | Output | Expected? |
|-------|--------|-----------|
| `"MCDONALD"` | `"Mcdonald"` | Probably correct |
| `"McDonald"` | `"Mcdonald"` | Debatable — compound surname corrupted |
| `"O'Brien"` | `"O'brien"` | Apostrophe-name corrupted |
| `"john doe"` | `"John doe"` | Second word not capitalized (use `capitalizeWords` for this) |

**Impact:** Names with mixed case or apostrophes may not display as intended. This is standard behavior for `capitalize()` — the `capitalizeWords()` method handles multi-word cases. The apostrophe case is a known limitation of simple capitalization.

**Fix:** Only if surname-aware capitalization becomes a requirement (not in current scope).

---

### 3.4 `CorporatePricingStrategy.java` — No `@Override` in code_rules spec but present in implementation

**code_rules §12.4 (line 1556–1568):**
```java
public class CorporatePricingStrategy implements PricingStrategy {
    private double discountRate;
    public CorporatePricingStrategy(double discountRate) { ... }
    public double calculatePrice(Room room, int numberOfNights) { ... }  // No @Override
}
```

**Our code:** Uses `@Override` on all three implementations.

This is actually **better** than the spec — `@Override` provides compile-time checking that the method actually overrides an interface method. But it's a deviation worth noting for audit completeness. **No fix needed.**

---

## 4. CONTEXT Contradictions Discovered

### 4.1 Package name: `code_rules.md` uses `com.hotelms` but codebase uses `hms`

Throughout `code_rules.md`, the reference package is `com.hotelms.*`. The actual project uses `hms.*` (e.g., `hms.util`, `hms.service`, `hms.config`). Examples in §§4.1, 7.1, 11.2, 11.3, 12.1, 12.2, 12.4 all reference `com.hotelms.*`.

| Reference | code_rules.md | Actual Source |
|-----------|---------------|---------------|
| ValidationUtil | `com.hotelms.util.ValidationUtil` | `hms.util.ValidationUtil` |
| PasswordUtil | `com.hotelms.util.PasswordUtil` | `hms.util.PasswordUtil` |
| PricingStrategy | `com.hotelms.controller` (implied by usage) | `hms.service` |
| Constants | `com.hotelms.config.Constants` | `hms.config.Constants` |

**Status:** Known, documented in project summary. No action needed — the package rename was an early Phase 0 decision.

### 4.2 `@FunctionalInterface` annotation vs spec example

**code_rules §12.4** shows the interface without `@FunctionalInterface`. Our code adds it. This is a beneficial annotation (enforces single abstract method at compile time) but is not in the spec example. Not a contradiction, just a deviation.

### 4.3 PasswordUtil: `final` class + private constructor vs spec example

**code_rules §11.2** shows `public class PasswordUtil` (not `final`, no private constructor). Our code has both `final` and a private constructor. This is **better practice** per §5.3 (immutability) and is consistent with how all utility classes are structured in the project. Not a contradiction worth fixing.

---

## 5. Summary

| Severity | Count | Items |
|----------|-------|-------|
| **CRITICAL** | 3 | Inverted dates → negative charge; truncation exceeds maxLength; parseDate inconsistent error handling |
| **MODERATE** | 1 | Zero JavaDoc across 42 public methods (§10.1) |
| **MINOR** | 4 | Icon resources missing; hardcoded date format; capitalize surname behavior; @Override annotation deviation |
| **CONTEXT CONTRADICTIONS** | 3 | Package name drift; @FunctionalInterface added; final+private constructor on PasswordUtil |

---

## 6. Recommended Fix Order

| Order | Issue | Effort | Impact |
|-------|-------|--------|--------|
| 1 | 1.1 `calculateNights` inverted dates | 1 line | Prevents negative billing |
| 2 | 1.2 `truncate` overflow | 1 line | Prevents UI layout corruption |
| 3 | 1.3 `parseDate` inconsistency | 4 lines | Prevents runtime crashes |
| 4 | 2.1 JavaDoc | ~42 blocks | Compliance + maintainability |
| 5 | 3.1 Icon resource strategy | Refactor | Makes utility usable immediately |
| 6 | 3.2 Date format constant | Defer | No current reuse |

---

## 7. Verification

- [ ] Fix 1.1 — `calculateNights`: add start > end guard
- [ ] Fix 1.2 — `truncate`: adjust substring to account for "..."
- [ ] Fix 1.3 — `parseDate`: wrap in try-catch, return null
- [ ] Fix 2.1 — Add JavaDoc to all 42 methods
- [ ] Fix 3.1 — Refactor IconUtil to use UIManager icons
- [ ] Clean & Build
