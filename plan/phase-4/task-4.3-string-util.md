# Task 4.3 — StringUtil.java

**Package:** `hms.util`
**File:** `src/hms/util/StringUtil.java`

## Description
Static utility class for string operations: truncate, capitalize, sanitize, reservation ID generation.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `truncate(String, int)` | `String` | Truncate with "..." if over maxLength |
| 2 | `capitalize(String)` | `String` | First char uppercase, rest lowercase |
| 3 | `capitalizeWords(String)` | `String` | Each word capitalized |
| 4 | `sanitize(String)` | `String` | Trim + collapse internal whitespace |
| 5 | `generateReservationId()` | `String` | RES-YYYYMMDD-XXXXX (random) |
| 6 | `generateReservationId(int)` | `String` | RES-YYYYMMDD-XXXXX (sequential) |

## Dependencies
- Constants.RES_ID_PREFIX
- java.time.LocalDate
- java.time.format.DateTimeFormatter
- java.util.concurrent.ThreadLocalRandom

## Checklist
- [x] Create checklist file
- [x] Write `StringUtil.java`
- [x] Verify Clean & Build
