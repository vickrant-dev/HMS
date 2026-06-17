# Performance 001: Dangling MySQL Connection on Application Exit

**Status:** Documented (no fix required)  
**Date:** 2026-06-16  
**Category:** Connection Lifecycle / Clean Shutdown  
**Severity:** Cosmetic — no functional impact

---

## Description

`DatabaseConnection.closeConnection()` (line 74) is defined but never invoked. When the application exits (via window close, Ctrl+C, or `System.exit()`), the Singleton's shared `Connection` is left open. The MySQL server only learns about the disconnection after `wait_timeout` (default 28800 seconds = 8 hours) or when the TCP socket is eventually cleaned up by the OS.

---

## Root Cause

`HotelManagementApp.java` registers no shutdown hook or `WindowListener` to call `closeConnection()`. The Singleton's `Connection` is simply abandoned on JVM termination.

---

## Impact Analysis

| Factor | Assessment |
|--------|-----------|
| **Data loss** | None — auto-commit is `true` for all DAO operations. No uncommitted transactions are lost. |
| **Connection leak** | None — one connection per app instance. MySQL's default `max_connections` is 151. A single abandoned connection is negligible. Even repeated restarts (without waiting for `wait_timeout`) would require 151 launches to exhaust the pool. |
| **Server-side cleanup** | MySQL's `wait_timeout` (8h) or `interactive_timeout` (8h) eventually closes idle connections. `thread_pool` (if enabled) handles cleanup faster. |
| **User experience** | No visible impact — the app exits instantly and the connection drops silently. |
| **Multi-user scale** | N/A — single-user desktop app per PRD §9.4 ("Each user operates one instance, no concurrent multi-user"). |

---

## Why This Is Deferred

1. **No functional bug** — The app works correctly before, during, and after exit.
2. **Zero risk of resource exhaustion** — One connection per launch cannot accumulate (earlier connections die with the JVM process).
3. **Single-user constraint** — The PRD explicitly limits to one instance at a time. Connection pooling, graceful shutdown, and other server-grade concerns are out of scope.
4. **Edge case only** — Even rapid restarting (>151 times within 8 hours) would be needed to hit MySQL's connection limit. Unrealistic for coursework testing.
5. **JDBC driver cleanup** — `com.mysql.cj.jdbc.AbandonedConnectionCleanupThread` runs as a daemon thread and handles abandoned connections on the driver side.

---

## Recommended Fix (if needed in future)

**File:** `src/hms/HotelManagementApp.java` — add after `mainWindow.setVisible(true)`:

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    try {
        DatabaseConnection.getInstance().closeConnection();
    } catch (DatabaseException e) {
        // Application shutting down — silently ignore
    }
}));
```

**Required imports:**
```java
import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
```

---

## Related

- This performance note exists because `closeConnection()` was identified as dead code during the database/↔dao/ audit (same audit that found Issue 002 and Issue 003).
- The Singleton pattern is now correctly implemented (Issue 003 fixed), so the Connection is reused across DAO calls. Cleanup on exit is the only remaining lifecycle gap.
- If a connection pooling library (e.g., HikariCP) is ever introduced, `closeConnection()` would be replaced by `dataSource.close()`, and a shutdown hook would become mandatory.
