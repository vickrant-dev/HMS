# Task 0.7 — Create `DatabaseConnection.java`

**Goal:** Singleton database connection manager with retry logic (3 attempts, 2s delay).

**Package:** `hms.database` — `src/hms/database/DatabaseConnection.java`

---

## Checklist

- [x] Create this checklist file
- [x] Write `DatabaseConnection.java`
- [x] Verify Clean & Build — *BUILD SUCCESSFUL (8 source files, JDK 25 for -source 24)*

---

## Methods

| Method | Visibility | Description |
|--------|-----------|-------------|
| `getInstance()` | `public static synchronized` | Singleton accessor |
| `getConnection()` | `public` | Returns valid connection, retries on failure |
| `connectToDatabase()` | `private` | Retry loop: 3 attempts with 2s delay |
| `isConnectionValid()` | `private` | Checks `connection.isValid(30)` |
| `closeConnection()` | `public` | Closes connection gracefully |
