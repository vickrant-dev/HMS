# HMS GUI Building Reference — Phase 6 to 14

> **Purpose:** Detailed implementation specification for every GUI panel and dialog in the Hotel Management System. Use this as a blueprint when building each `.java` + `.form` pair in NetBeans.
>
> **Tool:** NetBeans Matisse GUI Builder (Design view) for all `.form` files. Only `MainWindow.java` is hand-coded.
>
> **Theme:** `FlatMacDarkLaf` — all colors, borders, spacing come from the theme. Do NOT hardcode colors.
>
> **Convention:** All panels use `BorderLayout` as top-level layout unless specified otherwise. Standard padding: `BorderFactory.createEmptyBorder(10, 10, 10, 10)`.

---

## Table of Contents

1. [Phase 6 — MainWindow](#phase-6--mainwindow)
2. [Phase 7 — DashboardPanel](#phase-7--dashboardpanel)
3. [Phase 8 — Guest Management](#phase-8--guest-management)
4. [Phase 9 — Room Management](#phase-9--room-management)
5. [Phase 10 — Reservation Management](#phase-10--reservation-management)
6. [Phase 11 — Billing](#phase-11--billing)
7. [Phase 12 — Services](#phase-12--services)
8. [Phase 13 — Staff](#phase-13--staff)
9. [Phase 14 — ReportsPanel](#phase-14--reportspanel)
10. [Appendix: Reusable UI Patterns](#appendix-reusable-ui-patterns)

---

## Phase 6 — MainWindow

**File:** `hms.view.MainWindow.java` (hand-coded, NO `.form` file)

**Extends:** `JFrame`

### Layout Structure

```
┌─────────────────────────────────────────────────────────┐
│ [Menu Bar]                                              │
├─────────────────────────────────────────────────────────┤
│                                                         │
│   ┌─────────────────────────────────────────────────┐   │
│   │                                                 │   │
│   │           Tabbed Pane (content area)             │   │
│   │     Switches between GuestPanel, RoomPanel,      │   │
│   │     ReservationPanel, BillingPanel,              │   │
│   │     ServicePanel, StaffPanel, ReportsPanel,      │   │
│   │     DashboardPanel                               │   │
│   │                                                 │   │
│   └─────────────────────────────────────────────────┘   │
│                                                         │
├─────────────────────────────────────────────────────────┤
│ [Status Bar: Date/Time | DB Connection Indicator]       │
└─────────────────────────────────────────────────────────┘
```

### Menu Bar Items

| Menu | Items |
|------|-------|
| **Dashboard** | Show Dashboard (single click, no dropdown) |
| **Guests** | Guest Management, separator, Exit |
| **Rooms** | Room Management |
| **Reservations** | New Reservation, Manage Reservations, separator, Check-in, Check-out |
| **Billing** | Billing Overview, Payments |
| **Services** | Service Catalog, Service Bookings |
| **Staff** | Staff Directory, Room Assignments |
| **Reports** | Guest Invoice, Occupancy & Revenue |

### Components to Create

**Menu bar (`JMenuBar`):**
- `JMenu` for each module: Dashboard, Guests, Rooms, Reservations, Billing, Services, Staff, Reports
- Each menu has `JMenuItem`s as listed above

**Content area:**
- `JTabbedPane` (or a `CardLayout` JPanel) — used to display module panels
- Default tab on launch: Dashboard

**Status bar:**
- `JPanel` with `FlowLayout(LEFT)` containing:
  - `JLabel` — Shows current date/time (updated via `javax.swing.Timer` every second)
  - `JLabel` — Shows DB connection status: green circle icon + "Connected" or red circle icon + "Disconnected"
  - Separator (vertical line or `JSeparator`)
  - `JLabel` — "Hotel Management System v1.0"

### Controller Wiring (6.2)

- On `actionPerformed` of each menu item, switch the content area to the corresponding panel
- Create all panel instances ONCE in the constructor, cache them, and show/hide via `CardLayout.show()`
- Register `DashboardPanel` as an observer on controllers that need it (ReservationController, BillingController)

### Window Settings

- Title: `Constants.APP_TITLE` ("Hotel Management System")
- Default size: `Constants.WINDOW_WIDTH` x `Constants.WINDOW_HEIGHT` (1400 x 900)
- Minimum size: 1024 x 768
- `setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)`
- Center on screen: `setLocationRelativeTo(null)`
- Maximized by default: `setExtendedState(JFrame.MAXIMIZED_BOTH)`

---

## Phase 7 — DashboardPanel

**File:** `hms.view.panels.DashboardPanel.java` + `.form`

**Extends:** `JPanel`

**Implements:** `DashboardObserver` (see code_rules.md §12.3)

**Parent layout:** `BorderLayout`

### Visual Layout

```
┌───────────────────────────────────────────────────────────────┐
│  DASHBOARD                                          [Refresh] │
├───────────────────────────────────────────────────────────────┤
│ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐  │
│ │  Occupancy  │ │Revenue TM  │ │Revenue MTH │ │ Revenue YTD│  │
│ │    72%      │ │  $2,450    │ │  $18,200   │ │  $124,500  │  │
│ │ [gauge]     │ │  Today     │ │ This Month │ │ Year to... │  │
│ └────────────┘ └────────────┘ └────────────┘ └────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────┐  ┌──────────────────────────────────┐│
│ │  TODAY'S ACTIVITY    │  │  ROOM STATUS                     ││
│ │  Check-ins:  3       │  │  ┌────┬────┬────┬────┐          ││
│ │  Check-outs: 2       │  │  │Avai│Occu│Maint│Resr│          ││
│ │  New Bookings: 4     │  │  │ 12 │ 15 │  2  │  3 │          ││
│ │                      │  │  └────┴────┴────┴────┘          ││
│ │                      │  │  Legend: color-coded bars        ││
│ └──────────────────────┘  └──────────────────────────────────┘│
├───────────────────────────────────────────────────────────────┤
│  PENDING RESERVATIONS (Next 24h)                              │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ RES-20260625-001 | John Doe | Deluxe Suite | Jun 25      │ │
│  │ RES-20260625-002 | Jane Smith | Standard Room | Jun 25   │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────────────────────────────────────────────┘
```

### Component Breakdown

**Top header row:**
- `JLabel` "Dashboard" — large bold font (18pt or heading style)
- `JButton` "Refresh" — align right, triggers `refreshAll()`

**Revenue Cards Row (4 cards):**

Each card is a `JPanel` with:
- `TitledBorder` or styled border with rounded look
- Top: Large number `JLabel` (the value)
- Bottom: Smaller `JLabel` (description: "Today", "This Month", "Year to Date")
- Subtle icon or background tint per card

Cards:
1. **Occupancy** — Shows percentage (e.g., "72%"), a small horizontal progress bar below the number
2. **Revenue Today** — Currency formatted number
3. **Revenue This Month** — Currency formatted number
4. **Revenue Year to Date** — Currency formatted number

**Middle row (split into two halves):**

Left half — **"Today's Activity" panel:**
- `JLabel` header "Today's Activity" (bold)
- Check-ins counter: icon + `JLabel` "Check-ins: 3"
- Check-outs counter: icon + `JLabel` "Check-outs: 2"
- New bookings counter: icon + `JLabel` "New Bookings: 4"
- Each counter is a row with a small colored icon and label

Right half — **"Room Status" panel:**
- `JLabel` header "Room Status" (bold)
- A 2x4 grid or horizontal bar display:
  - Available: count, green indicator
  - Occupied: count, red indicator
  - Maintenance: count, orange indicator
  - Reserved: count, blue indicator
- Could use simple colored `JPanel` segments or a custom drawn bar

**Bottom section — "Pending Reservations" (Next 24 hours):**
- `JLabel` header "Pending Reservations (Next 24h)"
- `JList` or `JTable` (single column, no header) showing:
  - Reservation display ID | Guest name | Room type | Check-in date
  - Each row styled with consistent padding

### DashboardObserver Implementation

```java
public interface DashboardObserver {
    void onReservationCreated(Reservation reservation);
    void onCheckIn(Reservation reservation);
    void onCheckOut(Reservation reservation);
    void onPaymentRecorded(Billing billing);
}
```

Each method calls `refreshAll()` or targeted update methods.

### Methods

| Method | Description |
|--------|-------------|
| `refreshAll()` | Reload all data from controllers: occupancy %, revenue figures, today's counts, pending list |
| `updateOccupancy()` | Recalculate and update occupancy card |
| `updateRevenue()` | Reload revenue figures for today/month/YTD |
| `updateTodayCounts()` | Reload check-in, check-out, and booking counts |
| `updatePendingReservations()` | Refresh the pending reservations list |
| `refreshOnTimer()` | Optional: auto-refresh every 60 seconds via Timer |

### Data Sources (through controllers)

- Occupancy: `roomController.getOccupancyRate()` — counts rooms with status "occupied" / total rooms
- Revenue today/month/YTD: `billingController.getRevenueByDateRange(...)`
- Check-ins today: `reservationController.getTodayCheckIns()`
- Check-outs today: `reservationController.getTodayCheckOuts()`
- New bookings today: `reservationController.getBookingsByDate(today)`
- Room status counts: `roomController.getRoomCountsByStatus()`
- Pending reservations: `reservationController.getPendingReservations(24h)`

---

## Phase 8 — Guest Management

### 8.1 GuestPanel

**File:** `hms.view.panels.GuestPanel.java` + `.form`

**Extends:** `JPanel`

**Parent layout:** `BorderLayout`

#### Layout

```
┌───────────────────────────────────────────────────────────────┐
│  GUEST MANAGEMENT                                   [Add]     │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────┐  │
│ │ [Search: ________________________] [Search] [Clear]      │  │
│ │ Filter: [All Fields ▼]           Status: [Any ▼]        │  │
│ └──────────────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────┐  │
│ │  # │ Name          │ Email           │ Phone     │ DOB   │  │
│ │ ───┼───────────────┼─────────────────┼───────────┼────── │  │
│ │  1 │ John Doe      │ john@test.com   │ 0771234567│ 1990… │  │
│ │  2 │ Jane Smith    │ jane@test.com   │ 0777654321│ 1992… │  │
│ │ ...│               │                 │           │       │  │
│ │                                                    Page 1│  │
│ └──────────────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ [Edit] [Delete] [View History]    Records: 15    < 1 2 3 >   │
└───────────────────────────────────────────────────────────────┘
```

#### Components

**Header row:**
- `JLabel` "Guest Management" (bold, large)
- `JButton` "Add Guest" — primary action style (blue background)

**Search/Filter bar:**
- `JTextField` for search query (placeholder text: "Search by name, email, or phone...")
- `JButton` "Search"
- `JButton` "Clear"
- `JComboBox` filter field selector: ["All Fields", "Name", "Email", "Phone"]
- `JComboBox` status filter: ["Any", "Active", "Inactive"] *(if guest has soft-delete)*

**Table area:**
- `JTable` inside a `JScrollPane`
- Columns: `#`, `First Name`, `Last Name`, `Email`, `Phone`, `ID Proof Type`, `ID Proof Number`, `DOB`, `Created At`
- Sortable columns (enable `setAutoCreateRowSorter(true)`)
- Alternating row colors (FlatLaf does this by default)

**Bottom bar:**
- `JButton` "Edit" — enabled only when exactly one row is selected
- `JButton` "Delete" — enabled only when exactly one row is selected, shows confirmation dialog
- `JButton` "View History" — enabled when one row selected, opens Guest History sub-panel
- `JLabel` "Records: {count}" — shows total matching record count
- Pagination controls: `<<` `<` `[Page X of Y]` `>` `>>`
- `JComboBox` or `JSpinner` for page size: [10, 25, 50, 100]

#### Methods

| Method | Description |
|--------|-------------|
| `loadGuests()` | Load all guests from `guestController.getAllGuests()`, display in table with pagination |
| `searchGuests()` | Call `guestController.searchByName/Email/Phone(query)` based on filter selection, update table |
| `clearSearch()` | Reset search field to empty, reload all guests |
| `addGuest()` | Open `GuestDialog` in CREATE mode |
| `editGuest()` | Open `GuestDialog` in EDIT mode with selected guest's data pre-filled |
| `deleteGuest()` | Show confirmation dialog, then call `guestController.deleteGuest(id)` if confirmed |
| `viewGuestHistory()` | Load selected guest's history into the history sub-panel (show in same panel area or separate view) |
| `refreshTable()` | Repopulate the table with current data |

#### Event Handling

- Search button / Enter key in search field → `searchGuests()`
- Clear button → `clearSearch()`
- Add button → `addGuest()`
- Edit button (or double-click row) → `editGuest()`
- Delete button → `deleteGuest()`
- View History button → `viewGuestHistory()`
- Pagination buttons → reload page

---

### 8.2 GuestDialog

**File:** `hms.view.dialogs.GuestDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  {Add / Edit} Guest                                  │
├───────────────────────────────────────────────────────┤
│  First Name:*  [________________________]             │
│  Last Name:*   [________________________]             │
│  Email:*       [________________________]             │
│  Phone:*       [________________________]             │
│  Address:      [________________________]             │
│                [________________________]             │
│  ID Proof Type:[Passport          ▼]                  │
│  ID Proof No:  [________________________]             │
│  Date of Birth:[        ] [Choose...]                 │
│                                                       │
│  * Required fields                                    │
├───────────────────────────────────────────────────────┤
│           [Save]         [Cancel]                     │
└───────────────────────────────────────────────────────┘
```

#### Components

- `JTextField` firstName (max 100 chars)
- `JTextField` lastName (max 100 chars)
- `JTextField` email (max 150 chars)
- `JTextField` phone (max 15 chars)
- `JTextArea` address (3-4 rows, wrapped)
- `JComboBox` idProofType: ["Passport", "Driving License", "National ID", "Other"]
- `JTextField` idProofNumber (max 50 chars)
- `JFormattedTextField` or date picker for dateOfBirth (use `JXDatePicker` if available in NetBeans, otherwise three `JSpinner`s for day/month/year or a simple `JTextField` with date validation)

**Bottom buttons:**
- `JButton` "Save" — default button (responds to Enter)
- `JButton` "Cancel" — responds to Escape

#### Mode

Dialog operates in two modes controlled by a constructor parameter or setter:

| Mode | Title | Behavior |
|------|-------|----------|
| **CREATE** | "Add Guest" | Empty fields, on Save → `guestController.createGuest(guest)` |
| **EDIT** | "Edit Guest" | Fields pre-filled from selected Guest, on Save → `guestController.updateGuest(guest)` |

#### Methods

| Method | Description |
|--------|-------------|
| `GuestDialog(Window owner, String mode, GuestController controller)` | Constructor |
| `void setGuest(Guest guest)` | Pre-fill fields for EDIT mode |
| `Guest getGuest()` | Return the Guest object constructed from form fields |
| `boolean showDialog()` | Display dialog modally, return true if Save was clicked |
| `void validateInput()` | Check required fields, email format, phone format, date validity. Show error dialog if invalid. |

#### Validation Rules (in `validateInput()`)

- firstName, lastName: required, letters/spaces only, 2-100 chars
- email: required, valid email regex, max 150 chars
- phone: required, digits+allowed chars, max 15 chars
- idProofNumber: optional, max 50 chars
- dateOfBirth: optional, must be past date (not today or future)

If validation fails, show `JOptionPane.ERROR_MESSAGE` with specific field errors.

---

### 8.3 Guest History Sub-panel

**No separate `.form` file** — this is a sub-panel embedded in `GuestPanel`, shown in the same content area (or a side panel) when "View History" is clicked.

#### Layout

```
┌──────────────────────────────────────────────────────────┐
│  GUEST HISTORY — John Doe                                │
├──────────────────────────────────────────────────────────┤
│  Total Stays: 5    Total Nights: 12    Avg Spend/Night:  │
│  $85.50    Total Spend: $1,026.00                        │
├──────────────────────────────────────────────────────────┤
│  Reservations:                                           │
│  ┌──────┬──────────┬────────┬──────────┬───────┬───────┐ │
│  │ Res# │ Check-in │ Check-│ Room     │Amount │Status │ │
│  │      │          │ out   │          │       │       │ │
│  ├──────┼──────────┼────────┼──────────┼───────┼───────┤ │
│  │RES.. │2026-05-10│05-12  │Deluxe     │$350   │CO     │ │
│  │RES.. │2026-04-01│04-03  │Standard   │$200   │CO     │ │
│  └──────┴──────────┴────────┴──────────┴───────┴───────┘ │
└──────────────────────────────────────────────────────────┘
```

#### Components

- Summary stats row:
  - `JLabel` "Total Stays: {count}", "Total Nights: {count}", "Avg Spend/Night: {amount}", "Total Spend: {amount}"
- `JTable` inside `JScrollPane`:
  - Columns: Reservation #, Check-in, Check-out, Room, Total Amount, Status
  - Read-only, no pagination needed (history is typically small per guest)

#### Data Source

- `reservationController.getReservationsByGuestId(guestId)`
- Calculate aggregates from the returned list

---

## Phase 9 — Room Management

### 9.1 RoomPanel

**File:** `hms.view.panels.RoomPanel.java` + `.form`

**Extends:** `JPanel`

**Parent layout:** `BorderLayout`

#### Layout

```
┌───────────────────────────────────────────────────────────────┐
│  ROOM MANAGEMENT                                    [Add]     │
├───────────────────────────────────────────────────────────────┤
│ Filter by:  Status: [All ▼]  Type: [All ▼]                   │
│             Price: [$0  ──────── $500]  Capacity: [Any ▼]     │
│             [Apply Filters]  [Clear Filters]                  │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────┐  │
│ │ Room View: [Table ▼]                                     │  │
│ │                                                          │  │
│ │  # │ Room No│ Type    │Floor│Price  │Cap│Status          │  │
│ │ ───┼────────┼─────────┼─────┼───────┼───┼─────────────── │  │
│ │  1 │ 101    │ Single  │ 1   │$100   │ 1 │ [● Available]  │  │
│ │  2 │ 102    │ Double  │ 1   │$150   │ 2 │ [● Occupied]   │  │
│ │  3 │ 103    │ Suite   │ 1   │$300   │ 4 │ [● Maintenance]│  │
│ └──────────────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ [Edit] [Delete] [Mark Maintenance] [Mark Available]           │
└───────────────────────────────────────────────────────────────┘
```

#### Components

**Header:**
- `JLabel` "Room Management" (bold, large)
- `JButton` "Add Room" — primary action

**Filter bar:**
- `JComboBox` status filter: ["All", "Available", "Occupied", "Maintenance", "Reserved"]
- `JComboBox` type filter: ["All", "Single", "Double", "Suite", "Deluxe"]
- `JSlider` or two `JSpinner`s for price range: min price, max price
- `JComboBox` capacity filter: ["Any", "1", "2", "3", "4", "5", "6"]
- `JButton` "Apply Filters"
- `JButton` "Clear Filters"

**View toggle:**
- `JComboBox` or toggle buttons: ["Table View", "Grid View" (optional)]

**Table area:**
- `JTable` inside `JScrollPane`
- Columns: `#`, `Room Number`, `Room Type`, `Floor`, `Base Price`, `Capacity`, `Status`
- Status column rendered with colored badges:
  - Available: green background, white text
  - Occupied: red background, white text
  - Maintenance: orange background, white text
  - Reserved: blue background, white text
- Use custom `TableCellRenderer` for the status column

**Bottom action bar:**
- `JButton` "Edit" — enabled when one row selected
- `JButton` "Delete" — enabled when one row selected (only if room has no active reservations)
- `JButton` "Mark Maintenance" — change status of selected room(s) to "maintenance"
- `JButton` "Mark Available" — change status of selected room(s) to "available"

#### Methods

| Method | Description |
|--------|-------------|
| `loadRooms()` | Load all rooms from `roomController.getAllRooms()`, display in table |
| `applyFilters()` | Call `roomController.filterByStatus/Type/PriceRange(...)` based on filter selections |
| `clearFilters()` | Reset all filters to default, reload all rooms |
| `addRoom()` | Open `RoomDialog` in CREATE mode |
| `editRoom()` | Open `RoomDialog` in EDIT mode |
| `deleteRoom()` | Confirm, then call `roomController.deleteRoom(id)` if no active reservations |
| `updateStatus(roomId, newStatus)` | Update single room's status via `roomController.updateStatus()` |
| `refreshTable()` | Repopulate table |

---

### 9.2 RoomDialog

**File:** `hms.view.dialogs.RoomDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  {Add / Edit} Room                                    │
├───────────────────────────────────────────────────────┤
│  Room Number:*  [________]                            │
│  Room Type:*    [Single       ▼]                      │
│  Capacity:*     [   1   ▲  ▼  ]                       │
│  Base Price:*   [________]   .00                      │
│  Floor:         [________]                            │
│  Description:   [________________________]            │
│                 [________________________]             │
│                                                       │
│  * Required fields                                    │
├───────────────────────────────────────────────────────┤
│           [Save]         [Cancel]                     │
└───────────────────────────────────────────────────────┘
```

#### Components

- `JTextField` roomNumber (max 10 chars) — unique validation
- `JComboBox` roomType: ["Single", "Double", "Suite", "Deluxe", "Penthouse"]
- `JSpinner` capacity: min=1, max=6, step=1
- `JFormattedTextField` basePrice: currency format, positive
- `JSpinner` floor: min=0 (ground), max=20
- `JTextField` or `JTextArea` description (optional, max 200 chars)

#### Mode

Same as GuestDialog: CREATE mode has empty fields, EDIT mode pre-fills from selected Room.

#### Validation

- roomNumber: required, alphanumeric, unique (check via controller before save)
- roomType: required, must select one
- capacity: required, 1-6
- basePrice: required, positive number
- floor: required, 0-20

---

## Phase 10 — Reservation Management

### 10.1 ReservationPanel

**File:** `hms.view.panels.ReservationPanel.java` + `.form`

**Extends:** `JPanel`

**Parent layout:** `BorderLayout`

#### Layout

```
┌───────────────────────────────────────────────────────────────┐
│  RESERVATIONS                                       [New]     │
├───────────────────────────────────────────────────────────────┤
│ Search: [________________________] [Search]                   │
│ Filter: Status: [All ▼]  Date: [This Month ▼]                │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────┐  │
│ │ # │Res ID     │Guest     │Room   │Chkin│Chkout│Amt│Status │  │
│ │ ──┼───────────┼──────────┼───────┼─────┼──────┼───┼────── │  │
│ │ 1 │RES-20260. │John Doe  │101    │06/25│06/27 │$300│● Conf │  │
│ │ 2 │RES-20260. │Jane Smith│205    │06/25│06/28 │$450│● C-In │  │
│ │   │           │          │       │     │      │   │       │  │
│ └──────────────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ [Check-in] [Check-out] [Modify] [Cancel]  ──   Page 1 of 3   │
└───────────────────────────────────────────────────────────────┘
```

#### Components

**Header:**
- `JLabel` "Reservations" (bold, large)
- `JButton` "New Reservation" — primary

**Search/Filter:**
- `JTextField` search (placeholder: "Search by ID, guest name, or room...")
- `JButton` "Search"
- `JComboBox` status: ["All", "Pending", "Confirmed", "Checked-in", "Checked-out", "Cancelled"]
- `JComboBox` date range: ["All", "Today", "This Week", "This Month", "Custom Range..."]
- (When "Custom Range" selected, show two date pickers)

**Table area:**
- `JTable` inside `JScrollPane`
- Columns: `#`, `Reservation ID`, `Guest Name`, `Room`, `Check-in`, `Check-out`, `Amount`, `Status`, `Created`
- Status column with colored badges (custom `TableCellRenderer`):
  - Pending: gray
  - Confirmed: blue
  - Checked-in: green
  - Checked-out: dark green/teal
  - Cancelled: red (strikethrough text optional)
- Row sorting enabled

**Bottom action bar:**
- `JButton` "Check-in" — enabled only for Confirmed reservations
- `JButton` "Check-out" — enabled only for Checked-in reservations
- `JButton` "Modify" — enabled for Confirmed (not checked-in/out)
- `JButton` "Cancel" — enabled for non-cancelled/non-checked-out
- Pagination: same pattern as GuestPanel

#### Methods

| Method | Description |
|--------|-------------|
| `loadReservations()` | Load all from `reservationController.getAllReservations()` |
| `searchReservations()` | Search by ID/guest/room |
| `applyFilters()` | Filter by status, date range |
| `newReservation()` | Open `ReservationDialog` in CREATE mode |
| `checkIn()` | Open `CheckInDialog` for selected reservation |
| `checkOut()` | Open `CheckOutDialog` for selected reservation |
| `modifyReservation()` | Open `ReservationDialog` in EDIT mode |
| `cancelReservation()` | Open `CancellationDialog` |

---

### 10.2 ReservationDialog

**File:** `hms.view.dialogs.ReservationDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌──────────────────────────────────────────────────────────────────┐
│  {New / Modify} Reservation                                      │
├──────────────────────────────────────────────────────────────────┤
│  Guest:*   [Search guest...______________] [Find]                │
│            Selected: John Doe (ID: 5)                            │
│                                                                    │
│  Room:*    [Select room by availability...]  Available: 5        │
│  Check-in: [        ] [Choose]    Check-out: [        ] [Choose] │
│  Guests:   [   2   ▲▼]                                           │
│                                                                    │
│  ┌─────────────────────────────────────────────────────────────┐ │
│  │  AVAILABILITY (for selected dates)                          │ │
│  │  Room  │ Type     │ Price/Night │ Status                     │ │
│  │  101   │ Single   │ $100        │ [Available]                │ │
│  │  102   │ Double   │ $150        │ [Occupied]                 │ │
│  │  205   │ Suite    │ $300        │ [Available]                │ │
│  └─────────────────────────────────────────────────────────────┘ │
│                                                                    │
│  ┌──────────────────────────────────────────────────────────────┐ │
│  │  PRICE BREAKDOWN                                              │ │
│  │  2 nights × $150  =  $300                                     │ │
│  │  Service est.      =   $0                                     │ │
│  │  Tax (10%)         =  $30                                     │ │
│  │  ─────────────────────────────────                             │ │
│  │  Total Estimate    =  $330                                    │ │
│  └──────────────────────────────────────────────────────────────┘ │
│                                                                    │
│  Notes: [________________________________]                        │
├──────────────────────────────────────────────────────────────────┤
│                    [Save Reservation]      [Cancel]               │
└──────────────────────────────────────────────────────────────────┘
```

#### Components

**Guest selection:**
- `JTextField` guest search + `JButton` "Find" → opens a small guest lookup dialog or dropdown
- `JLabel` showing selected guest name and ID

**Room/Date selection:**
- `JComboBox` or `JTable` for room selection (filtered by availability for selected dates)
- `JLabel` showing count of available rooms
- Two date pickers (or `JFormattedTextField`s with date validation)

**Availability table:**
- Read-only `JTable` showing rooms that are available for the selected date range
- Columns: Room #, Type, Price/Night, Status indicator
- Highlights when a room is selected

**Price breakdown panel:**
- Read-only display showing:
  - Room charge (nights × price)
  - Estimated service charges (can be 0 at booking time)
  - Tax (10% of subtotal)
  - Total estimated amount

**Notes:**
- `JTextField` or `JTextArea` for reservation notes

#### Behavior

- When dates change → re-check availability, update price breakdown
- When room changes → update price breakdown
- When guest count changes → validate against room capacity (show warning if exceeded)

#### Real-time Price Calculation

Use the `PricingStrategy` from Phase 4:
- Default: `NormalPricingStrategy`
- If dates fall in a season: `SeasonalPricingStrategy`
- If guest is corporate: `CorporatePricingStrategy`

Call `reservationController.calculatePrice(roomId, checkIn, checkOut, strategy)` and display in the breakdown panel.

---

### 10.3 CheckInDialog

**File:** `hms.view.dialogs.CheckInDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  CHECK-IN — RES-20260625-001                          │
├───────────────────────────────────────────────────────┤
│  Guest:    John Doe                                   │
│  Room:     101 — Deluxe Suite                         │
│  Check-in: Jun 25, 2026                               │
│  Check-out: Jun 27, 2026                              │
│  Nights:   2                                          │
│  Status:   Confirmed                                  │
│                                                       │
│  [✔] Reservation details verified                     │
│  [✔] Room is ready and clean                          │
│                                                       │
│  Notes: [________________________]                    │
│                                                       │
│  This will update reservation to "Checked-in"         │
│  and room status to "Occupied".                       │
├───────────────────────────────────────────────────────┤
│     [Confirm Check-in]          [Cancel]              │
└───────────────────────────────────────────────────────┘
```

#### Components

- Display labels (read-only):
  - Guest name
  - Room number and type
  - Check-in/out dates
  - Number of nights
  - Current status
- Two `JCheckBox` items for verification steps (optional but good UX):
  - "Reservation details verified"
  - "Room is ready and clean"
- `JTextField` for notes (optional)
- `JButton` "Confirm Check-in"
- `JButton` "Cancel"

#### Action on Confirm

1. Call `reservationController.checkIn(reservationId)`
2. This updates reservation status → "checked_in"
3. This updates room status → "occupied"
4. Dashboard observers are notified
5. Show success message
6. Close dialog

---

### 10.4 CheckOutDialog

**File:** `hms.view.dialogs.CheckOutDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌──────────────────────────────────────────────────────────────────┐
│  CHECK-OUT — RES-20260625-001                                    │
├──────────────────────────────────────────────────────────────────┤
│  Guest:    John Doe                                               │
│  Room:     101 — Deluxe Suite                                     │
│  Check-in: Jun 25,  Check-out: Jun 27 (2 nights)                 │
│                                                                    │
│  ┌──────────────────────────────────────────────────────────────┐ │
│  │  ITEMIZED BILL                                                │ │
│  │                                                               │ │
│  │  Room Charges (2 × $150)                $300.00               │ │
│  │  Service: Breakfast (2 × $15)            $30.00               │ │
│  │  Service: Laundry                         $20.00               │ │
│  │  Other: Late checkout                     $25.00               │ │
│  │  ─────────────────────────────────────                         │ │
│  │  Subtotal                                $375.00               │ │
│  │  Tax (10%)                                $37.50               │ │
│  │  ═══════════════════════════════════════                         │ │
│  │  TOTAL                                   $412.50               │ │
│  └──────────────────────────────────────────────────────────────┘ │
│                                                                    │
│  Payment Method: [Cash     ▼]                                      │
│  Amount Received: [$412.50      ]                                  │
│  Change Due: $0.00                                                 │
│                                                                    │
│  Notes: [________________________]                                 │
├──────────────────────────────────────────────────────────────────┤
│     [Complete Check-out]          [Cancel]                        │
└──────────────────────────────────────────────────────────────────┘
```

#### Components

- Read-only labels showing guest, room, dates
- **Itemized bill section** — either a `JTable` (read-only) or a `JTextPane` with HTML-rendered bill
  - Line items: room charge, each service booking, other charges
  - Subtotal, tax (10%), total
  - Currency formatted
- **Payment section:**
  - `JComboBox` payment method: ["Cash", "Card", "Bank Transfer"]
  - `JFormattedTextField` amount received
  - `JLabel` change due (auto-calculated as amount received - total)
- `JTextField` notes
- `JButton` "Complete Check-out"
- `JButton` "Cancel"

#### Auto-calculations

- Total bill is fetched/calculated by `billingController.generateBill(reservationId)`
- Change due = Amount Received - Total Bill (only shown for Cash)
- When Amount Received changes, recalculate change

#### Action on Confirm

1. Call `reservationController.checkOut(reservationId, paymentMethod, amountReceived, notes)`
2. This generates the final bill in the billing table
3. Updates reservation status → "checked_out"
4. Updates room status → "available" (or "maintenance" if flagged)
5. Dashboard observers notified
6. Show success message with total
7. Close dialog

---

### 10.5 CancellationDialog

**File:** `hms.view.dialogs.CancellationDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  CANCEL RESERVATION — RES-20260625-001                │
├───────────────────────────────────────────────────────┤
│  Guest:    John Doe                                   │
│  Room:     101 — Deluxe Suite                         │
│  Check-in: Jun 25, 2026                               │
│                                                       │
│  Cancellation Reason:                                 │
│  [● Guest request                                     │
│   ○ No-show                                           │
│   ○ Administrative                                    │
│   ○ Other: ______________________]                    │
│                                                       │
│  Additional Notes:                                    │
│  [________________________________]                   │
│                                                       │
│  ⚠ This action cannot be undone.                      │
│    Room will be marked as Available.                  │
├───────────────────────────────────────────────────────┤
│     [Confirm Cancellation]     [Cancel]               │
└───────────────────────────────────────────────────────┘
```

#### Components

- Read-only labels: Guest name, Room, Check-in date
- `JRadioButton` group for cancellation reason:
  - "Guest request"
  - "No-show"
  - "Administrative"
  - "Other:" + `JTextField`
- `JTextArea` for additional notes (optional)
- Warning label (red/orange text)
- `JButton` "Confirm Cancellation"
- `JButton` "Cancel"

#### Action on Confirm

1. Call `reservationController.cancelReservation(reservationId, reason, notes)`
2. Reservation status → "cancelled"
3. Room status → "available"
4. Dashboard observers notified
5. Show confirmation message
6. Close dialog

---

## Phase 11 — Billing

### 11.1 BillingPanel

**File:** `hms.view.panels.BillingPanel.java` + `.form`

**Extends:** `JPanel`

**Parent layout:** `BorderLayout`

#### Layout

```
┌───────────────────────────────────────────────────────────────┐
│  BILLING MANAGEMENT                                           │
├───────────────────────────────────────────────────────────────┤
│ Filter:  Status: [All ▼]  Date From: [        ] To: [      ] │
│         [Apply Filters]  [Clear]   [Export... ▼]             │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────┐  │
│ │ # │Bill#│Res ID    │Guest     │Total     │Paid   │Status │  │
│ │ ──┼─────┼──────────┼──────────┼──────────┼───────┼───────│  │
│ │ 1 │ B1  │RES-20260.│John Doe  │$412.50   │$412.50│● Paid │  │
│ │ 2 │ B2  │RES-20260.│Jane Smith│$350.00   │$0.00  │● Pending│  │
│ │ 3 │ B3  │RES-20260.│Bob Adams │$525.00   │$200.00│● Partial│ │
│ └──────────────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ [View Details] [Record Payment] [Adjust Bill]   Page 1 of 2   │
└───────────────────────────────────────────────────────────────┘
```

#### Components

**Header:**
- `JLabel` "Billing Management" (bold, large)

**Filter bar:**
- `JComboBox` payment status: ["All", "Pending", "Partial", "Paid", "Refunded"]
- Two `JFormattedTextField` or date pickers: From date, To date
- `JButton` "Apply Filters"
- `JButton` "Clear"
- `JButton` or `JComboBox` "Export..." → options: ["Export to PDF", "Export to CSV"]

**Table area:**
- `JTable` inside `JScrollPane`
- Columns: `#`, `Bill ID`, `Reservation ID`, `Guest Name`, `Total Amount`, `Amount Paid`, `Payment Status`, `Payment Date`
- Status column rendered with colored badges:
  - Paid: green
  - Partial: orange
  - Pending: red
  - Refunded: gray/blue

**Bottom bar:**
- `JButton` "View Details" — show itemized bill (expand or dialog)
- `JButton` "Record Payment" — open PaymentDialog
- `JButton` "Adjust Bill" — open AdjustmentDialog (only for non-paid bills)
- Pagination

#### Methods

| Method | Description |
|--------|-------------|
| `loadBills()` | Load all bills from `billingController.getAllBills()` |
| `applyFilters()` | Filter by status and/or date range |
| `viewDetails()` | Show itemized bill in a read-only dialog or expandable section |
| `recordPayment()` | Open `PaymentDialog` |
| `adjustBill()` | Open `AdjustmentDialog` |
| `exportBills(format)` | Generate PDF/CSV of current filtered bills |

---

### 11.2 PaymentDialog

**File:** `hms.view.dialogs.PaymentDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  RECORD PAYMENT — RES-20260625-001                    │
├───────────────────────────────────────────────────────┤
│  Guest:    John Doe                                   │
│  Total Bill: $412.50                                  │
│  Already Paid: $0.00                                  │
│  Balance Due: $412.50                                 │
│                                                       │
│  Payment Method: [Cash      ▼]                        │
│  Amount:        [$412.50      ]                       │
│  Payment Date:  [Jun 27, 2026] [Choose]              │
│  Reference #:   [________________] (optional)         │
│  Notes:         [________________]                    │
│                                                       │
│  * Amount cannot exceed balance due.                  │
├───────────────────────────────────────────────────────┤
│     [Record Payment]          [Cancel]                │
└───────────────────────────────────────────────────────┘
```

#### Components

- Read-only labels: Guest, Total Bill, Already Paid, Balance Due (auto-calculated)
- `JComboBox` payment method: ["Cash", "Card", "Bank Transfer"]
- `JFormattedTextField` amount (currency format)
- Date picker for payment date (defaults to today)
- `JTextField` reference number (optional — e.g., card transaction ID, cheque number)
- `JTextField` or `JTextArea` notes

#### Validation

- Amount must be > 0
- Amount cannot exceed balance due (total - already paid)
- If amount < balance due, payment status becomes "partial"
- If amount >= balance due, payment status becomes "paid"

#### Action on Confirm

1. Call `billingController.recordPayment(billingId, amount, method, reference, date, notes)`
2. Update billing record
3. Dashboard observer notified via `onPaymentRecorded()`
4. Show success message
5. Close dialog

---

### 11.3 AdjustmentDialog

**File:** `hms.view.dialogs.AdjustmentDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  BILL ADJUSTMENT — Bill #B1                           │
├───────────────────────────────────────────────────────┤
│  Current Total: $412.50                               │
│                                                       │
│  Discount:     [$0.00   ] (positive number)           │
│  Late Charge:  [$0.00   ] (positive number)           │
│  Other Charge: [$0.00   ] (positive number)           │
│  Other Description: [________________]                │
│                                                       │
│  ─────────────────────────────────────                 │
│  New Total:    $412.50                                │
│                                                       │
│  Notes: [________________________]                    │
│                                                       │
│  Adjustments are reflected in the final bill.         │
├───────────────────────────────────────────────────────┤
│     [Apply Adjustments]      [Cancel]                 │
└───────────────────────────────────────────────────────┘
```

#### Components

- Read-only label showing current total
- `JFormattedTextField` discount amount
- `JFormattedTextField` late charge
- `JFormattedTextField` other charge
- `JTextField` other charge description
- `JLabel` showing recalculated new total (updates as fields change)
- `JTextField` or `JTextArea` notes

#### Auto-calculation

New Total = Current Total - Discount + Late Charge + Other Charge

Update the "New Total" label whenever any adjustment field changes using a `DocumentListener`.

#### Action on Confirm

1. Call `billingController.adjustBill(billingId, discount, lateCharge, otherCharge, otherDesc, notes)`
2. Update billing record with adjusted values
3. Close dialog

---

## Phase 12 — Services

### 12.1 ServicePanel

**File:** `hms.view.panels.ServicePanel.java` + `.form`

**Extends:** `JPanel`

**Parent layout:** `BorderLayout`

#### Layout

```
┌───────────────────────────────────────────────────────────────┐
│  SERVICE CATALOG                                    [Add]     │
├───────────────────────────────────────────────────────────────┤
│ Filter: Type: [All ▼]  Status: [All ▼]  [Apply]             │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────┐  │
│ │ # │Service Name  │Type     │Price   │Available │Bookings │  │
│ │ ──┼──────────────┼─────────┼────────┼──────────┼─────────│  │
│ │ 1 │Breakfast     │Food     │$15.00  │[✓] Yes   │  45     │  │
│ │ 2 │Laundry       │Laundry  │$20.00  │[✓] Yes   │  12     │  │
│ │ 3 │Spa Massage   │Spa      │$50.00  │[✗] No    │   0     │  │
│ └──────────────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ [Edit] [Delete] [Toggle Availability]                         │
│ [Book Service for Reservation...]                             │
└───────────────────────────────────────────────────────────────┘
```

#### Components

**Header:**
- `JLabel` "Service Catalog" (bold, large)
- `JButton` "Add Service"

**Filter bar:**
- `JComboBox` service type: ["All", "Food", "Laundry", "Spa", "Conference", "Transport", "Other"]
- `JComboBox` availability: ["All", "Available", "Unavailable"]

**Table area:**
- `JTable` inside `JScrollPane`
- Columns: `#`, `Service Name`, `Service Type`, `Price`, `Available`, `Total Bookings`
- "Available" column: checkbox-style rendering (✓ or ✗)

**Bottom bar:**
- `JButton` "Edit"
- `JButton` "Delete" — only if service has no active bookings
- `JButton` "Toggle Availability" — flips the is_available flag
- `JButton` "Book for Reservation..." — opens ServiceBookingDialog

#### Methods

| Method | Description |
|--------|-------------|
| `loadServices()` | Load all from `serviceController.getAllServices()` |
| `applyFilters()` | Filter by type and/or availability |
| `addService()` | Open `ServiceDialog` in CREATE mode |
| `editService()` | Open `ServiceDialog` in EDIT mode |
| `deleteService()` | Confirm, delete if no active bookings |
| `toggleAvailability()` | Flip `isAvailable` flag via `serviceController.toggleAvailability(id)` |
| `openBookingDialog()` | Open `ServiceBookingDialog` |

---

### 12.2 ServiceDialog

**File:** `hms.view.dialogs.ServiceDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  {Add / Edit} Service                                 │
├───────────────────────────────────────────────────────┤
│  Service Name:* [________________________]            │
│  Service Type:* [Food            ▼]                   │
│  Price:*        [$________]                           │
│  Description:   [________________________]            │
│                 [________________________]             │
│  Available:     [✓] Yes, this service is available    │
│                                                       │
│  * Required fields                                    │
├───────────────────────────────────────────────────────┤
│           [Save]         [Cancel]                     │
└───────────────────────────────────────────────────────┘
```

#### Components

- `JTextField` serviceName (max 150 chars)
- `JComboBox` serviceType: ["Food", "Laundry", "Spa", "Conference", "Transport", "Other"]
- `JFormattedTextField` price (currency, positive)
- `JTextArea` description (3-4 rows, optional, max 500 chars)
- `JCheckBox` isAvailable — default checked

---

### 12.3 ServiceBookingDialog

**File:** `hms.view.dialogs.ServiceBookingDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  BOOK SERVICE FOR RESERVATION                         │
├───────────────────────────────────────────────────────┤
│  Reservation:* [Search..._________] [Find]            │
│  Selected: RES-20260625-001 — John Doe (Checked-in)   │
│                                                       │
│  Service:*   [Breakfast — $15.00    ▼]               │
│  Quantity:   [   2   ▲▼]                              │
│                                                       │
│  ─────────────────────────────────────                 │
│  Total Price: $30.00                                  │
│                                                       │
│  Notes: [________________________]                    │
│                                                       │
│  * Required fields                                    │
├───────────────────────────────────────────────────────┤
│           [Book Service]      [Cancel]                │
└───────────────────────────────────────────────────────┘
```

#### Components

- Reservation lookup:
  - `JTextField` search (by reservation ID or guest name)
  - `JButton` "Find"
  - `JLabel` showing selected reservation info
- `JComboBox` service selection — shows available services only, format: "Service Name — $Price"
- `JSpinner` quantity: min=1, max=100
- `JLabel` total price (auto-calculated: quantity × service price)

#### Auto-calculation

When service or quantity changes → update "Total Price" label: `quantity * service.getPrice()`

#### Action on Confirm

1. Call `serviceController.bookService(reservationId, serviceId, quantity, notes)`
2. Insert record into `service_bookings` table
3. Show success message
4. Close dialog

---

## Phase 13 — Staff

### 13.1 StaffPanel

**File:** `hms.view.panels.StaffPanel.java` + `.form`

**Extends:** `JPanel`

**Parent layout:** `BorderLayout`

#### Layout

```
┌───────────────────────────────────────────────────────────────┐
│  STAFF DIRECTORY                                    [Add]     │
├───────────────────────────────────────────────────────────────┤
│ Filter: Dept: [All ▼]  Position: [All ▼]  Status: [All ▼]   │
│         [Apply Filters]  [Clear]                              │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────┐  │
│ │ # │Name       │Email          │Position │Dept │Status    │  │
│ │ ──┼───────────┼───────────────┼─────────┼─────┼───────── │  │
│ │ 1 │Alice John │alice@hotel.com│Manager  │Ops  │● Active  │  │
│ │ 2 │Bob Smith  │bob@hotel.com  │Recept.  │Front│● Active  │  │
│ │ 3 │Carol Lee  │carol@hotel.com│Housekeep│HSKP │● On Leave│ │
│ └──────────────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ [Edit] [Delete] [View Assignments] [Manage Room Assignments]  │
└───────────────────────────────────────────────────────────────┘
```

#### Components

**Header:**
- `JLabel` "Staff Directory" (bold, large)
- `JButton` "Add Staff"

**Filter bar:**
- `JComboBox` department: ["All", "Front Desk", "Housekeeping", "Kitchen", "Management", "Maintenance"]
- `JComboBox` position: ["All", "Manager", "Receptionist", "Housekeeper", "Chef", "Maintenance Tech", "Security"]
- `JComboBox` status: ["All", "Active", "Inactive", "On Leave"]

**Table area:**
- `JTable` inside `JScrollPane`
- Columns: `#`, `First Name`, `Last Name`, `Email`, `Phone`, `Position`, `Department`, `Salary`, `Status`, `Joining Date`
- Status colors: green for Active, gray for Inactive, orange for On Leave

**Bottom bar:**
- `JButton` "Edit"
- `JButton` "Delete" — confirm, soft delete if staff has assignments
- `JButton` "View Assignments" — show room assignments for selected staff
- `JButton` "Manage Room Assignments" — open RoomAssignmentPanel functionality

---

### 13.2 StaffDialog

**File:** `hms.view.dialogs.StaffDialog.java` + `.form`

**Extends:** `JDialog`

**Modal:** `true`

#### Layout

```
┌───────────────────────────────────────────────────────┐
│  {Add / Edit} Staff                                   │
├───────────────────────────────────────────────────────┤
│  First Name:*  [________________________]             │
│  Last Name:*   [________________________]             │
│  Email:*       [________________________]             │
│  Phone:*       [________________________]             │
│  Position:*    [Receptionist         ▼]               │
│  Department:*  [Front Desk           ▼]               │
│  Salary:       [$________]                            │
│  Joining Date:*[        ] [Choose]                    │
│  Status:       [Active              ▼]                │
│                                                       │
│  * Required fields                                    │
├───────────────────────────────────────────────────────┤
│           [Save]         [Cancel]                     │
└───────────────────────────────────────────────────────┘
```

#### Components

- `JTextField` firstName (max 100 chars)
- `JTextField` lastName (max 100 chars)
- `JTextField` email (max 150 chars, unique validation)
- `JTextField` phone (max 15 chars)
- `JComboBox` position: ["Manager", "Receptionist", "Housekeeper", "Chef", "Maintenance Technician", "Security", "Accountant", "Other"]
- `JComboBox` department: ["Front Desk", "Housekeeping", "Kitchen", "Management", "Maintenance", "Security", "Finance"]
- `JFormattedTextField` salary (currency, positive, optional)
- Date picker joiningDate
- `JComboBox` status: ["Active", "Inactive", "On Leave"]

#### Mode

Same CREATE/EDIT pattern as other dialogs.

---

### 13.3 RoomAssignmentPanel

**File:** `hms.view.panels.RoomAssignmentPanel.java` + `.form`

**Note:** This can be either a separate panel displayed in the tabbed content area, OR a dialog opened from StaffPanel. The plan lists it as a panel.

**Extends:** `JPanel` or `JDialog`

#### Layout (as panel)

```
┌───────────────────────────────────────────────────────────────┐
│  ROOM ASSIGNMENTS                                   [New]     │
├───────────────────────────────────────────────────────────────┤
│ Filter: Status: [All ▼]  Date: [Today ▼]  Staff: [All ▼]    │
├───────────────────────────────────────────────────────────────┤
│ ┌──────────────────────────────────────────────────────────┐  │
│ │ # │Room     │Staff       │Type       │Date      │Status  │  │
│ │ ──┼─────────┼────────────┼───────────┼──────────┼────────│  │
│ │ 1 │101      │Carol Lee   │Cleaning   │Jun 25    │● Pending│ │
│ │ 2 │205      │Bob Smith   │Inspection │Jun 25    │● In Prog│ │
│ │ 3 │302      │Carol Lee   │Maintenance│Jun 24    │● Done  │  │
│ └──────────────────────────────────────────────────────────┘  │
├───────────────────────────────────────────────────────────────┤
│ [Edit] [Mark In Progress] [Mark Completed] [Delete]           │
└───────────────────────────────────────────────────────────────┘
```

#### Components

**Header:**
- `JLabel` "Room Assignments" (bold, large)
- `JButton` "New Assignment"

**Filter bar:**
- `JComboBox` status: ["All", "Pending", "In Progress", "Completed"]
- `JComboBox` date: ["All", "Today", "This Week", "This Month"]
- `JComboBox` staff: ["All"] + list of staff names

**Table area:**
- `JTable` inside `JScrollPane`
- Columns: `#`, `Room #`, `Staff Name`, `Assignment Type`, `Assignment Date`, `Status`, `Notes`
- Status colors: Pending=gray/orange, In Progress=blue, Completed=green

**Bottom bar:**
- `JButton` "Edit"
- `JButton` "Mark In Progress"
- `JButton` "Mark Completed"
- `JButton` "Delete"

#### New Assignment Dialog (embedded or separate)

When "New Assignment" is clicked, show a small dialog or inline form:

```
  Room:       [101 — Deluxe Suite    ▼]
  Staff:      [Carol Lee — Housekeeping ▼]
  Type:       [Cleaning              ▼]
  Date:       [Jun 25, 2026] [Choose]
  Notes:      [________________________]

      [Create Assignment]  [Cancel]
```

#### Assignment Types (JComboBox)

["Cleaning", "Maintenance", "Inspection", "Repair", "Turndown Service", "Other"]

---

## Phase 14 — ReportsPanel

**File:** `hms.view.panels.ReportsPanel.java` + `.form`

**Extends:** `JPanel`

**Parent layout:** `BorderLayout`

#### Layout

```
┌───────────────────────────────────────────────────────────────┐
│  REPORTS                                                      │
├───────────────────────────────────────────────────────────────┤
│  Report Type: [Guest Invoice ▼]                               │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │  FILTERS                                                 │  │
│  │                                                          │  │
│  │  Date Range: From: [        ] To: [        ]             │  │
│  │  Guest:      [Search guest..._______] [Browse]           │  │
│  │  Room Type:  [All ▼]                                     │  │
│  │  Status:     [All ▼]                                     │  │
│  │                                                          │  │
│  │                     [Generate Report]                    │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐  │
│  │                                                          │  │
│  │  PREVIEW AREA                                            │  │
│  │  (Embedded JasperViewer or scrollable preview)           │  │
│  │                                                          │  │
│  └─────────────────────────────────────────────────────────┘  │
│                                                               │
│                          [Export to PDF]                       │
└───────────────────────────────────────────────────────────────┘
```

#### Components

**Header:**
- `JLabel` "Reports" (bold, large)

**Report type selector:**
- `JComboBox` reportType: ["Guest Invoice", "Occupancy & Revenue Analysis"]

**Filters panel:**
- **Date range:** Two date pickers (From / To) — required for all reports
- **Guest selector** (for Guest Invoice report):
  - `JTextField` search + `JButton` "Browse" → guest lookup dialog
  - Enabled only when "Guest Invoice" is selected
- **Room type filter** (for Occupancy report):
  - `JComboBox` room type: ["All", "Single", "Double", "Suite", "Deluxe", "Penthouse"]
- **Status filter** (for Occupancy report):
  - `JComboBox` status: ["All", "Confirmed", "Checked-in", "Checked-out", "Cancelled"]
- `JButton` "Generate Report" — primary action, large button

**Preview area:**
- Either an embedded `JRViewer` (JasperReports viewer component) inside a `JScrollPane`
- Or a placeholder `JPanel` where the report is displayed
- If JasperReports viewer is used, it provides built-in zoom, navigation, and print

**Export button:**
- `JButton` "Export to PDF" — enabled only after a report has been generated
- Calls `reportUtil.exportToPDF(reportPath, params, outputPath)`

#### Behavior

1. User selects report type → relevant filters are shown/hidden
2. User sets filters and clicks "Generate Report"
3. Call `reportUtil.generateReport(reportType, parameters)` which:
   - Loads the `.jasper` compiled report
   - Sets parameters (date range, guest ID, etc.)
   - Fills the report with data from the database
   - Displays in the preview area
4. User clicks "Export to PDF" → saves to user-chosen location

#### Report Parameters

**Guest Invoice:**
- `reportType` = "guest_invoice"
- Parameters: `startDate`, `endDate`, `guestId` (optional — if empty, include all guests)

**Occupancy & Revenue:**
- `reportType` = "occupancy_revenue"
- Parameters: `startDate`, `endDate`, `roomType` (optional), `status` (optional)

---

## Appendix: Reusable UI Patterns

### Common Dialog Pattern

All dialogs (Guest, Room, Service, Staff, etc.) follow the same pattern:

```java
public class XxxDialog extends JDialog {
    
    // Mode enum
    public enum Mode { CREATE, EDIT }
    
    private Mode mode;
    private XxxController controller;
    private Xxx result; // the created/edited object
    private boolean confirmed = false;
    
    // Constructor for CREATE
    public XxxDialog(Window owner, Mode mode, XxxController controller) {
        super(owner, mode == Mode.CREATE ? "Add ..." : "Edit ...", 
              ModalityType.APPLICATION_MODAL);
        this.mode = mode;
        this.controller = controller;
        initComponents();
    }
    
    // Pre-fill for EDIT
    public void setXxx(Xxx xxx) { /* pre-fill fields */ }
    
    // Show and return result
    public Xxx showDialog() {
        setVisible(true); // blocks until dialog closes
        return confirmed ? result : null;
    }
    
    // Save button action
    private void onSave() {
        if (!validateInput()) return;
        // Build object from fields
        // Call controller.createXxx() or controller.updateXxx()
        // Set result, confirmed = true, dispose()
    }
}
```

### Table with Pagination Pattern

For all list panels (Guest, Room, Reservation, Billing, Service, Staff):

```java
public class XxxPanel extends JPanel {
    
    private int currentPage = 1;
    private int pageSize = 25;
    private int totalRecords = 0;
    private List<Xxx> allData; // full dataset from controller
    private List<Xxx> filteredData; // after search/filter
    
    private void loadPage() {
        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredData.size());
        List<Xxx> pageData = filteredData.subList(fromIndex, toIndex);
        tableModel.setData(pageData);
        updatePaginationControls();
    }
    
    private void updatePaginationControls() {
        int totalPages = (int) Math.ceil((double) filteredData.size() / pageSize);
        pageLabel.setText("Page " + currentPage + " of " + totalPages);
        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage < totalPages);
        recordCountLabel.setText("Records: " + filteredData.size());
    }
}
```

### Search/Filter Debouncing

For search fields that call the controller on each keystroke, use a `javax.swing.Timer` to debounce:

```java
private Timer searchTimer = new Timer(300, e -> performSearch());

// In constructor:
searchTimer.setRepeats(false);
searchField.getDocument().addDocumentListener(new DocumentListener() {
    public void insertUpdate(DocumentEvent e) { searchTimer.restart(); }
    public void removeUpdate(DocumentEvent e) { searchTimer.restart(); }
    public void changedUpdate(DocumentEvent e) { searchTimer.restart(); }
});
```

### Status Badge Renderer

Reusable `TableCellRenderer` for status columns:

```java
public class StatusBadgeRenderer extends DefaultTableCellRenderer {
    
    private static final Map<String, Color> STATUS_COLORS = Map.of(
        "Available", new Color(40, 167, 69),    // green
        "Occupied", new Color(220, 53, 69),      // red
        "Maintenance", new Color(255, 193, 7),   // orange
        "Reserved", new Color(23, 162, 184),     // blue
        "Paid", new Color(40, 167, 69),          // green
        "Pending", new Color(220, 53, 69),       // red
        "Partial", new Color(255, 193, 7),       // orange
        "Confirmed", new Color(23, 162, 184),    // blue
        "Checked-in", new Color(40, 167, 69),    // green
        "Checked-out", new Color(32, 201, 151),  // teal
        "Cancelled", new Color(108, 117, 125),   // gray
        "Active", new Color(40, 167, 69),        // green
        "Inactive", new Color(108, 117, 125),    // gray
        "On Leave", new Color(255, 193, 7)       // orange
    );
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);
        
        String status = value.toString();
        Color bg = STATUS_COLORS.getOrDefault(status, Color.GRAY);
        
        label.setBackground(bg);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(CENTER);
        label.setOpaque(true);
        
        return label;
    }
}
```

### Confirmation Dialogs

For delete operations, use a consistent pattern:

```java
private boolean confirmDelete(String itemType, String itemName) {
    int result = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete " + itemType + " \"" + itemName + "\"?\n"
        + "This action cannot be undone.",
        "Confirm Deletion",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );
    return result == JOptionPane.YES_OPTION;
}
```

---

*End of GUI Building Reference — This document describes all 19 `.form` files (plus MainWindow hand-coded) across Phases 6 through 14.*
