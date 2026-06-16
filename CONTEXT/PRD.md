# Hotel Management System - Product Requirements Document (PRD)

**Version:** 1.0  
**Date:** June 2026  
**Course:** Enterprise Application Development 1 (DSE 25.2)  
**Institution:** National Institute of Business Management (NIBM), The City University  
**Domain:** Hospitality and Tourism Sector

---

## 1. Executive Summary

The Hotel Management System (HMS) is a comprehensive Java Swing-based enterprise application designed to streamline hotel operations, from guest reservations to billing and staff management. The system targets small to medium-sized hotels with 20-100 rooms and aims to improve operational efficiency, guest experience, and revenue management.

---

## 2. Project Objectives

1. **Automate guest lifecycle management** - From booking to checkout
2. **Enable real-time room availability tracking** - Prevent overbooking
3. **Streamline billing and payment processing** - Accurate charge computation
4. **Generate actionable business intelligence** - Multi-table reports for decision-making
5. **Maintain clean, professional UI** - Using FlatLaf dark theme for optimal UX
6. **Ensure data integrity and security** - Database normalization, input validation, hashed passwords
7. **Facilitate code maintainability** - Design patterns, MVC architecture, clear organization

---

## 3. Scope Definition

### 3.1 In Scope

#### 3.1.1 Guest Management Module
- Guest registration and profile management
- Guest history and previous bookings
- Guest contact details (Email, Phone, Address)
- Guest identification proof storage

#### 3.1.2 Room Management Module
- Room inventory management (CRUD operations)
- Room type categorization (Single, Double, Suite, etc.)
- Room status tracking (Available, Occupied, Maintenance, Reserved)
- Room pricing configuration per type
- Room assignment to reservations

#### 3.1.3 Reservation & Booking Module
- Create, read, update, delete reservations
- Check-in/Check-out functionality
- Real-time room availability verification
- Auto-generated reservation IDs
- Booking confirmation with guest details
- Length of stay calculations

#### 3.1.4 Billing Module
- Automatic bill generation for each reservation
- Room charges (per night, based on room type)
- Service charges (meals, laundry, spa, etc.)
- Additional charges (parking, late checkout, etc.)
- Tax calculations
- Payment status tracking
- Receipt generation

#### 3.1.5 Staff Management Module
- Employee registration and profile
- Role and department assignment
- Room assignment tracking (housekeeping, maintenance)
- Staff performance data

#### 3.1.6 Service Management Module
- Service catalog (meals, laundry, spa, conference rooms, etc.)
- Service booking by guests
- Service pricing
- Service bill integration

#### 3.1.7 Reporting Module
- **Report 1: Guest Invoice Report**
  - Guest details, room information, stay duration
  - Itemized charges (room, services, taxes)
  - Payment details and total amount
  - Multi-table data (reservations, guests, billing, rooms, services)

- **Report 2: Occupancy & Revenue Analysis**
  - Room-wise occupancy percentage
  - Revenue per room type
  - Peak booking periods analysis
  - Staff performance metrics
  - Monthly/quarterly/yearly revenue trends
  - Multi-table data (reservations, rooms, billing, staff, service_bookings)

#### 3.1.8 Dashboard
- Real-time occupancy rate gauge
- Revenue overview (current month/year-to-date)
- Today's check-ins and check-outs count
- Room status summary
- Pending reservations alerts
- Quick statistics cards

#### 3.1.9 User Interfaces
- **Transaction UI (Reservation):** Booking form, check-in/check-out
- **Input UIs:** Guest registration, room management, staff management, billing
- **Dashboard:** Executive overview with KPIs
- **Search & Filter:** Advanced search across modules
- **Settings & Configuration:** System settings

### 3.2 Out of Scope

- Staff authentication/login system (not required for coursework)
- Email/SMS API integration (optional enhancement only)
- Multi-property management (single hotel only)
- Advanced accounting (GL integration, tax calculations are simplified)
- Mobile application
- API-based architecture (Desktop application only)
- Inventory management (beyond rooms and services)
- Marketing automation
- Guest feedback/review system

