# Task D10 — Integration & Final Polish (Phase 15)

**Area:** Phase 15 — Final QA  
**Severity:** HIGH — final quality gate before declaring application production-ready

**Dependencies:** All D1–D9 must be complete before starting this task.

## Scope

### 15.1 End-to-end workflow testing

Execute the complete hotel management flow:

```
Guest Registration
  → Create guest (AddGuestDialog)
  → Validate: email/phone/name validation works
  → Edit guest → changes persist
  → Search by name/email/phone

Room Management
  → Add room → appears in table
  → Filter by type, status
  → Edit room details → changes persist
  → Check availability from reservation dialog

Reservation
  → Create reservation with guest + room + dates
  → Verify price calculation (Normal/Seasonal/Corporate)
  → Verify room shows as reserved
  → Check In → GuestCheckInDialog → status = checked_in
  → Book services for reservation
  → Check Out → GuestCheckOutDialog → payment recorded
  → Verify billing: room charges + service charges + tax
  → Verify room status = available

Staff & Room Assignments
  → Create staff member → appears in table
  → Assign room to staff → assignment persists

Billing
  → Adjust bill: add discount, late charge → recalculates
  → Record payment → status = paid

Reports
  → Generate occupancy report
  → Generate invoice report
  → Export to PDF → file opens correctly
```

All status transitions must be verified: `pending → confirmed → checked_in → checked_out` for reservations; `available → reserved → available` for rooms; `pending → paid` for billing.

### 15.2 Input validation sweep

Walk through every dialog and verify error messages for:

| Field Type | Test |
|------------|------|
| Email | Invalid format → error shown |
| Phone | Non-numeric → error shown (after D3 fix) |
| Name | Numbers/special chars → error shown |
| Dates | Past date for check-in → error; check-out before check-in → error |
| Numeric | Negative prices, zero quantities → error |
| Required | Empty required fields → error |
| Length | Over-max-length → error or truncation |

### 15.3 Error handling review

- No uncaught exceptions bubble to user
- All DAO exceptions show user-friendly `JOptionPane.showMessageDialog` (not stack traces)
- Connection failures show retry dialog (not crash)
- Empty tables show "No data found" (not blank or exception)

### 15.4 Theme consistency

- FlatMacDarkLaf applied globally (confirm in `HotelManagementApp.java`)
- Check all panels for unreadable text (dark-on-dark, light-on-light)
- Consistent button spacing and alignment
- Table header styling matches theme

### 15.5 JAR packaging

- Build executable JAR with all dependencies
- Verify `Clean & Build` produces a runnable JAR
- Test JAR on clean machine (no NetBeans)

### 15.6 README.md

Write final `README.md` in project root covering:
- Prerequisites (Java 24, XAMPP, MySQL)
- Database setup: import schema.sql + seed.sql
- Build instructions: Clean & Build in NetBeans
- Run instructions: java -jar dist/ead_cw.jar
- Screenshots of key screens
- Architecture overview (layers, packages)
- Key features with screenshots/demos

### 15.7 Code cleanup

- Remove unused imports (check every `.java` file)
- Remove dead variables, commented-out code
- Verify JavaDoc on all public methods
- Check naming conventions against `code_rules.md`
- Final audit against `code_rules.md` checklist

## Verification

- ✅ Full end-to-end flow works without errors
- ✅ All form validations produce clear error messages
- ✅ No stack traces visible to end user
- ✅ FlatMacDarkLaf theme consistent
- ✅ Clean & Build produces runnable JAR
- ✅ README complete with setup instructions
- ✅ Zero compilation errors, zero warnings

## Checklist

- [ ] Execute end-to-end workflow test
- [ ] File any bugs found as separate task items
- [ ] Sweep all forms for input validation
- [ ] Review error handling in all layers
- [ ] Verify FlatMacDarkLaf theme consistency
- [ ] Build JAR and verify on clean machine
- [ ] Write project-root README.md
- [ ] Code cleanup: unused imports, dead code, JavaDoc
- [ ] Final Clean & Build — zero errors, zero warnings
