# Hotel Management System — Development Plan

**Course:** Enterprise Application Development 1 (DSE 25.2)  
**IDE:** NetBeans | **Java:** 24 | **Theme:** FlatMacDarkLaf

---

## Phase 0: Project Foundation & Tooling

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 0.1 | Restructure package hierarchy | Create sub-packages under `ead_cw`: `.model`, `.dao`, `.controller`, `.view`, `.view.panels`, `.view.dialogs`, `.util`, `.config`, `.database`, `.exception`, `.service` | — |
| 0.2 | Set up `lib/` folder | Place JARs: FlatLaf 3.5.1, MySQL Connector 9.7.0, jBCrypt, JasperReports 7.0.6 | — |
| 0.3 | Configure build.xml & project.properties | Add all JARs to classpath, set main class `hms.HotelManagementApp`, configure dist JAR | 0.2 |
| 0.4 | Create `database/` folder | Placeholder for `schema.sql` and `seed.sql` | — |
| 0.5 | Create `Constants.java` | Centralized constants: DB credentials, UI dimensions, validation limits, status enums, tax/pricing, error messages | — |
| 0.6 | Create custom exceptions | `ApplicationException`, `DatabaseException`, `ValidationException`, `ReservationException`, `BillingException` (required — DAOs and controllers depend on these for typed error handling; without them, all layers would lose structured exception propagation) | — |
| 0.7 | Create `DatabaseConnection.java` | Singleton with retry logic (3 attempts, 2s delay), try-with-resources pattern | 0.5, 0.6 |
| 0.8 | Create `HotelManagementApp.java` | Main entry point, FlatMacDarkLaf setup, launch `MainWindow` | 0.7 |
| 0.9 | Initialize Git repo | `.gitignore` for NetBeans/build artifacts, initial commit | — |

**Deliverable:** Runnable skeleton app that loads FlatLaf theme and shows empty main window.

---

## Phase 1: Database Schema & Seed Data

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 1.1 | Write `schema.sql` | All 8 tables: `guests`, `rooms`, `reservations`, `billing`, `staff`, `services`, `service_bookings`, `room_assignments` with constraints, indexes, ENUMs | — |
| 1.2 | Write `seed.sql` | 10-15 sample guests, 20+ rooms (Single/Double/Suite), 5 staff members, 8 services, 10 sample reservations with billing, service bookings and room assignments | 1.1 |

**Deliverable:** Executable SQL scripts for full database creation and test data population.

---

## Phase 2: Data Models (POJOs) — All Entities

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 2.1 | `Guest.java` | Fields: guestId, firstName, lastName, email, phone, address, idProofType, idProofNumber, dateOfBirth, createdAt | — |
| 2.2 | `Room.java` | Fields: roomId, roomNumber, roomType, capacity, basePrice, status, floor, createdAt | — |
| 2.3 | `Reservation.java` | Fields: reservationId, guest (Guest), room (Room), checkInDate, checkOutDate, bookingDate, numberOfGuests, status, totalAmount, createdAt | 2.1, 2.2 |
| 2.4 | `Billing.java` | Fields: billingId, reservation (Reservation), roomCharge, serviceCharge, otherCharges, taxAmount, totalBill, paymentStatus, paymentDate, notes | 2.3 |
| 2.5 | `Staff.java` | Fields: staffId, firstName, lastName, email, phone, position, department, salary, joiningDate, status, passwordHash, createdAt | — |
| 2.6 | `Service.java` | Fields: serviceId, serviceName, serviceType, price, description, isAvailable, createdAt | — |
| 2.7 | `ServiceBooking.java` | Fields: serviceBookingId, reservation (Reservation), service (Service), bookingDate, quantity, totalPrice, status | 2.3, 2.6 |
| 2.8 | `RoomAssignment.java` | Fields: assignmentId, room (Room), staff (Staff), assignmentDate, assignmentType, status, notes | 2.2, 2.5 |

**Deliverable:** 8 immutable POJO classes following code_rules.md standards (private final fields, getters, no setters, JavaDoc on constructors/getters).

---

