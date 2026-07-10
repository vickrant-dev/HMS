# Task D4 — Dashboard → Observer Wiring

**Area:** Phase 7 — Dashboard Panel (completion)  
**Severity:** HIGH — Dashboard displays static/hardcoded data and never refreshes

## Problem

The Observer infrastructure is complete on the controller side:

| Component | Status |
|-----------|--------|
| `DashboardObserver` interface | ✅ Exists (`@FunctionalInterface` with `onReservationCreated`, `onCheckIn`, `onCheckOut`) |
| `ReservationController` observers list | ✅ Exists (holds `List<DashboardObserver>`, has `addObserver`/`removeObserver`/notify methods) |
| `DashboardPanel` | ❌ **Does not implement `DashboardObserver`** — has no controller integration whatsoever |
| `MainWindow.setupTabs()` | ❌ **Never registers observers** — no `addObserver()` call anywhere |

`DashboardPanel` currently shows hardcoded values: occupancy "84.56%", revenue "$12,450.00", pending "12".

## Changes Required

### 1. `src/hms/view/panels/DashboardPanel.java`

- Add `implements DashboardObserver` to class declaration
- Import `hms.controller.DashboardObserver`, `hms.model.Reservation`
- Implement the three observer methods:

```java
@Override
public void onReservationCreated(Reservation reservation) {
    // Refresh occupancy and pending reservation counts
    loadPendingCheckIns();
    loadOccupancyRate();
}

@Override
public void onCheckIn(Reservation reservation) {
    loadPendingCheckIns();
    loadOccupancyRate();
}

@Override
public void onCheckOut(Reservation reservation) {
    loadPendingCheckIns();
    loadOccupancyRate();
    loadTodayRevenue();
}
```

- Add private helper methods that query controllers for real data:
  - `loadOccupancyRate()` — query `RoomDAO` for occupied/total
  - `loadTodayRevenue()` — query `BillingController` for today's revenue
  - `loadPendingCheckIns()` — query `ReservationController` for today's check-ins
  - `loadRoomStatusDistribution()` — query `RoomDAO` for status breakdown

### 2. `src/hms/view/MainWindow.java`

In `setupTabs()`, after creating `dashboardPanel`, add:

```java
reservationController.addObserver((DashboardObserver) dashboardPanel);
```

If `BillingController` also needs observer support, extend the interface and register similarly.

## Verification

1. Launch app → Dashboard shows real (not hardcoded) stats
2. Create a new reservation → Dashboard occupancy/pending count updates
3. Check in a guest → Dashboard pending count decreases
4. Check out a guest → Dashboard revenue + occupancy updates

## Checklist

- [ ] Make `DashboardPanel` implement `DashboardObserver`
- [ ] Implement `onReservationCreated()`, `onCheckIn()`, `onCheckOut()`
- [ ] Add private helper methods for real data loading
- [ ] Register observer in `MainWindow.setupTabs()`
- [ ] Import `ReservationController` and other necessary controllers in DashboardPanel
- [ ] Verify Clean & Build