---

## 4. Technical Architecture

### 4.1 Architecture Pattern
**MVC (Model-View-Controller) with Data Access Objects (DAO)**

```
┌─────────────────────────────────────────┐
│           Presentation Layer            │
│  (Views - Swing Components, FlatLaf)    │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│          Controller Layer                │
│  (Business Logic Orchestration)         │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│            Service Layer (if needed)     │
│  (Business Logic Implementation)         │
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│       Data Access Layer (DAO)           │
│  (Database Operations, PreparedStatements)│
└────────────────┬────────────────────────┘
                 │
┌────────────────▼────────────────────────┐
│            MySQL Database               │
│  (XAMPP - Direct Connection)            │
└─────────────────────────────────────────┘
```

### 4.2 Design Patterns Used

| Pattern | Usage | Rationale |
|---------|-------|-----------|
| **Singleton** | Database connection manager, Utilities | Ensures single instance, resource efficiency |
| **DAO (Data Access Object)** | All database operations | Separates data logic from business logic |
| **Factory Pattern** | Room type creation | Encapsulates object creation |
| **Observer Pattern** | Dashboard updates | Reactive updates when data changes |
| **Strategy Pattern** | Pricing strategies (seasonal, corporate) | Flexible pricing without modification |

### 4.3 Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Language** | Java | 21 (Latest) |
| **Build Tool** | Apache Ant | Default |
| **UI Framework** | Java Swing + FlatLaf | FlatLaf 3.5.1 |
| **Database** | MySQL | XAMPP default |
| **JDBC Driver** | MySQL Connector/J | 9.7.0 |
| **Reporting** | Jasper Reports | 7.0.6 |
| **IDE** | NetBeans | Latest |
| **Icons** | FontAwesome/Material Design/Lucide | As available in NetBeans |
| **Theme** | macOS Dark Theme (FlatLaf) | Built-in |

---

## 5. Database Design

### 5.1 Schema Overview

#### Core Tables

**guests**
```sql
guest_id (INT, PRIMARY KEY, AUTO_INCREMENT)
first_name (VARCHAR 100, NOT NULL)
last_name (VARCHAR 100, NOT NULL)
email (VARCHAR 150, UNIQUE, NOT NULL)
phone (VARCHAR 15, NOT NULL)
address (TEXT)
id_proof_type (VARCHAR 50) -- Passport, Driving License, etc.
id_proof_number (VARCHAR 50)
date_of_birth (DATE)
created_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
```

**rooms**
```sql
room_id (INT, PRIMARY KEY, AUTO_INCREMENT)
room_number (VARCHAR 10, UNIQUE, NOT NULL)
room_type (VARCHAR 50, NOT NULL) -- Single, Double, Suite, etc.
capacity (INT, NOT NULL)
base_price (DECIMAL 10,2, NOT NULL)
status (ENUM: 'available', 'occupied', 'maintenance', 'reserved')
floor (INT)
created_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)

INDEXES:
- room_number (UNIQUE)
- status
- room_type
```

**reservations**
```sql
reservation_id (INT, PRIMARY KEY, AUTO_INCREMENT)
guest_id (INT, FOREIGN KEY -> guests)
room_id (INT, FOREIGN KEY -> rooms)
check_in_date (DATE, NOT NULL)
check_out_date (DATE, NOT NULL)
booking_date (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
number_of_guests (INT, NOT NULL)
status (ENUM: 'pending', 'confirmed', 'checked_in', 'checked_out', 'cancelled')
total_amount (DECIMAL 12,2)
created_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)

INDEXES:
- guest_id
- room_id
- check_in_date
- check_out_date
- status
```

**billing**
```sql
billing_id (INT, PRIMARY KEY, AUTO_INCREMENT)
reservation_id (INT, FOREIGN KEY -> reservations)
room_charge (DECIMAL 12,2, NOT NULL)
service_charge (DECIMAL 12,2, DEFAULT 0)
other_charges (DECIMAL 12,2, DEFAULT 0)
tax_amount (DECIMAL 12,2, DEFAULT 0)
total_bill (DECIMAL 12,2, NOT NULL)
payment_status (ENUM: 'pending', 'partial', 'paid', 'refunded')
payment_date (TIMESTAMP)
notes (TEXT)

INDEXES:
- reservation_id
- payment_status
```