## Phase 3: Data Access Layer (DAOs)

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 3.1 | `GuestDAO.java` | CRUD: save, getById, getAll, update, delete (hard), searchByName/Email/Phone | 2.1, 0.7 |
| 3.2 | `RoomDAO.java` | CRUD: save, getById, getAll, update, delete, filterByStatus/Type/PriceRange, checkAvailability(dates) | 2.2, 0.7 |
| 3.3 | `ReservationDAO.java` | CRUD + getByGuestId, getByRoomId, getByDateRange, getByStatus, updateStatus, getTodayCheckIns/CheckOuts | 2.3, 0.7 |
| 3.4 | `BillingDAO.java` | CRUD: save, getByReservationId, updatePaymentStatus, getAll, getRevenueByDateRange | 2.4, 0.7 |
| 3.5 | `StaffDAO.java` | CRUD: save, getById, getAll, update, delete, filterByDepartment/Position/Status | 2.5, 0.7 |
| 3.6 | `ServiceDAO.java` | CRUD: save, getById, getAll, update, delete, filterByType/Availability | 2.6, 0.7 |
| 3.7 | `ServiceBookingDAO.java` | CRUD + getByReservationId, calculateServiceCharges(reservationId) | 2.7, 0.7 |
| 3.8 | `RoomAssignmentDAO.java` | CRUD + getByRoomId, getByStaffId, updateStatus, getByDate | 2.8, 0.7 |

**All DAOs:** PreparedStatements exclusively, try-with-resources, custom DatabaseException wrapping SQLException, never return null collections.

**Deliverable:** Complete data access layer for all 8 entities.

---

## Phase 4: Utilities & Services

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 4.1 | `ValidationUtil.java` | Email, phone, name, date (future/past), numeric range, string length validators | — |
| 4.2 | `DateUtil.java` | Calculate nights between dates, format dates, date range helpers | — |
| 4.3 | `StringUtil.java` | Truncate, capitalize, sanitize input, reservation ID generation (RES-YYYYMMDD-XXXXX) | — |
| 4.4 | `PasswordUtil.java` | bcrypt hash + verify (using jBCrypt) | — |
| 4.5 | `IconUtil.java` | Load icons for UI buttons/actions | — |
| 4.6 | Pricing Strategy pattern | `PricingStrategy` interface + `NormalPricingStrategy`, `SeasonalPricingStrategy`, `CorporatePricingStrategy` | 2.2 |
| 4.7 | `ReportUtil.java` | JasperReports helper: load report, set parameters, fill, export to PDF | JasperReports lib |

**Deliverable:** Reusable utility classes and pricing strategy implementations.

---

## Phase 5: Controllers

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 5.1 | `GuestController.java` | Orchestrate guest CRUD, validation before DAO calls, coordinate search | 3.1, 4.1 |
| 5.2 | `RoomController.java` | Orchestrate room CRUD, availability check, status transitions | 3.2, 4.1 |
| 5.3 | `ReservationController.java` | Create/modify/cancel reservations, check-in/check-out workflow, room status updates, observer notifications | 3.3, 3.2, 3.4, 4.2, 4.6 |
| 5.4 | `BillingController.java` | Bill generation (room+service+tax), payment recording, adjustments, receipt data | 3.4, 3.7 |
| 5.5 | `StaffController.java` | Staff CRUD, room assignment management | 3.5, 3.8 |
| 5.6 | `ServiceController.java` | Service catalog mgmt, service booking linked to reservations | 3.6, 3.7 |

**Deliverable:** 6 controllers with business logic, validation, exception handling, and observer pattern for dashboard updates.

---

## Phase 6: Main Window & Navigation

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 6.1 | `MainWindow.java` | JFrame with menu bar (Guests, Rooms, Reservations, Billing, Services, Staff, Reports, Dashboard), tabbed navigation panel, status bar with date/time + DB connection status | 0.8 |
| 6.2 | Module panel wiring | Wire each menu item to display corresponding panel in content area | All phase 5 controllers |

**NetBeans note:** This phase uses hand-coded Swing (no `.form` file) since complex programmatic layout is needed for the main frame.

**Deliverable:** Main application window with functional navigation and status bar.

---

## Phase 7-13: UI Modules (NetBeans Form Editor)

