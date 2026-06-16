# Task 4.1 — ValidationUtil.java

**Package:** `hms.util`
**File:** `src/hms/util/ValidationUtil.java`

## Description
Static utility class providing 13 input validation methods. Follows code_rules.md §11.3 exactly, extended with additional methods for all domain types.

## Dependencies
- `ValidationException` (Phase 0.6)
- `Constants` (Phase 0.5) — `MAX_EMAIL_LENGTH`, `MAX_PHONE_LENGTH`

## Methods

| # | Method | Description | Uses |
|---|--------|-------------|------|
| 1 | `isValidEmail(String email)` | Basic regex match + max length | Guest/Staff email |
| 2 | `isValidPhone(String phone)` | Non-null, non-blank, digit-strip ≤ 15 | Guest/Staff phone |
| 3 | `isValidName(String name)` | Letters + spaces regex | Guest/Staff name |
| 4 | `isFutureDate(LocalDate date)` | After LocalDate.now() | Reservation check-in/out |
| 5 | `isPastDate(LocalDate date)` | Before LocalDate.now() | Guest DOB, Staff joining_date |
| 6 | `isDateInRange(LocalDate, min, max)` | Between min and max inclusive | Date range filters |
| 7 | `isPositive(double value)` | > 0 | Room price, billing charges |
| 8 | `isInRange(double, min, max)` | >= min && <= max | Price bounds |
| 9 | `isPositive(int value)` | > 0 | Room capacity, guest count |
| 10 | `isInRange(int, min, max)` | >= min && <= max | Guest count bounds |
| 11 | `isNotEmpty(String value)` | Not null, not blank | Required text fields |
| 12 | `isValidLength(String, int)` | Null or ≤ maxLength | String boundary enforcement |
| 13 | `matchesPattern(String, regex)` | Not null, matches pattern | Generic regex validation |

## Checklist
- [x] Create checklist file
- [x] Write `ValidationUtil.java`
- [x] Verify Clean & Build