**staff**
```sql
staff_id (INT, PRIMARY KEY, AUTO_INCREMENT)
first_name (VARCHAR 100, NOT NULL)
last_name (VARCHAR 100, NOT NULL)
email (VARCHAR 150, UNIQUE, NOT NULL)
phone (VARCHAR 15, NOT NULL)
position (VARCHAR 100, NOT NULL) -- Manager, Receptionist, Housekeeper, etc.
department (VARCHAR 100, NOT NULL)
salary (DECIMAL 12,2)
joining_date (DATE, NOT NULL)
status (ENUM: 'active', 'inactive', 'on_leave')
password_hash (VARCHAR 255) -- For optional login functionality
created_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)

INDEXES:
- email
- position
- department
```

**services**
```sql
service_id (INT, PRIMARY KEY, AUTO_INCREMENT)
service_name (VARCHAR 150, NOT NULL)
service_type (VARCHAR 100) -- Food, Laundry, Spa, Conference, etc.
price (DECIMAL 10,2, NOT NULL)
description (TEXT)
is_available (BOOLEAN DEFAULT TRUE)
created_at (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
```

**service_bookings**
```sql
service_booking_id (INT, PRIMARY KEY, AUTO_INCREMENT)
reservation_id (INT, FOREIGN KEY -> reservations)
service_id (INT, FOREIGN KEY -> services)
booking_date (TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
quantity (INT, DEFAULT 1)
total_price (DECIMAL 12,2)
status (ENUM: 'pending', 'completed', 'cancelled')

INDEXES:
- reservation_id
- service_id
```

**room_assignments** (Housekeeping/Maintenance tracking)
```sql
assignment_id (INT, PRIMARY KEY, AUTO_INCREMENT)
room_id (INT, FOREIGN KEY -> rooms)
staff_id (INT, FOREIGN KEY -> staff)
assignment_date (DATE, NOT NULL)
assignment_type (VARCHAR 100) -- Cleaning, Maintenance, Inspection, etc.
status (ENUM: 'pending', 'in_progress', 'completed')
notes (TEXT)

INDEXES:
- room_id
- staff_id
- assignment_date
```

### 5.2 Database Connection Strategy

- **Connection Manager:** Singleton pattern with retry logic (3 attempts)
- **Connection Type:** Direct MySQL connection via mysql-connector-java 9.7.0
- **Credentials:** Hardcoded following DRY principle (centralized Constants class)
- **Timeout:** Connection timeout after 3 failed attempts, retry mechanism
- **XAMPP Configuration:** Default MySQL port 3306, localhost

---

## 6. Functional Requirements

### 6.1 Guest Management

**FR-G1: Guest Registration**
- User should be able to register a new guest with mandatory fields: Name, Email, Phone, Address
- Email should be validated and unique
- ID proof details optional but recommended
- Auto-generate guest profile

**FR-G2: Guest Search**
- Search guest by name, email, or phone
- Display guest history (past reservations, total nights, average spending)
- Edit guest details
- View current reservation status

**FR-G3: Guest Profile Management**
- Update guest contact information
- View guest booking history
- Cancel guest account (soft delete if guest has bookings)

### 6.2 Room Management

**FR-R1: Room Inventory**
- Add new rooms with details: Room number, Type, Capacity, Price, Floor
- Update room information
- Delete rooms (only if no active reservations)
- View all rooms with current status

**FR-R2: Room Status Management**
- Track room status: Available, Occupied, Maintenance, Reserved
- Update status based on reservation lifecycle
- Mark rooms for maintenance and track duration
- Auto-update status based on check-in/check-out

**FR-R3: Room Search & Filter**
- Filter rooms by type, capacity, price range, status
- Display availability calendar for room types
- Real-time availability check for booking dates

