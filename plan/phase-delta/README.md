# Phase Delta — Remaining & Remediation Work

**Scope:** All items left incomplete, unwired, or unresolved after Phases 0–15 implementation. Each task fixes a specific gap found during the codebase audit.

## Task Overview

| # | Task | Area | Severity | Dependencies |
|---|------|------|----------|--------------|
| D1 | Wire Check-In / Check-Out / Cancel Dialogs | Phase 10 (Reservation UI) | **HIGH** | ReservationPanel, GuestCheckInDialog, GuestCheckOutDialog, CancellationDialog |
| D2 | Pricing Strategy Selection Logic | Phase 4.6 (Pricing) | **MEDIUM** | ReservationController, BillingController, Guest model |
| D3 | Fix `ValidationUtil.isValidPhone()` Bug | Phase 4.1 (Validation) | **MEDIUM** | ValidationUtil |
| D4 | Dashboard → Observer Wiring | Phase 7 (Dashboard) | **HIGH** | DashboardPanel, DashboardObserver, ReservationController, MainWindow |
| D5 | GuestHistorySubPanel Wiring | Phase 8.3 (Guest UI) | **HIGH** | GuestHistorySubPanel, GuestManagementPanel |
| D6 | Reports Module (Phase 14) | Phase 14 (Reports) | **HIGH** | JasperReports templates, ReportsPanel |
| D7 | Controller Dead Method Cleanup | Phases 5, 9, 12, 13 | **MEDIUM** | All controllers, all panels |
| D8 | MainWindow Menu Bar + Status Bar | Phase 6 (Navigation) | **LOW** | MainWindow |
| D9 | Icon Resource Files | Phase 4.5 (Icons) | **LOW** | None |
| D10 | Integration & Final Polish | Phase 15 (QA) | **HIGH** | All D1–D9 |

## Dependency Graph

```
D1 ──┐
D2 ──┤
D3 ──┤
D4 ──┤
D5 ──┤  ─→ D10 (Integration & Polish)
D6 ──┤
D7 ──┤
D8 ──┤
D9 ──┘
```

All D1–D9 are independent. D10 is the final quality gate.

## Common References

- **Code rules:** `CONTEXT/code_rules.md`
- **Architecture guidelines:** `plan/GUI_BUILDING_REFERENCE.md`
- **Main plan:** `plan/README.md`
