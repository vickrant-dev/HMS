# Task 4.2 — DateUtil.java

**Package:** `hms.util`
**File:** `src/hms/util/DateUtil.java`

## Description
Static utility class for date operations: calculate nights, format/parse dates, date range generation.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `calculateNights(LocalDate, LocalDate)` | `long` | ChronoUnit.DAYS.between |
| 2 | `formatDate(LocalDate)` | `String` | yyyy-MM-dd |
| 3 | `formatDate(LocalDate, String)` | `String` | Custom pattern |
| 4 | `formatDateTime(LocalDateTime)` | `String` | yyyy-MM-dd HH:mm:ss |
| 5 | `formatDateTime(LocalDateTime, String)` | `String` | Custom pattern |
| 6 | `parseDate(String)` | `LocalDate` | ISO format |
| 7 | `parseDate(String, String)` | `LocalDate` | Custom pattern |
| 8 | `getDateRange(LocalDate, LocalDate)` | `List<LocalDate>` | Inclusive range |

## Dependencies
- java.time.LocalDate, LocalDateTime
- java.time.format.DateTimeFormatter
- java.time.temporal.ChronoUnit
- java.util.List, ArrayList

## Checklist
- [x] Create checklist file
- [x] Write `DateUtil.java`
- [x] Verify Clean & Build