### 6.3 Reservation Management

**FR-RES1: Create Reservation**
- Select guest and room(s)
- Choose check-in and check-out dates
- Verify room availability for selected period
- Calculate length of stay and room charge
- Auto-generate reservation ID (format: RES-YYYYMMDD-XXXXX)
- Save reservation with status "Confirmed"

**FR-RES2: Modify Reservation**
- Change check-in/check-out dates (if rooms available)
- Change room (if available)
- Update number of guests
- Recalculate charges
- Track modification history

**FR-RES3: Check-in**
- Verify reservation exists
- Update reservation status to "Checked In"
- Update room status to "Occupied"
- Record actual check-in time
- Generate room key/access info display

**FR-RES4: Check-out**
- Finalize billing
- Update reservation status to "Checked Out"
- Update room status to "Available" (or Maintenance if needed)
- Record checkout time
- Print/display final bill

**FR-RES5: Cancel Reservation**
- Allow cancellation with reason
- Refund calculation based on cancellation policy (optional)
- Update room status to "Available"
- Mark reservation as "Cancelled"
- Generate cancellation confirmation

### 6.4 Billing Management

**FR-B1: Auto Bill Generation**
- Generate bill when guest checks out
- Calculate room charges: (Number of nights × Room price per night)
- Add service charges from service_bookings
- Apply any additional charges (late checkout, damages, etc.)
- Calculate tax on total amount (simplified: flat 10%)
- Display itemized breakdown

**FR-B2: Payment Processing**
- Record payment method (Cash, Card, Bank Transfer)
- Update payment status: Pending → Partial → Paid/Refunded
- Accept partial payments
- Generate payment receipts

**FR-B3: Bill Adjustments**
- Add discounts (manager approval optional)
- Add late charges
- Add complimentary charges
- Update billing notes

### 6.5 Service Management

**FR-S1: Service Catalog**
- Display available services (Meals, Laundry, Spa, Conference rooms, etc.)
- Show service pricing
- Enable/disable services

**FR-S2: Service Booking**
- Guest can book services during or after check-in
- Link service bookings to reservations
- Calculate service total automatically
- Update service_bookings table

**FR-S3: Service Bill Integration**
- Automatically include service charges in final bill
- Show service itemization in bill
- Allow service modifications before billing

### 6.6 Staff Management

**FR-ST1: Staff Registration**
- Register staff with: Name, Email, Phone, Position, Department, Salary
- Store password hash (bcrypt or similar)
- Set employment status (Active, Inactive, On Leave)

**FR-ST2: Staff Room Assignment**
- Assign staff to specific rooms/tasks
- Track assignment type (Cleaning, Maintenance, Inspection)
- Mark completion status

**FR-ST3: Staff Directory**
- View all staff members by department
- Filter by position or status
- Update staff information

### 6.7 Dashboard & Reporting

**FR-D1: Executive Dashboard**
- Display occupancy rate (percentage of occupied rooms)
- Show today's check-ins and check-outs count
- Display room status distribution (pie/bar chart)
- Show revenue summary (today, this month, year-to-date)
- Alert pending reservations (next 24 hours)
- Refresh in real-time as data updates

**FR-D2: Guest Invoice Report**
- Generate PDF report with guest details and charges
- Include itemized services and taxes
- Multi-table data: guests, reservations, billing, rooms, services
- Filterable by date range or guest name
- Export capability

**FR-D3: Occupancy & Revenue Analysis**
- Generate PDF report showing occupancy trends
- Include revenue breakdown by room type
- Show staff performance metrics
- Include booking patterns and peak periods
- Multi-table data: reservations, rooms, billing, staff, service_bookings
- Filterable by date range, room type, or staff member

### 6.8 Search & Filter

**FR-SF1: Universal Search**
- Search reservations by ID, guest name, room number
- Search guests by name, email, phone
- Search rooms by number, type, or status
- Auto-complete suggestions

**FR-SF2: Advanced Filters**
- Filter reservations by date range, status, room type
- Filter guests by registration date, booking history
- Filter services by type or availability

