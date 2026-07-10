# Task D8 — MainWindow Menu Bar + Status Bar

**Area:** Phase 6 — Main Window & Navigation (completion)  
**Severity:** LOW — core navigation works via tabs, menus are supplementary

## Problem

`MainWindow.java` has a menu bar with empty "File" and "Edit" menus (no items). The plan (Phase 6.1) specifies:

- **Menu bar**: File (New Reservation, Exit), Edit (Preferences), View (toggle panels), Reports, Help (About)
- **Status bar**: Current date/time, DB connection status indicator

Neither is implemented beyond the bare menu headers.

## Changes Required

### 1. Menu bar population

In `MainWindow.java`, after `initComponents()` or in `setupTabs()`, populate the menus:

| Menu | Item | Action |
|---|---|---|
| File | New Reservation | Switch to Reservations tab, open NewReservationDialog |
| File | Exit | `System.exit(0)` with confirmation |
| Edit | Preferences | (Stretch goal — no preferences system yet) |
| View | Dashboard / Guests / ... | Switch to corresponding tab |
| Help | About | Show version info dialog |

Some action handler stubs already exist (e.g., `newReservationMenuItemActionPerformed`).

### 2. Status bar

Add a `JPanel` at the bottom of the main frame containing:

- `JLabel` — Current date/time (updated via `javax.swing.Timer` every second)
- `JLabel` — DB connection status (green "Connected" / red "Disconnected")
- `JLabel` — Current user (stretch — "Logged in as: Admin")

### 3. Layout

The status bar should be a `JPanel` with `BorderLayout` or `FlowLayout(LEFT)` added to the frame's `BorderLayout.SOUTH` region (the content area uses `CENTER`).

## Verification

1. Launch app → menu shows File → New Reservation, Exit
2. Click File → New Reservation → Reservations tab activates
3. Status bar shows live clock updating each second
4. Status bar shows DB connection status
5. Click File → Exit → confirmation dialog → quits

## Checklist

- [ ] Populate File menu with New Reservation + Exit actions
- [ ] Wire existing action handler stubs
- [ ] Add status bar panel with date/time label
- [ ] Add DB connection status label to status bar
- [ ] Add Timer for clock updates
- [ ] Verify Clean & Build