> **Important:** All UI panels and dialogs in these phases are built using **NetBeans' Matisse GUI builder**, which generates paired `.java` + `.form` files. The `.form` files define the visual layout; the `.java` files contain event handlers and controller bindings. Do NOT hand-code Swing layouts for these — use the NetBeans Design view.

| Phase | # | Task | Description | Dependencies |
|-------|---|------|-------------|--------------|
| **7** | 7.1 | `DashboardPanel.java` (+ `.form`) | Occupancy gauge, revenue cards (Today/Month/YTD), check-in/check-out counters, room status distribution, pending reservations list, real-time refresh via Observer pattern | 5.3, 5.4, 3.2, 3.3 |
| **8** | 8.1 | `GuestPanel.java` (+ `.form`) | Table with pagination, search bar (name/email/phone), filter options, action buttons (Add/Edit/Delete/View History) | 5.1 |
| **8** | 8.2 | `GuestDialog.java` (+ `.form`) | Modal form: name, email, phone, address, ID proof, DOB; bind validation on save | 5.1, 4.1 |
| **8** | 8.3 | Guest History view | Sub-panel showing past reservations, total nights, avg spending for selected guest | 5.1, 3.3 |
| **9** | 9.1 | `RoomPanel.java` (+ `.form`) | Table/grid with status color-coding, filter by status/type/capacity/price, Add/Edit/Delete buttons | 5.2 |
| **9** | 9.2 | Room dialog (+ `.form`) | Modal for adding/editing room: number, type, capacity, price, floor | 5.2 |
| **10** | 10.1 | `ReservationPanel.java` (+ `.form`) | Reservation list with status badges, search by ID/guest/room, quick-action buttons (Check-in, Check-out, Cancel, Modify) | 5.3 |
| **10** | 10.2 | `ReservationDialog.java` (+ `.form`) | Create/modify: select guest, room, dates, guest count; real-time availability + price calc | 5.3, 4.1, 4.2, 4.6 |
| **10** | 10.3 | `CheckInDialog.java` (+ `.form`) | Verify reservation, confirm check-in, update statuses | 5.3 |
| **10** | 10.4 | `CheckOutDialog.java` (+ `.form`) | Finalize billing, show itemized bill, record payment, update statuses | 5.3, 5.4 |
| **10** | 10.5 | Cancellation dialog (+ `.form`) | Cancel with reason, update room availability | 5.3 |
| **11** | 11.1 | `BillingPanel.java` (+ `.form`) | Bill list with payment status, filter by date/status, view itemized bill details | 5.4 |
| **11** | 11.2 | Payment dialog (+ `.form`) | Record payment method (Cash/Card/Bank Transfer), amount, update status | 5.4 |
| **11** | 11.3 | Adjustment dialog (+ `.form`) | Add discounts, late charges, complimentary items, notes | 5.4 |
| **12** | 12.1 | `ServicePanel.java` (+ `.form`) | Service catalog table, enable/disable toggle, Add/Edit/Delete | 5.6 |
| **12** | 12.2 | Service booking dialog (+ `.form`) | Book services for a reservation, quantity, auto-calculate price | 5.6 |
| **12** | 12.3 | Service dialog (+ `.form`) | Add/edit service: name, type, price, description, availability | 5.6 |
| **13** | 13.1 | `StaffPanel.java` (+ `.form`) | Staff directory table, filter by department/position/status, Add/Edit/Delete | 5.5 |
| **13** | 13.2 | `StaffDialog.java` (+ `.form`) | Registration form: name, email, phone, position, department, salary, joining date, status | 5.5 |
| **13** | 13.3 | Room assignment panel (+ `.form`) | Assign staff tasks to rooms, track completion status | 5.5, 3.8 |

**Deliverable:** 7 complete UI modules with NetBeans `.form` files, event handlers, and controller wiring.

---