---

## 7. Non-Functional Requirements

### 7.1 Performance
- Page load time: < 2 seconds for data tables
- Report generation: < 5 seconds for 5+ years of data
- Database query optimization with smart indexing (no over-indexing)
- Efficient lazy-loading for large datasets

### 7.2 Reliability
- Graceful error handling with user-friendly messages
- Data validation at UI and database layers
- Connection retry mechanism: 3 attempts with 2-second intervals
- Backup of critical operations (optional logging)

### 7.3 Security
- SQL injection prevention using PreparedStatements exclusively
- Input validation and sanitization at UI layer
- Password hashing for staff accounts (bcrypt)
- Database constraints (FOREIGN KEY, UNIQUE, NOT NULL)
- Proper data type enforcement

### 7.4 Usability
- FlatLaf dark theme for macOS aesthetic
- Responsive UI that scales properly
- Intuitive navigation with clear menu structure
- Consistent button placement and terminology
- Helpful error messages with actionable suggestions
- Keyboard navigation support

### 7.5 Maintainability
- MVC architecture for clear separation of concerns
- DAO pattern for data access abstraction
- Design patterns implementation (Singleton, Factory, Strategy, Observer)
- Code organization in logical packages
- Single-line comments for clarity, multi-line only when necessary
- JavaDoc for public methods and exposed classes
- Consistent naming conventions (snake_case for DB, camelCase for Java)

### 7.6 Scalability
- Database schema normalized to 3NF
- Indexed columns for frequently queried fields
- Ability to add new room types without code changes
- Service types configurable in database
- Easy addition of new reports

---

## 8. User Interface Requirements

### 8.1 Main Application Window
- Application title: "Hotel Management System"
- Menu bar with modules: Guests, Rooms, Reservations, Billing, Services, Staff, Reports, Dashboard
- Status bar showing current date/time and connection status
- Look and feel: FlatLaf dark theme (macOS)

### 8.2 Core UI Components

**Dashboard Panel**
- Occupancy gauge/percentage display
- Revenue cards (Today, This Month, YTD)
- Check-in/Check-out counters
- Room status pie/bar chart
- Pending reservations list

**Guest Management Panel**
- Guest list table with pagination
- Guest registration form
- Search bar with filters
- Guest history view
- Edit/Delete functionality

**Room Management Panel**
- Room inventory grid/table
- Room status filter
- Room type categorization
- Add/Edit/Delete room dialog
- Availability calendar view

**Reservation Panel**
- Reservation list with status
- Quick check-in/check-out buttons
- Create reservation wizard
- Search by guest/room/ID
- Modify/Cancel options

**Billing Panel**
- Bill list with payment status
- Bill details view with itemization
- Payment recording interface
- Print/Export bill
- Discount/Adjustment dialog

**Reports Panel**
- Report type selection dropdown
- Date range picker
- Filter options (guest, room type, etc.)
- Generate Report button
- Report preview and export to PDF

### 8.3 Color & Theme
- **Theme:** macOS Dark (FlatLaf built-in)
- **Primary Action Color:** System blue
- **Alert Color:** System red
- **Success Color:** System green
- **Warning Color:** System orange

### 8.4 Dialogs & Forms
- Modal dialogs for critical operations
- Confirmation dialogs for deletions
- Input validation error dialogs with clear messages
- Success notification toasts

---

## 9. Constraints & Assumptions

### 9.1 Technical Constraints
- Java 21 (latest version)
- NetBeans IDE only
- Ant for build management
- XAMPP MySQL (default configuration)
- FlatLaf 3.5.1
- MySQL Connector 9.7.0
- Jasper Reports 7.0.6
- Direct JDBC connections (no ORM framework)

### 9.2 Functional Constraints
- Single hotel operation (no multi-property support)
- No staff authentication system for coursework scope
- No email/SMS integration in base version (optional only)
- Simplified tax calculation (flat percentage)
- No accounting GL integration

