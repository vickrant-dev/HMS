# Task 0.1 — Restructure Package Hierarchy

**Goal:** Create root package `hms` with sub-packages for MVC layering, rename main class, update NetBeans config.

---

## Checklist

### Part A: Create root + sub-packages

- [x] Rename `src/ead_cw/` → `src/hms/`
- [x] Create package `hms.config`
- [x] Create package `hms.database`
- [x] Create package `hms.model`
- [x] Create package `hms.dao`
- [x] Create package `hms.controller`
- [x] Create package `hms.view`
- [x] Create package `hms.view.panels`
- [x] Create package `hms.view.dialogs`
- [x] Create package `hms.util`
- [x] Create package `hms.exception`
- [x] Create package `hms.service`

### Part B: Rename main class + update config

- [x] Rename `Ead_cw.java` → `HotelManagementApp.java`
- [x] Update class declaration: `class Ead_cw` → `class HotelManagementApp`
- [x] Update package declaration: `package ead_cw` → `package hms`
- [x] Update `project.properties`: `main.class=ead_cw.Ead_cw` → `main.class=hms.HotelManagementApp`
- [x] Update theme: `FlatMacLightLaf` → `FlatMacDarkLaf`

### Part C: Verify

- [ ] `HotelManagementApp.java` compiles without errors
- [ ] 11 empty package folders exist under `src/hms/`
- [x] Project runs and shows the basic FlatLaf window

---

## Structure After Completion

```
src/hms/
├── HotelManagementApp.java    ← renamed, package hms
├── config/
├── database/
├── model/
├── dao/
├── controller/
├── view/
│   ├── panels/
│   └── dialogs/
├── util/
├── exception/
└── service/
```
