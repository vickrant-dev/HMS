# Issue 003: `DAO` Layer — Connection Lifecycle Conflict with Singleton

**Status:** Fully Fixed (all 8 DAOs)  
**Date:** 2026-06-16  
**Found in:** database/ ↔ dao/ interaction audit  
**Root Cause:** `code_rules.md` §8.3 and §12.1 documented DAO methods wrapping `Connection` in `try`-with-resources, which auto-closes the Singleton's shared connection. After each DAO call, the connection is closed, forcing a new TCP connection on the next call — defeating connection reuse and blocking transaction support.

---

## Discovery

All 70 DAO methods follow this pattern:

```java
try (Connection conn = DatabaseConnection.getInstance().getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    // ...
}
```

`try`-with-resources calls `conn.close()` when the block exits. Since `DatabaseConnection` is a Singleton managing **one** shared `Connection` instance (per §12.1), the next DAO call triggers `isConnectionValid()` → `false` → `connectToDatabase()`. Every single DAO operation creates a new MySQL TCP connection.

---

## Impact

| Concern | Detail |
|---------|--------|
| **Performance** | Every DAO call pays ~30ms MySQL handshake overhead. For 10 sequential operations, ~300ms of unnecessary latency. |
| **Transaction support** | Phase 5 (Controllers) requires multi-step transactions via `setAutoCommit(false)`, `commit()`/`rollback()`. If DAOs close the Connection internally, the outer transaction breaks — `conn.commit()` operates on a closed connection. |
| **Resource waste** | TCP connections are created and destroyed hundreds of times during a single application session. |

---

## The Contradiction

`code_rules.md` §12.1 (Singleton) manages one shared `Connection`:

```java
public Connection getConnection() throws DatabaseException {
    if (connection == null || !isConnectionValid()) {
        connectToDatabase();
    }
    return connection;  // Same instance every call
}
```

But `code_rules.md` §8.3 (Connection Management) and §12.1 (usage example) wrap that same `Connection` in `try`-with-resources, calling `close()` on exit. The Singleton cannot maintain a reusable connection if every consumer closes it.

---

## Fix Applied (GuestDAO.java — 9 methods)

### Before (every method):
```java
try (Connection conn = DatabaseConnection.getInstance().getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    // conn.close() called here — invalidates Singleton
```

### After:
```java
Connection conn = DatabaseConnection.getInstance().getConnection();
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    // only pstmt.close() called — Singleton connection stays open
```

Only the `Connection` line moves outside `try`. `PreparedStatement` and `ResultSet` remain auto-closed. No logic changes.

### GuestDAO.java methods fixed

| Method | Pattern | Transformation |
|--------|---------|---------------|
| `save()` | Connection + PreparedStatement(RETURN_GENERATED_KEYS) + inner generatedKeys | Connection outside try |
| `getById()` | Connection + PreparedStatement + inner ResultSet | Connection outside outer try |
| `getAll()` | Connection + PreparedStatement + ResultSet (all in try) | Connection outside try |
| `update()` | Connection + PreparedStatement | Connection outside try |
| `delete()` | Connection + PreparedStatement | Connection outside try |
| `searchByName()` | Connection + PreparedStatement + inner ResultSet | Connection outside outer try |
| `searchByEmail()` | Connection + PreparedStatement + inner ResultSet | Connection outside outer try |
| `searchByPhone()` | Connection + PreparedStatement + inner ResultSet | Connection outside outer try |
| `existsByEmail()` | Connection + PreparedStatement + inner ResultSet | Connection outside outer try |

### code_rules.md sections corrected

| Section | What Changed |
|---------|-------------|
| §8.1 | PreparedStatement example — Connection moved outside try |
| §8.3 | Connection Management — example fixed, INCORRECT example relabeled to "no try-with-resources (resource leak)" |
| §8.5 | Transaction Management — Connection outside try, `finally` block restores `autoCommit(true)` |
| §12.1 | Singleton usage example — Connection moved outside try |
| §12.2 | DAO pattern (getById, save, update, delete) — all updated |

---

## Verification (GuestDAO.java)

- [x] Clean & Build — *BUILD SUCCESSFUL* (25 source files)

---

## All DAOs Fixed

| File | Methods | Status |
|------|---------|--------|
| `GuestDAO.java` | 9 | ✅ Fixed in Step 1 |
| `RoomDAO.java` | 10 | ✅ Fixed in Step 2 |
| `ReservationDAO.java` | 12 | ✅ Fixed in Step 2 |
| `BillingDAO.java` | 6 | ✅ Fixed in Step 2 |
| `StaffDAO.java` | 9 | ✅ Fixed in Step 2 |
| `ServiceDAO.java` | 8 | ✅ Fixed in Step 2 |
| `ServiceBookingDAO.java` | 7 | ✅ Fixed in Step 2 |
| `RoomAssignmentDAO.java` | 9 | ✅ Fixed in Step 2 |
| **Total** | **70** | **✅ BUILD SUCCESSFUL** |

---

## Lessons Learned

1. Documented patterns in `code_rules.md` must be validated for internal consistency — the Singleton (§12.1) and Connection Management (§8.3) examples contradicted each other.
2. `try`-with-resources is correct for `PreparedStatement` and `ResultSet`, but the `Connection` lifecycle belongs to the Singleton — DAOs should never close it.
3. Transactions require the controller to manage `autoCommit`, `commit()`, and `rollback()` directly on the Connection, with a `finally` block restoring `autoCommit(true)`.
4. Build verification (Clean & Build) is sufficient to catch syntax errors but not lifecycle bugs — runtime testing with connection logging is needed to confirm reuse.
