# Task 4.4 — PasswordUtil.java

**Package:** `hms.util`
**File:** `src/hms/util/PasswordUtil.java`

## Description
bcrypt password hashing and verification using jBCrypt. Follows code_rules.md §11.2.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `hashPassword(String)` | `String` | BCrypt.hashpw with gensalt(12) |
| 2 | `verifyPassword(String, String)` | `boolean` | BCrypt.checkpw |

## Dependencies
- org.mindrot.jbcrypt.BCrypt (jbcrypt-0.4.jar in lib/)

## Checklist
- [x] Create checklist file
- [x] Write `PasswordUtil.java`
- [x] Verify Clean & Build