## Phase 14: Reporting Module

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 14.1 | JasperReports setup | Place `.jrxml` report templates in `reports/`, compile to `.jasper` | — |
| 14.2 | Guest Invoice Report (Report 1) | Multi-table: guests, reservations, rooms, billing, services; itemized charges, date range filtering, PDF export | 4.7, DB schema |
| 14.3 | Occupancy & Revenue Report (Report 2) | Multi-table: reservations, rooms, billing, staff, service_bookings; occupancy %, revenue/room type, peak periods, staff metrics, monthly trends, filterable | 4.7, DB schema |
| 14.4 | `ReportsPanel.java` (+ `.form`) | Report type dropdown, date range picker, filter options, Generate + Export buttons, preview area | 14.2, 14.3 |

**Deliverable:** 2 comprehensive JasperReports with PDF export, filtering, and preview.

---

## Phase 15: Integration & Final Polish

| # | Task | Description | Dependencies |
|---|------|-------------|--------------|
| 15.1 | End-to-end workflow testing | Guest → Reservation → Service booking → Check-in → Check-out → Bill generation → Report; verify all status transitions | All phases |
| 15.2 | Input validation sweep | Verify all forms validate: email, phone, dates, numeric fields, mandatory fields, length limits | All phases |
| 15.3 | Error handling review | Ensure all exceptions show user-friendly dialogs, no stack traces to end-user | All phases |
| 15.4 | Theme consistency check | Verify FlatMacDarkLaf applied across all panels, consistent spacing/buttons/colors | All phases |
| 15.5 | JAR packaging | Build executable `ead_cw.jar` with all dependencies bundled | 0.3 |
| 15.6 | `README.md` | Setup instructions: XAMPP, schema import, run JAR; screenshots, usage guide | All phases |
| 15.7 | Code cleanup | Remove dead code, verify JavaDoc on public methods, check naming conventions, final review against code_rules.md checklist | All phases |

**Deliverable:** Complete, tested, packaged application ready for submission.

---

## Dependency Graph

```
Phase 0 ─────────────────────────────────────────────────────────────────┐
  └── Phase 1 (schema + seed) ─────────────────────────────────────────┐ │
  └── Phase 2 (models) ───────────────────────────────────────────────┐│ │
      └── Phase 3 (DAOs) ────────────────────────────────────────────┐││ │
          └── Phase 4 (utilities) ──────────────────────────────────┐│││ │
              └── Phase 5 (controllers) ──────────────────────────┐ ││││ │
                  ├── Phase 6 (main window + nav) ◄───────────────┼─┼┼┼┼─┘
                  ├── Phase 7 (dashboard) ◄───────────────────────┼─┼┼┼┘
                  ├── Phase 8 (guest UI) ◄────────────────────────┼─┼┼┘
                  ├── Phase 9 (room UI) ◄─────────────────────────┼─┼┘
                  ├── Phase 10 (reservation UI) ◄─────────────────┼─┘
                  ├── Phase 11 (billing UI) ◄─────────────────────┘
                  ├── Phase 12 (service UI) ◄─────────────────────┐
                  ├── Phase 13 (staff UI) ◄───────────────────────┤
                  └── Phase 14 (reports) ◄────────────────────────┤
                      └── Phase 15 (integration + polish) ◄───────┘
```

---

## Key Architectural Decisions

| Aspect | Decision |
|--------|----------|
| **Root package** | `hms` with sub-packages per layer |
| **UI construction** | NetBeans Form Editor (`.java` + `.form` pairs) for all panels; hand-coded Swing only for MainWindow |
| **UI theme** | FlatMacDarkLaf (`com.formdev.flatlaf.themes.FlatMacDarkLaf`) |
| **DB connection** | Singleton pattern with retry (3 attempts, 2s delay) |
| **SQL safety** | PreparedStatements exclusively |
| **Password hashing** | jBCrypt (bcrypt, 12 rounds) |
| **Reports** | JasperReports 7.0.6 with `.jrxml` + `.jasper` compiled templates |
| **Pricing** | Strategy pattern (Normal, Seasonal, Corporate) |
| **Dashboard updates** | Observer pattern (controllers notify DashboardPanel) |
| **Error handling** | Custom exception hierarchy (ApplicationException → DatabaseException, ValidationException, ReservationException, BillingException), user-friendly dialogs |
| **Code style** | 4-space indent, 100-char line limit, JavaDoc on public methods |
