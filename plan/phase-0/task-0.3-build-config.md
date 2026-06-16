# Task 0.3 — Configure build.xml & project.properties

**Goal:** Ensure the build configuration produces a correct executable JAR with all dependencies bundled.

---

## Checklist

### Part A: Verify project.properties

- [x] `main.class=hms.HotelManagementApp` (set in task 0.1)
- [x] All 3 JARs in `javac.classpath` (set in task 0.2)
- [x] `run.classpath` inherits from `javac.classpath`
- [x] `dist.jar=${dist.dir}/ead_cw.jar` — output JAR name
- [x] `dist.archive.excludes=` — no exclusions
- [x] `manifest.file=manifest.mf` — manifest will be used

### Part B: Verify build.xml

- [x] Imports `nbproject/build-impl.xml` — inherits all standard targets
- [x] No customizations needed — default `-do-jar` target handles dist via `CopyLibs` task

### Part C: Verify CopyLibs availability (NetBeans build dependency)

- [x] `libs.CopyLibs.classpath` defined in `%APPDATA%\NetBeans\27\build.properties`
- [x] This enables automatic copying of `lib/*.jar` to `dist/lib/` and manifest `Class-Path` generation

### Part D: Verify

- [x] Run **Clean and Build** in NetBeans — *confirmed by user*
- [x] Confirm `dist/ead_cw.jar` is created — *confirmed by user*
- [x] Confirm `dist/lib/` contains the 3 dependency JARs — *confirmed by user*
- [x] Confirm `MANIFEST.MF` in the JAR has correct `Main-Class` and `Class-Path` — *verified: `Main-Class: hms.HotelManagementApp`, `Class-Path: lib/flatlaf-3.5.1.jar lib/mysql-connector-j-9.7.0.jar lib/jbcrypt-0.4.jar`*

---

## Build Output (expected)

```
ead_cw/
├── dist/
│   ├── ead_cw.jar              ← executable JAR
│   └── lib/
│       ├── flatlaf-3.5.1.jar
│       ├── mysql-connector-j-9.7.0.jar
│       └── jbcrypt-0.4.jar
├── lib/                         ← local copy (build source)
│   ├── flatlaf-3.5.1.jar
│   ├── mysql-connector-j-9.7.0.jar
│   └── jbcrypt-0.4.jar
├── build/                       ← build artifacts
└── ...
```

---

## How the dist JAR works

```
┌─ build.xml ─────────────────────────────────────────────────┐
│  imports nbproject/build-impl.xml                           │
│  which contains:                                            │
│                                                             │
│  target -do-jar:                                            │
│    1. Create manifest with Main-Class + Class-Path          │
│    2. Copy lib/*.jar → dist/lib/  (via CopyLibs task)       │
│    3. Package classes into dist/ead_cw.jar                  │
│    4. Result: self-contained executable JAR                 │
└─────────────────────────────────────────────────────────────┘
```