### 9.3 Data Constraints
- Room capacity: max 6 guests
- Guest ID proof: storage format as text (not image)
- Email length: max 150 characters
- Phone number: max 15 digits
- Reservation ID format: RES-YYYYMMDD-XXXXX

### 9.4 Assumptions
- Users have basic understanding of hotel operations
- XAMPP is installed and running on localhost:3306
- MySQL user credentials are standard (root/password)
- Each user operates one instance (no concurrent multi-user)
- Historical data will remain within single fiscal year
- Room pricing doesn't change dynamically (no surge pricing)

---

## 10. Deliverables

### 10.1 Code Deliverables
- **Executable JAR file** - `HotelManagementSystem.jar` (executable with all dependencies)
- **Source code repository** - GitHub repository with complete source
- **Database schema** - `schema.sql` file
- **Sample data** - `seed.sql` file with test data
- **Documentation** - README.md with setup instructions

### 10.2 Documentation Deliverables
- **PRD.md** - This document (Product Requirements)
- **code_rules.md** - Code standards and guidelines
- **ARCHITECTURE.md** - Detailed architecture explanation (if highly required)
- **README.md** - Setup and usage instructions
- **Database Schema Diagram** - Visual representation (optional)

### 10.3 Submission Format
- GitHub repository link submitted to LMS
- Repository contains all source code, configuration, and database files
- Clear commit history showing development progression

---

## 11. Success Criteria

### 11.1 Functional Success
- ✓ All CRUD operations work correctly for Guests, Rooms, Reservations, Billing
- ✓ Real-time room availability prevents overbooking
- ✓ Accurate bill calculations with itemization
- ✓ Reports generate successfully with multi-table data
- ✓ Dashboard displays accurate real-time metrics

### 11.2 Technical Success
- ✓ MVC architecture properly implemented
- ✓ Design patterns applied appropriately
- ✓ SQL injection prevention via PreparedStatements
- ✓ Input validation on all forms
- ✓ Password hashing for staff accounts
- ✓ Connection retry logic functioning
- ✓ FlatLaf dark theme applied consistently

### 11.3 Quality Success
- ✓ Code follows defined naming conventions
- ✓ Clean, readable code with appropriate comments
- ✓ JavaDoc on public methods
- ✓ No hardcoded values (constants used)
- ✓ Proper exception handling
- ✓ Database normalization maintained

### 11.4 Deliverable Success
- ✓ Executable JAR runs on any Java 21 system
- ✓ Database setup via manual SQL scripts
- ✓ GitHub repository with clear commit history
- ✓ Documentation complete and accurate

---

## 12. Marking Rubric Alignment

| Rubric Factor | Target | How Achieved |
|---------------|--------|-------------|
| **User Interface (20)** | Excellent | Professional FlatLaf dark theme, 5+ specialized panels, responsive layout |
| **Reports (10)** | Excellent | 2 comprehensive multi-table reports with Jasper, exportable to PDF |
| **Industry Standards (20)** | Excellent | Full MVC implementation, design patterns, OOP principles, smart constants |
| **Scope Covered (20)** | Very High | 8 interconnected modules, complex business logic, 6+ user roles |
| **Validation & Exception (10)** | Excellent | Input validation, custom exceptions, proper error handling |
| **Deployment (10)** | Excellent | Executable JAR, schema + seed scripts, GitHub repository |
| **Viva & Presentation (10)** | Excellent | Clear architecture, design decisions documented, demo-ready |

---

## 13. Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | June 2026 | Initial PRD creation |

---

## Appendix A: Glossary

- **HMS:** Hotel Management System
- **CRUD:** Create, Read, Update, Delete operations
- **DAO:** Data Access Object pattern
- **MVC:** Model-View-Controller architecture
- **XAMPP:** Cross-platform web development environment with MySQL
- **PreparedStatement:** SQL statement with pre-compiled query (prevents SQL injection)
- **FlatLaf:** Flat, modern look-and-feel for Swing applications
- **Jasper Reports:** Open-source reporting tool
- **Singleton:** Design pattern ensuring single instance of a class

---

**Document End**
