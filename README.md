# Hotel Management System (HMS)

A full-featured desktop application for managing hotel operations built with Java Swing and FlatLaf. Supports dark and light themes.

## Features

- **Dashboard** — Occupancy rate, daily revenue, pending check-ins, housekeeping alerts, and a 7-day revenue bar chart (JFreeChart)
- **Guest Management** — Add, edit, search, and manage guest profiles with ID proof tracking
- **Room Management** — Manage rooms, capacities, pricing, and status (available, occupied, maintenance, reserved)
- **Reservations** — Create, modify, check-in, and check-out reservations with tax-inclusive pricing strategies
- **Billing** — Generate bills, record partial/full payments, apply discounts and late charges, adjustment tracking
- **Services** — Manage service bookings (Food, Laundry, Spa, Transport, etc.) linked to reservations
- **Staff Management** — Add, edit, and manage staff profiles with role-based positions and bcrypt-hashed passwords
- **Room Assignments** — Assign staff (housekeeping, maintenance, inspection) to rooms
- **Reports** — JasperReports-based invoice and occupancy reports with PDF export
- **Authentication** — Secure login with bcrypt password verification
- **Dual Theme** — Dark and Light theme support with custom FlatLaf properties

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java 26 |
| UI Framework | Swing + FlatLaf 3.5 |
| Database | MySQL 8+ (XAMPP) |
| Reporting | JasperReports 7.0.6 |
| Charts | JFreeChart 1.5.4 |
| Password Hashing | jBCrypt 0.4 |
| SVG Icons | FlatSVGIcon / JSVG |
| Build | NetBeans Ant |
| Connector | MySQL Connector/J 9.7 |

## Prerequisites

- **Java 26+** (JDK) — [Download](https://adoptium.net/)
- **XAMPP** (or any MySQL server) — [Download](https://www.apachefriends.org/)
- **NetBeans** (optional, for development) — [Download](https://netbeans.apache.org/)

## Setup

### 1. Database

Start MySQL via XAMPP Control Panel, then run:

```bash
mysql -u root < database/schema.sql
mysql -u root < database/seed.sql
```

This creates the `hotel_management_system` database with 8 tables and populates it with sample data (30 guests, 24 rooms, 8 staff, 10 services, 77 reservations, etc.).

### 2. Run the Application

**Option A — NetBeans:**
1. Open the project in NetBeans
2. Press `F6` (Run Project)

**Option B — Pre-built JAR:**
```bash
java -jar dist/ead_cw.jar
```

**Option C — Build from source:**
```bash
javac -cp "lib/*" -d build/classes src/**/*.java
jar cfm dist/ead_cw.jar META-INF/MANIFEST.MF -C build/classes .
java -jar dist/ead_cw.jar
```

## Default Login

All staff accounts use the password: `staff123`

| Email | Role | Department |
|---|---|---|
| `alice.johnson@hms.com` | Manager | Administration |
| `bob.williams@hms.com` | Receptionist | Front Desk |
| `carol.davis@hms.com` | Receptionist | Front Desk |
| `daniel.brown@hms.com` | Housekeeper | Housekeeping |
| `eva.martinez@hms.com` | Housekeeper | Housekeeping |
| `frank.wilson@hms.com` | Maintenance | Maintenance |
| `gayan.fernando@hms.com` | Concierge | Front Desk |
| `priyanka.j@hms.com` | Shift Supervisor | Administration |

## Theme

Toggle between dark and light mode by changing `Constants.THEME` in `src/hms/config/Constants.java`:

```java
public static final String THEME = "dark";   // "dark" or "light"
```

## Database Schema

8 tables with foreign key relationships:

```
guests ──┐
         │
rooms ───┤
         ├── reservations ──┬── billing
         │                  ├── service_bookings ── services
         │                  └── room_assignments ── staff
         │
staff ───┘
```

## Project Structure

```
src/hms/
├── config/        Constants.java (app config, DB, theme, validation)
├── controller/    Business logic controllers
├── dao/           Data Access Objects (CRUD)
├── database/      DatabaseConnection.java
├── exception/     Custom exceptions (DatabaseException, ValidationException)
├── model/         Domain models (Guest, Room, Staff, Reservation, etc.)
├── reports/       JasperReports (.jrxml) templates
├── resources/
│   ├── icons/     SVG icon files
│   └── theme/     FlatLaf .properties theme files
├── service/       Service layer
├── theme/         RoundedPanel and UI theme utilities
├── util/          Utilities (PasswordUtil, ValidationUtil, IconUtil)
└── view/
    ├── dialogs/   12 dialog windows (add/edit/check-in/check-out/payment/etc.)
    ├── panels/    10 management panels (Dashboard, Guests, Rooms, etc.)
    └── MainWindow.java  Main application frame
```

## Libraries

All JAR dependencies are in `lib/`:
- `flatlaf-3.5.1.jar` / `flatlaf-extras-3.4.1.jar` — Look and feel
- `mysql-connector-j-9.7.0.jar` — MySQL JDBC driver
- `jasperreports-7.0.6.jar` (+ PDF & Fonts) — Reporting
- `jfreechart-1.5.4.jar` — Bar charts
- `jbcrypt-0.4.jar` — Password hashing
- `jsvg-1.4.0.jar` — SVG rendering
- `jackson-2.18.2` (core/databind/annotations/xml) — JSON/XML
- `openpdf-1.3.43.jar` — PDF generation
- `jcalendar-1.4.jar` — Date picker
- `commons-*` (beanutils, collections4, digester, logging) — Apache Commons
- `stax2-api-4.2.2.jar` — XML streaming

## License

This project is developed for academic purposes.
