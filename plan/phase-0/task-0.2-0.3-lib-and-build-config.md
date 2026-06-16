# Task 0.2 & 0.3 — lib/ Setup & Build Configuration

**Goal:** Create local `lib/` folder, copy JARs from Downloads, update `project.properties` to reference local paths.

---

## Checklist

### Part A: Create lib/ & populate JARs

- [x] Create `lib/` directory under project root
- [x] Copy `flatlaf-3.5.1.jar` → `lib/flatlaf-3.5.1.jar`
- [x] Copy `mysql-connector-j-9.7.0.jar` → `lib/mysql-connector-j-9.7.0.jar`
- [x] Copy `jbcrypt-0.4.jar` → `lib/jbcrypt-0.4.jar`

### Part B: Update project.properties

- [x] Re-point `file.reference.flatlaf-3.5.1.jar` to `lib/flatlaf-3.5.1.jar`
- [x] Re-point `file.reference.mysql-connector-j-9.7.0.jar` to `lib/mysql-connector-j-9.7.0.jar`
- [x] Add `file.reference.jbcrypt-0.4.jar=lib/jbcrypt-0.4.jar`
- [x] Add `jbcrypt-0.4.jar` to `javac.classpath`

### Part C: Verify

- [ ] ~~NetBeans Clean and Build passes without errors~~ *(Java/ant not available in this environment — verify manually in NetBeans)*
- [x] `lib/` contains 3 JARs: FlatLaf, MySQL Connector, jBCrypt
- [x] No external path references in `project.properties` (no `Downloads` paths)
- [x] Plan file created at `plan/phase-0/task-0.2-0.3-lib-and-build-config.md`

---

## Final lib/ Structure

```
ead_cw/
├── lib/
│   ├── flatlaf-3.5.1.jar
│   ├── mysql-connector-j-9.7.0.jar
│   └── jbcrypt-0.4.jar
├── src/
├── nbproject/
│   └── project.properties    ← updated paths
├── build.xml
└── ...
```

---

## Notes

- **JasperReports 7.0.6** was not copied to `lib/` — it is not required until Phase 14 (Reporting), so excluding it now does not violate any Phase 0 requirements or guidelines. It will be added when Phase 14 begins.
- **jBCrypt** was included because `PasswordUtil.java` (Phase 4.4) depends on it, and the PRD mandates bcrypt password hashing for staff accounts.
- Run **Clean and Build** in NetBeans to confirm compilation succeeds.
