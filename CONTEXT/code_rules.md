# Hotel Management System - Code Rules & Standards

**Version:** 1.1  
**Date:** June 2026  
**IDE:** NetBeans (Only)  
**Build Tool:** Apache Ant  
**Java Version:** 21 (Latest)

---

## Table of Contents
1. [Project Structure](#1-project-structure)
2. [Naming Conventions](#2-naming-conventions)
3. [Code Style & Formatting](#3-code-style--formatting)
4. [Package Organization](#4-package-organization)
5. [Class & Interface Guidelines](#5-class--interface-guidelines)
6. [Method Guidelines](#6-method-guidelines)
7. [Variable & Constant Guidelines](#7-variable--constant-guidelines)
8. [Database & SQL Guidelines](#8-database--sql-guidelines)
9. [Exception Handling](#9-exception-handling)
10. [Comments & Documentation](#10-comments--documentation)
11. [Security Guidelines](#11-security-guidelines)
12. [Design Patterns Implementation](#12-design-patterns-implementation)
13. [UI/Component Guidelines](#13-uicomponent-guidelines)
14. [Git Workflow](#14-git-workflow)
15. [Testing & Validation](#15-testing--validation)

---

## 1. Project Structure

```
hotel-management-system/
├── src/
│   └── com/
│       └── hotelms/
│           ├── config/              # Configuration & Constants
│           ├── database/            # Database connections
│           ├── model/               # Data models (Entities)
│           ├── dao/                 # Data Access Objects
│           ├── service/             # Business logic (if needed)
│           ├── controller/          # Controllers for UI coordination
│           ├── view/                # UI panels and dialogs
│           ├── util/                # Utility classes
│           ├── exception/           # Custom exceptions
│           └── HotelManagementApp.java  # Main entry point
├── database/
│   ├── schema.sql                   # Database schema
│   └── seed.sql                     # Sample test data
├── build.xml                        # Ant build configuration
├── lib/                             # External libraries (JAR files)
├── dist/                            # Compiled JAR output
├── PRD.md                           # Product Requirements Document
├── code_rules.md                    # This file
├── README.md                        # Setup & usage instructions
└── .gitignore                       # Git ignore rules
```

---

## 2. Naming Conventions

### 2.1 Java Code Naming

| Element | Convention | Example | Notes |
|---------|-----------|---------|-------|
| **Class Names** | PascalCase | `GuestController`, `ReservationDAO`, `DatabaseConnection` | One word, clear intent |
| **Interface Names** | PascalCase, start with `I` (optional) | `IGuestDAO` or `GuestRepository` | Descriptive, no "I" prefix preferred |
| **Method Names** | camelCase, verb-noun | `getGuestById()`, `createReservation()`, `validateEmail()` | Start with action verb |
| **Variable Names** | camelCase | `guestId`, `roomNumber`, `checkInDate` | Meaningful, avoid single letters (except loops) |
| **Constant Names** | UPPER_SNAKE_CASE | `MAX_GUESTS`, `DB_TIMEOUT_SECONDS`, `DEFAULT_THEME` | In Constants class |
| **Package Names** | lowercase, dot-separated | `com.hotelms.dao`, `com.hotelms.util` | Reverse domain naming |
| **Boolean Variables** | Prefix with `is`, `has`, `can` | `isAvailable`, `hasBooking`, `canCheckOut` | Clear boolean intent |
| **Collection Variables** | Plural names | `guestList`, `roomMap`, `serviceSet` | Clearly indicates multiple items |

### 2.2 Database Naming

| Element | Convention | Example | Notes |
|---------|-----------|---------|-------|
| **Table Names** | snake_case, plural | `guests`, `reservations`, `room_assignments` | Plural, clear purpose |
| **Column Names** | snake_case, singular | `guest_id`, `first_name`, `check_in_date` | Lowercase, snake_case |
| **Primary Keys** | `{table_name}_id` (singular) | `guest_id`, `room_id`, `reservation_id` | Consistent format |
| **Foreign Keys** | `{referenced_table}_id` | `guest_id` (in reservations table) | Clear reference |
| **Boolean Columns** | Prefix with `is_` | `is_available`, `is_active` | Clarify boolean type |
| **Timestamp Columns** | `created_at`, `updated_at` | Standard audit columns | TIMESTAMP type |
| **Index Names** | `idx_{table}_{column}` | `idx_guests_email`, `idx_reservations_status` | Descriptive index names |

---

## 3. Code Style & Formatting

### 3.1 Indentation & Spacing

```java
// ✓ CORRECT: 4-space indentation
public class GuestController {
    private GuestDAO guestDAO;
    
    public void createGuest(Guest guest) {
        if (validateGuest(guest)) {
            guestDAO.save(guest);
        }
    }
}

// ✗ INCORRECT: Tab indentation or 2-space
public class GuestController {
  private GuestDAO guestDAO;
  
  public void createGuest(Guest guest) {
    if (validateGuest(guest)) {
      guestDAO.save(guest);
    }
  }
}
```

### 3.2 Line Length

- **Maximum line length:** 100 characters
- **Exception:** URLs, long strings that can't be broken

```java
// ✓ CORRECT: Line wrapped at 100 chars
String errorMessage = "Guest with ID " + guestId 
    + " not found in the system. Please verify the ID.";

// ✗ INCORRECT: Line exceeds 100 characters
String errorMessage = "Guest with ID " + guestId + " not found in the system. Please verify the ID and try again.";
```

### 3.3 Braces

- **Style:** Java style (opening brace on same line)
- **Always use braces** - even for single-line blocks

```java
// ✓ CORRECT: Java style braces
public void processReservation(Reservation reservation) {
    if (reservation.isValid()) {
        createBill(reservation);
    } else {
        showError("Invalid reservation");
    }
}

// ✗ INCORRECT: Allman style or no braces
public void processReservation(Reservation reservation)
{
    if (reservation.isValid())
        createBill(reservation);
}
```

### 3.4 Spacing Around Keywords & Operators

```java
// ✓ CORRECT: Space after keywords, around operators
if (guestId > 0) {
    checkInDate = new Date();
    totalPrice = roomPrice * numberOfNights;
}

// ✗ INCORRECT: No spacing
if(guestId>0){
    checkInDate=new Date();
    totalPrice=roomPrice*numberOfNights;
}
```

### 3.5 Blank Lines

- One blank line between methods in a class
- Two blank lines between inner classes
- No blank lines at start/end of method or class

```java
public class ReservationManager {
    
    public void createReservation(Reservation res) {
        // Implementation
    }
    
    public void cancelReservation(int resId) {
        // Implementation
    }
}
```

---

## 4. Package Organization

### 4.1 Core Packages

```
com.hotelms.config
├── Constants.java          # All application-wide constants
└── AppConfig.java          # Configuration settings (if needed)

com.hotelms.database
├── DatabaseConnection.java # Singleton connection manager
└── ConnectionPool.java     # Connection pooling (if needed)

com.hotelms.model
├── Guest.java
├── Room.java
├── Reservation.java
├── Billing.java
├── Staff.java
├── Service.java
└── RoomAssignment.java

com.hotelms.dao
├── GuestDAO.java
├── RoomDAO.java
├── ReservationDAO.java
├── BillingDAO.java
├── StaffDAO.java
├── ServiceDAO.java
└── RoomAssignmentDAO.java

com.hotelms.controller
├── GuestController.java
├── RoomController.java
├── ReservationController.java
├── BillingController.java
├── StaffController.java
└── ServiceController.java

com.hotelms.view
├── MainWindow.java
├── panels/
│   ├── DashboardPanel.java
│   ├── GuestPanel.java
│   ├── RoomPanel.java
│   ├── ReservationPanel.java
│   ├── BillingPanel.java
│   ├── ServicePanel.java
│   ├── StaffPanel.java
│   └── ReportsPanel.java
└── dialogs/
    ├── GuestDialog.java
    ├── ReservationDialog.java
    ├── CheckInDialog.java
    └── CheckOutDialog.java

com.hotelms.util
├── ValidationUtil.java     # Input validation
├── DateUtil.java           # Date operations
├── ReportUtil.java         # Report generation
├── IconUtil.java           # Icon loading
├── StringUtil.java         # String operations
└── PasswordUtil.java       # Password hashing

com.hotelms.exception
├── ApplicationException.java    # Base custom exception
├── DatabaseException.java
├── ValidationException.java
├── ReservationException.java
└── BillingException.java

com.hotelms
└── HotelManagementApp.java     # Main entry point
```

### 4.2 Package Responsibilities

- **config:** All constants, configuration values (DRY principle)
- **database:** Database connection management (Singleton pattern)
- **model:** Plain data classes (POJOs) representing domain objects
- **dao:** Database CRUD operations, prepared statements
- **controller:** Orchestrate user actions, call DAOs and models
- **view:** Swing UI components, FlatLaf components
- **util:** Reusable utility methods
- **exception:** Custom exception classes
- **Main class:** Application entry point, UI initialization

---

## 5. Class & Interface Guidelines

### 5.1 Class Structure Order

```java
public class ReservationManager {
    // 1. Static fields (constants)
    private static final Logger logger = LoggerFactory.getLogger(ReservationManager.class);
    
    // 2. Static methods
    public static ReservationManager getInstance() {
        // Implementation
    }
    
    // 3. Instance fields
    private ReservationDAO reservationDAO;
    private GuestDAO guestDAO;
    private RoomDAO roomDAO;
    
    // 4. Constructors
    public ReservationManager() {
        this.reservationDAO = new ReservationDAO();
        this.guestDAO = new GuestDAO();
        this.roomDAO = new RoomDAO();
    }
    
    // 5. Public methods
    public void createReservation(Reservation reservation) throws ReservationException {
        // Implementation
    }
    
    public Reservation getReservationById(int id) {
        // Implementation
    }
    
    // 6. Private methods
    private boolean validateDates(Date checkIn, Date checkOut) {
        // Implementation
    }
    
    private void updateRoomStatus(Room room, String status) {
        // Implementation
    }
}
```

### 5.2 Class Size

- **Target:** Keep classes focused (Single Responsibility Principle)
- **Maximum lines:** 500 lines per class (guide, not strict)
- **Maximum methods:** 15-20 methods per class
- **If exceeded:** Consider splitting into separate classes

### 5.3 Immutability

```java
// ✓ CORRECT: Immutable model class
public final class Guest {
    private final int guestId;
    private final String firstName;
    private final String email;
    
    public Guest(int guestId, String firstName, String email) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.email = email;
    }
    
    public int getGuestId() {
        return guestId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getEmail() {
        return email;
    }
    // No setters
}

// ✗ INCORRECT: Mutable model class (avoid)
public class Guest {
    public int guestId;
    public String firstName;
    
    public void setGuestId(int id) { this.guestId = id; }
    public void setFirstName(String name) { this.firstName = name; }
}
```

### 5.4 Visibility

```java
public class ReservationDAO {
    // ✓ Private fields with getters
    private ReservationDAO reservationDAO;
    
    // ✓ Package-private helper methods
    void updateRoomStatus(int roomId) {
        // Accessible within com.hotelms.dao package
    }
    
    // ✓ Private utility methods
    private PreparedStatement buildSelectStatement(int id) {
        // Internal helper
    }
    
    // ✓ Public interface methods
    public Reservation getById(int id) {
        // Called by controllers
    }
}
```

---

## 6. Method Guidelines

### 6.1 Method Signature

```java
// ✓ CORRECT: Clear, focused methods
public Reservation createReservation(Guest guest, Room room, Date checkIn, Date checkOut) 
        throws ReservationException, ValidationException {
    validateInputs(guest, room, checkIn, checkOut);
    
    if (!isRoomAvailable(room.getId(), checkIn, checkOut)) {
        throw new ReservationException("Room not available for selected dates");
    }
    
    Reservation reservation = new Reservation(guest, room, checkIn, checkOut);
    reservationDAO.save(reservation);
    return reservation;
}

// ✗ INCORRECT: Too many parameters, unclear purpose
public void doStuff(int a, String b, Date c, Date d, boolean e, int f, String g) {
    // What does this method actually do?
}
```

### 6.2 Method Length

- **Target:** 15-30 lines per method
- **Maximum:** 50 lines (extract sub-methods if longer)
- **Exception:** Complex algorithms

```java
// ✓ CORRECT: Readable, focused methods
public void processCheckOut(Reservation reservation) {
    validateReservation(reservation);
    
    Billing bill = generateBill(reservation);
    saveBill(bill);
    
    updateRoomStatus(reservation.getRoom(), "available");
    updateReservationStatus(reservation, "checked_out");
    
    displayBillDialog(bill);
}

// ✗ INCORRECT: Too long, mixed concerns
public void processCheckOut(Reservation reservation) {
    // 100 lines of mixed logic here
    // Database operations, billing, room updates all in one method
}
```

### 6.3 Return Types

```java
// ✓ CORRECT: Clear return types
public Guest getGuestById(int id) {
    // Returns single guest or throws exception
}

public List<Reservation> getReservationsByGuest(int guestId) {
    // Returns list (never null, use empty list if no results)
}

public boolean isRoomAvailable(int roomId, Date checkIn, Date checkOut) {
    // Boolean for yes/no questions
}

// ✗ INCORRECT: Ambiguous returns
public Object getData(String type) {  // What's returned?
    // Could return Guest, Room, or List - confusing
}

public List<Reservation> findReservations() {  // Could return null
    if (noResults) {
        return null;  // Avoid null returns, use empty list
    }
}
```

### 6.4 Method Parameters

```java
// ✓ CORRECT: Limited parameters
public void updateReservation(int reservationId, Date checkOutDate) {
    // 2 parameters, clear purpose
}

// ✗ INCORRECT: Too many parameters
public void updateReservation(int id, String gName, String gEmail, 
        String gPhone, int rId, Date chkIn, Date chkOut, int guests, 
        int amount, String status) {
    // 10 parameters - use object instead
}

// ✓ BETTER: Use object parameter
public void updateReservation(Reservation reservation) {
    // Single object, all related data together
}
```

---

## 7. Variable & Constant Guidelines

### 7.1 Constant Definition

All constants must be in `Constants.java` following DRY principle:

```java
// com/hotelms/config/Constants.java
public class Constants {
    
    // Database Configuration
    public static final String DB_HOST = "localhost";
    public static final int DB_PORT = 3306;
    public static final String DB_NAME = "hotel_management_system";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "password";
    
    // Connection Settings
    public static final int DB_CONNECTION_TIMEOUT = 30;
    public static final int DB_MAX_RETRIES = 3;
    public static final int DB_RETRY_DELAY_MS = 2000;
    
    // UI Constants
    public static final int WINDOW_WIDTH = 1400;
    public static final int WINDOW_HEIGHT = 900;
    public static final String APP_TITLE = "Hotel Management System";
    
    // Reservation Constants
    public static final String RES_ID_PREFIX = "RES";
    public static final int MIN_GUESTS = 1;
    public static final int MAX_GUESTS = 6;
    
    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_EMAIL_LENGTH = 150;
    public static final int MAX_PHONE_LENGTH = 15;
    
    // Status Constants
    public static final String ROOM_STATUS_AVAILABLE = "available";
    public static final String ROOM_STATUS_OCCUPIED = "occupied";
    public static final String ROOM_STATUS_MAINTENANCE = "maintenance";
    
    // Tax & Pricing
    public static final double DEFAULT_TAX_RATE = 0.10;  // 10%
    public static final double LATE_CHECKOUT_CHARGE = 50.0;
    
    // Report Paths
    public static final String JASPER_REPORT_PATH = "/com/hotelms/reports/";
    
    // Error Messages
    public static final String ERROR_DB_CONNECTION = "Failed to connect to database";
    public static final String ERROR_INVALID_EMAIL = "Invalid email format";
}
```

### 7.2 Using Constants

```java
// ✓ CORRECT: Use Constants class
public class RoomDAO {
    
    public List<Room> getAllAvailableRooms() throws DatabaseException {
        String sql = "SELECT * FROM rooms WHERE status = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, Constants.ROOM_STATUS_AVAILABLE);
            // Implementation
        } catch (SQLException e) {
            throw new DatabaseException(Constants.ERROR_DB_CONNECTION);
        }
    }
}

// ✗ INCORRECT: Hardcoded values
public class RoomDAO {
    
    public List<Room> getAllAvailableRooms() throws DatabaseException {
        String sql = "SELECT * FROM rooms WHERE status = ?";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, "available");  // Hardcoded!
            pstmt.setDouble(2, 50.0);         // Magic number!
            // Implementation
        }
    }
}
```

### 7.3 Variable Declaration

```java
// ✓ CORRECT: Declare close to use, meaningful names
public void createReservation(Guest guest, Room room, Date checkIn, Date checkOut) {
    // Only declare what you need
    double roomPrice = room.getPrice();
    int numberOfNights = calculateNights(checkIn, checkOut);
    double totalAmount = roomPrice * numberOfNights;
    
    Reservation reservation = new Reservation(guest, room, checkIn, checkOut);
    reservation.setAmount(totalAmount);
}

// ✗ INCORRECT: Declared too early, ambiguous names
public void createReservation(Guest guest, Room room, Date checkIn, Date checkOut) {
    int a = 0;
    int b = 0;
    int c = 0;
    String s = "";
    
    a = room.getId();
    b = calculateNights(checkIn, checkOut);
    c = (int)(room.getPrice() * b);
    // ... 50 lines later, actually use these variables
}
```

### 7.4 Collections

```java
// ✓ CORRECT: Initialize empty collections, never return null
public List<Reservation> getReservationsByGuest(int guestId) {
    List<Reservation> reservations = new ArrayList<>();
    
    String sql = "SELECT * FROM reservations WHERE guest_id = ?";
    try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
        pstmt.setInt(1, guestId);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            reservations.add(mapResultSetToReservation(rs));
        }
    }
    
    return reservations;  // Empty list if no results, never null
}

// ✗ INCORRECT: Null returns, unclear types
public Object getReservations(int guestId) {
    List<Reservation> reservations = null;
    // ... code ...
    if (reservations.isEmpty()) {
        return null;  // Caller must check for null
    }
    return reservations;
}
```

---

## 8. Database & SQL Guidelines

### 8.1 PreparedStatements (MANDATORY)

```java
// ✓ CORRECT: Always use PreparedStatements
public Guest getGuestById(int guestId) throws DatabaseException {
    String sql = "SELECT * FROM guests WHERE guest_id = ?";
    
    Connection conn = DatabaseConnection.getInstance().getConnection();
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, guestId);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            return mapResultSetToGuest(rs);
        }
    } catch (SQLException e) {
        throw new DatabaseException("Failed to retrieve guest: " + e.getMessage());
    }
    
    return null;
}

// ✗ INCORRECT: String concatenation (SQL Injection vulnerability!)
public Guest getGuestById(int guestId) throws DatabaseException {
    String sql = "SELECT * FROM guests WHERE guest_id = " + guestId;
    
    try (Connection conn = DatabaseConnection.getInstance().getConnection();
         Statement stmt = conn.createStatement()) {
        
        ResultSet rs = stmt.executeQuery(sql);  // UNSAFE!
        // ...
    }
}
```

### 8.2 SQL Query Guidelines

```java
// ✓ CORRECT: Readable, parameterized queries
String sql = "SELECT guest_id, first_name, last_name, email, phone "
           + "FROM guests "
           + "WHERE email = ? "
           + "AND created_at >= ? "
           + "ORDER BY created_at DESC";

// ✓ CORRECT: Meaningful variable names
pstmt.setString(1, guestEmail);
pstmt.setDate(2, sqlDate);

// ✗ INCORRECT: String concatenation
String sql = "SELECT * FROM guests WHERE email = '" + email + "' AND id = " + id;

// ✗ INCORRECT: No clear query structure
String sql = "SELECT * FROM guests WHERE email=? and created_at>=?";
```

### 8.3 Connection Management

```java
// ✓ CORRECT: Try-with-resources on Statement/ResultSet only
public List<Room> getAllRooms() throws DatabaseException {
    String sql = "SELECT * FROM rooms ORDER BY room_number";
    
    Connection conn = DatabaseConnection.getInstance().getConnection();
    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        
        List<Room> rooms = new ArrayList<>();
        while (rs.next()) {
            rooms.add(mapResultSetToRoom(rs));
        }
        return rooms;
        
    } catch (SQLException e) {
        throw new DatabaseException("Failed to retrieve rooms: " + e.getMessage());
    }
    // Connection stays open — Singleton manages its lifecycle
}

// ✗ INCORRECT: No try-with-resources (resource leak)
public List<Room> getAllRooms() throws DatabaseException {
    String sql = "SELECT * FROM rooms ORDER BY room_number";
    
    Connection conn = DatabaseConnection.getInstance().getConnection();
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
    
    List<Room> rooms = new ArrayList<>();
    while (rs.next()) {
        rooms.add(mapResultSetToRoom(rs));
    }
    
    // Statement and ResultSet may not be closed if exception occurs!
    return rooms;
}
```

### 8.4 Index Usage

```sql
-- ✓ Indexes on frequently queried columns
CREATE INDEX idx_guests_email ON guests(email);
CREATE INDEX idx_reservations_guest_id ON reservations(guest_id);
CREATE INDEX idx_reservations_room_id ON reservations(room_id);
CREATE INDEX idx_reservations_status ON reservations(status);
CREATE INDEX idx_billing_reservation_id ON billing(reservation_id);

-- ✗ Over-indexing (avoid)
CREATE INDEX idx_room_floor ON rooms(floor);
CREATE INDEX idx_room_capacity ON rooms(capacity);
CREATE INDEX idx_room_type_capacity ON rooms(room_type, capacity);
-- Only index if these columns are in WHERE clauses
```

### 8.5 Transaction Management

```java
// ✓ CORRECT: Transactional operations (Connection lifecycle by Singleton)
public void createReservation(Reservation reservation) throws DatabaseException {
    Connection conn = DatabaseConnection.getInstance().getConnection();
    try {
        conn.setAutoCommit(false);
        
        // Save reservation
        reservationDAO.save(reservation);
        
        // Update room status
        roomDAO.updateStatus(reservation.getRoom().getId(), "reserved");
        
        conn.commit();
    } catch (Exception e) {
        try {
            conn.rollback();
        } catch (SQLException sqle) {
            throw new DatabaseException("Rollback failed: " + sqle.getMessage());
        }
        throw new DatabaseException("Reservation creation failed: " + e.getMessage());
    } finally {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            // Restore default — non-critical cleanup
        }
    }
    // Connection stays open — Singleton manages its lifecycle
}
```

---

## 9. Exception Handling

### 9.1 Custom Exceptions

All custom exceptions extend `ApplicationException`:

```java
// com/hotelms/exception/ApplicationException.java
public class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }
    
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

// com/hotelms/exception/ValidationException.java
public class ValidationException extends ApplicationException {
    public ValidationException(String message) {
        super(message);
    }
}

// com/hotelms/exception/DatabaseException.java
public class DatabaseException extends ApplicationException {
    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

// com/hotelms/exception/ReservationException.java
public class ReservationException extends ApplicationException {
    public ReservationException(String message) {
        super(message);
    }
}

// com/hotelms/exception/BillingException.java
public class BillingException extends ApplicationException {
    public BillingException(String message) {
        super(message);
    }
}
```

### 9.2 Exception Handling Pattern

```java
// ✓ CORRECT: Meaningful exception handling
public class ReservationController {
    
    public void createReservation(Guest guest, Room room, Date checkIn, Date checkOut) {
        try {
            // Validate inputs
            if (guest == null || room == null) {
                throw new ValidationException("Guest and Room cannot be null");
            }
            
            if (checkOut.before(checkIn)) {
                throw new ValidationException("Checkout date must be after check-in");
            }
            
            // Business logic
            if (!isRoomAvailable(room.getId(), checkIn, checkOut)) {
                throw new ReservationException("Room not available for selected dates");
            }
            
            Reservation reservation = new Reservation(guest, room, checkIn, checkOut);
            reservationDAO.save(reservation);
            
            // Success feedback
            showSuccessMessage("Reservation created successfully");
            
        } catch (ValidationException e) {
            showErrorDialog("Validation Error", e.getMessage());
        } catch (ReservationException e) {
            showErrorDialog("Reservation Error", e.getMessage());
        } catch (DatabaseException e) {
            showErrorDialog("Database Error", "Failed to save reservation. Please try again.");
        } catch (Exception e) {
            showErrorDialog("Error", "An unexpected error occurred");
        }
    }
}

// ✗ INCORRECT: Poor exception handling
public class ReservationController {
    
    public void createReservation(Guest guest, Room room, Date checkIn, Date checkOut) {
        try {
            // No validation
            Reservation reservation = new Reservation(guest, room, checkIn, checkOut);
            reservationDAO.save(reservation);
        } catch (Exception e) {
            // Catches everything, loses information
            e.printStackTrace();
        }
    }
}
```

### 9.3 Throwing Exceptions

```java
// ✓ CORRECT: Throw with meaningful messages
public double calculateBill(Reservation reservation) throws BillingException {
    if (reservation == null) {
        throw new BillingException("Reservation cannot be null");
    }
    
    if (reservation.getCheckOutDate() == null) {
        throw new BillingException("Checkout date is required for billing calculation");
    }
    
    // Calculate bill
    return calculateAmount(reservation);
}

// ✗ INCORRECT: Vague error messages
public double calculateBill(Reservation reservation) throws BillingException {
    if (reservation == null) {
        throw new BillingException("Error");  // What error?
    }
    
    if (reservation.getCheckOutDate() == null) {
        throw new BillingException("Failed");  // What failed?
    }
}
```

---

## 10. Comments & Documentation

### 10.1 JavaDoc Standard

JavaDoc for **public methods** and **public classes only**:

```java
/**
 * Creates a new reservation in the system.
 * 
 * @param guest The guest making the reservation (not null)
 * @param room The room to be reserved (not null)
 * @param checkInDate The check-in date (not null, must be in future)
 * @param checkOutDate The check-out date (not null, must be after check-in)
 * @return The created Reservation object
 * @throws ValidationException If guest, room, or dates are invalid
 * @throws ReservationException If room is not available for the selected dates
 * @throws DatabaseException If database operation fails
 */
public Reservation createReservation(Guest guest, Room room, 
        Date checkInDate, Date checkOutDate) 
        throws ValidationException, ReservationException, DatabaseException {
    // Implementation
}

// ✓ Class-level JavaDoc
/**
 * Manages all reservation-related operations including creation, modification,
 * cancellation, and status tracking. This controller orchestrates interactions
 * between the UI, business logic, and data access layers.
 */
public class ReservationController {
    // Implementation
}

// ✗ INCORRECT: No JavaDoc on public method
public Reservation createReservation(Guest guest, Room room, 
        Date checkInDate, Date checkOutDate) {
    // What does this do? What are the parameters? What exceptions?
}
```

### 10.2 Inline Comments

Use single-line comments sparingly for **non-obvious logic only**:

```java
// ✓ CORRECT: Explains the "why"
public List<Reservation> getUpcomingReservations() {
    List<Reservation> reservations = new ArrayList<>();
    Date today = new Date();
    
    // Fetch reservations for next 7 days to allow advance room preparation
    Date nextWeek = new Date(today.getTime() + (7 * 24 * 60 * 60 * 1000));
    
    String sql = "SELECT * FROM reservations WHERE check_in_date BETWEEN ? AND ? "
               + "AND status = ?";
    
    try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
        pstmt.setDate(1, new java.sql.Date(today.getTime()));
        pstmt.setDate(2, new java.sql.Date(nextWeek.getTime()));
        pstmt.setString(3, Constants.RES_STATUS_CONFIRMED);
        
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            reservations.add(mapResultSetToReservation(rs));
        }
    }
    
    return reservations;
}

// ✗ INCORRECT: Obvious comments clutter code
public List<Reservation> getUpcomingReservations() {
    List<Reservation> reservations = new ArrayList<>();  // Create a list
    Date today = new Date();  // Get today's date
    
    // Add 7 days to today's date
    long weekInMillis = 7 * 24 * 60 * 60 * 1000;
    Date nextWeek = new Date(today.getTime() + weekInMillis);
    
    // Select all reservations
    String sql = "SELECT * FROM reservations WHERE check_in_date BETWEEN ? AND ?";
    
    // Try-with-resources for connection
    try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
        // Set first parameter
        pstmt.setDate(1, new java.sql.Date(today.getTime()));
        // Set second parameter
        pstmt.setDate(2, new java.sql.Date(nextWeek.getTime()));
        
        // Execute query
        ResultSet rs = pstmt.executeQuery();
        // Loop through results
        while (rs.next()) {
            // Add to list
            reservations.add(mapResultSetToReservation(rs));
        }
    }
    
    // Return list
    return reservations;
}
```

### 10.3 TODO Comments

Only use when action is genuinely pending:

```java
// ✓ ACCEPTABLE: Legitimate future work
public void generateReport(String reportType) throws Exception {
    // TODO: Implement email delivery of reports in future version
    // For now, generate PDF and save locally
    generatePDFReport(reportType);
}

// ✗ AVOID: Dead TODO comments
public void generateReport(String reportType) throws Exception {
    // TODO: Fix this method  (Comment written 6 months ago, no detail)
    // TODO: Optimize performance  (No specific plan)
    
    generatePDFReport(reportType);
}
```

---

## 11. Security Guidelines

### 11.1 SQL Injection Prevention

**MANDATORY: Always use PreparedStatements**

```java
// ✓ CORRECT: PreparedStatement protects against SQL injection
String sql = "SELECT * FROM guests WHERE email = ? AND status = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, userEmail);      // Safe
pstmt.setString(2, "active");       // Safe
ResultSet rs = pstmt.executeQuery();

// ✗ DANGEROUS: String concatenation allows SQL injection
String sql = "SELECT * FROM guests WHERE email = '" + userEmail + "'";
// If userEmail = "test@test.com' OR '1'='1", entire table is returned!

// ✗ DANGEROUS: String formatting
String sql = String.format("SELECT * FROM guests WHERE email = '%s'", userEmail);
// Still vulnerable to SQL injection
```

### 11.2 Password Hashing

For staff accounts using bcrypt:

```java
// com/hotelms/util/PasswordUtil.java
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    
    /**
     * Hashes a plain-text password using bcrypt.
     * 
     * @param plainPassword The password to hash
     * @return The hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }
    
    /**
     * Verifies a plain-text password against a hashed password.
     * 
     * @param plainPassword The plain-text password to verify
     * @param hashedPassword The hashed password from database
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

// Usage in Staff DAO
public boolean authenticateStaff(String email, String plainPassword) throws DatabaseException {
    String sql = "SELECT password_hash FROM staff WHERE email = ? AND status = ?";
    
    try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
        pstmt.setString(1, email);
        pstmt.setString(2, "active");
        
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String storedHash = rs.getString("password_hash");
            return PasswordUtil.verifyPassword(plainPassword, storedHash);
        }
        return false;
        
    } catch (SQLException e) {
        throw new DatabaseException("Authentication failed: " + e.getMessage());
    }
}

// ✗ NEVER: Store plain-text passwords
public boolean authenticateStaff(String email, String password) {
    String sql = "SELECT password FROM staff WHERE email = ? AND password = ?";
    // Plain password in database = CRITICAL SECURITY VULNERABILITY
}
```

### 11.3 Input Validation

```java
// com/hotelms/util/ValidationUtil.java
public class ValidationUtil {
    
    /**
     * Validates email format using regex.
     */
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }
    
    /**
     * Validates phone number (15 digits max).
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.replaceAll("[^0-9]", "").length() <= 15;
    }
    
    /**
     * Validates date is not in the past.
     */
    public static boolean isFutureDate(Date date) {
        return date != null && date.after(new Date());
    }
    
    /**
     * Validates name (alphanumeric and spaces only).
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z\\s]+$");
    }
}

// Usage
public void createGuest(String email, String phone, String name) throws ValidationException {
    if (!ValidationUtil.isValidEmail(email)) {
        throw new ValidationException("Invalid email format");
    }
    
    if (!ValidationUtil.isValidPhone(phone)) {
        throw new ValidationException("Invalid phone number");
    }
    
    if (!ValidationUtil.isValidName(name)) {
        throw new ValidationException("Name can only contain letters and spaces");
    }
    
    // Proceed with guest creation
}
```

### 11.4 Data Sanitization

```java
// ✓ CORRECT: Trim input and use PreparedStatements
String userInput = jTextFieldName.getText().trim();  // Remove whitespace
String sanitizedInput = userInput.replaceAll("^\\s+|\\s+$", "");  // Additional trim

String sql = "SELECT * FROM guests WHERE first_name = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, sanitizedInput);  // Safe, parameterized
```

---

## 12. Design Patterns Implementation

### 12.1 Singleton Pattern (Database Connection)

```java
// com/hotelms/database/DatabaseConnection.java
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;
    private int retryCount = 0;
    
    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Gets the singleton instance of DatabaseConnection.
     * Uses lazy initialization with double-check locking.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Gets or creates a database connection with retry logic.
     */
    public Connection getConnection() throws DatabaseException {
        if (connection == null || !isConnectionValid()) {
            connectToDatabase();
        }
        return connection;
    }
    
    private void connectToDatabase() throws DatabaseException {
        while (retryCount < Constants.DB_MAX_RETRIES) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                String url = "jdbc:mysql://" + Constants.DB_HOST + ":" + Constants.DB_PORT 
                           + "/" + Constants.DB_NAME;
                
                connection = DriverManager.getConnection(url, 
                        Constants.DB_USER, Constants.DB_PASSWORD);
                
                retryCount = 0;  // Reset retry count on success
                return;
                
            } catch (ClassNotFoundException | SQLException e) {
                retryCount++;
                
                if (retryCount < Constants.DB_MAX_RETRIES) {
                    try {
                        Thread.sleep(Constants.DB_RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new DatabaseException(Constants.ERROR_DB_CONNECTION + ": " + e.getMessage());
                }
            }
        }
    }
    
    private boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed() 
                && connection.isValid(Constants.DB_CONNECTION_TIMEOUT);
        } catch (SQLException e) {
            return false;
        }
    }
    
    public void closeConnection() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to close connection: " + e.getMessage());
        }
    }
}

// Usage throughout application — Connection outside try-with-resources
public class GuestDAO {
    
    public Guest getGuestById(int id) throws DatabaseException {
        String sql = "SELECT * FROM guests WHERE guest_id = ?";
        
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToGuest(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve guest: " + e.getMessage());
        }
        
        return null;
    }
}
```

### 12.2 DAO Pattern

```java
// com/hotelms/dao/ReservationDAO.java
public class ReservationDAO {
    
    /**
     * Retrieves a reservation by ID.
     */
    public Reservation getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve reservation: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Saves a new reservation.
     */
    public Reservation save(Reservation reservation) throws DatabaseException {
        String sql = "INSERT INTO reservations (guest_id, room_id, check_in_date, "
                   + "check_out_date, booking_date, number_of_guests, status, total_amount) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, 
                Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, reservation.getGuest().getGuestId());
            pstmt.setInt(2, reservation.getRoom().getRoomId());
            pstmt.setDate(3, new java.sql.Date(reservation.getCheckInDate().getTime()));
            pstmt.setDate(4, new java.sql.Date(reservation.getCheckOutDate().getTime()));
            pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.setInt(6, reservation.getNumberOfGuests());
            pstmt.setString(7, "confirmed");
            pstmt.setDouble(8, reservation.getTotalAmount());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save reservation");
            }
            
            // Get generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Reservation(
                            generatedKeys.getInt(1),
                            reservation.getGuest(),
                            reservation.getRoom(),
                            reservation.getCheckInDate(),
                            reservation.getCheckOutDate(),
                            reservation.getBookingDate(),
                            reservation.getNumberOfGuests(),
                            reservation.getStatus(),
                            reservation.getTotalAmount(),
                            reservation.getCreatedByStaffId(),
                            reservation.getCreatedAt()
                    );
                }
            }
            throw new DatabaseException("Failed to retrieve generated reservation ID");
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save reservation: " + e.getMessage());
        }
    }
    
    /**
     * Updates an existing reservation.
     */
    public void update(Reservation reservation) throws DatabaseException {
        String sql = "UPDATE reservations SET check_out_date = ?, status = ?, total_amount = ? "
                   + "WHERE reservation_id = ?";
        
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, new java.sql.Date(reservation.getCheckOutDate().getTime()));
            pstmt.setString(2, reservation.getStatus());
            pstmt.setDouble(3, reservation.getTotalAmount());
            pstmt.setInt(4, reservation.getReservationId());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update reservation: " + e.getMessage());
        }
    }
    
    /**
     * Deletes a reservation.
     */
    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete reservation: " + e.getMessage());
        }
    }
    
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("reservation_id"));
        reservation.setStatus(rs.getString("status"));
        reservation.setCheckInDate(new Date(rs.getDate("check_in_date").getTime()));
        reservation.setCheckOutDate(new Date(rs.getDate("check_out_date").getTime()));
        reservation.setTotalAmount(rs.getDouble("total_amount"));
        return reservation;
    }
}
```

### 12.3 Observer Pattern (Dashboard Updates)

```java
// Interface for observers
public interface DashboardObserver {
    void onReservationCreated(Reservation reservation);
    void onCheckIn(Reservation reservation);
    void onCheckOut(Reservation reservation);
}

// Dashboard panel implementing observer
public class DashboardPanel extends JPanel implements DashboardObserver {
    
    private JLabel occupancyLabel;
    private JLabel revenueLabel;
    
    @Override
    public void onReservationCreated(Reservation reservation) {
        updateOccupancyRate();
        updateRevenueDisplay();
    }
    
    @Override
    public void onCheckIn(Reservation reservation) {
        updateOccupancyRate();
        updateTodaysCheckInList();
    }
    
    @Override
    public void onCheckOut(Reservation reservation) {
        updateOccupancyRate();
        updateRevenueDisplay();
        updateTodaysCheckOutList();
    }
    
    private void updateOccupancyRate() {
        // Recalculate occupancy
        occupancyLabel.setText("Occupancy: 75%");
    }
}

// Controller notifies observers
public class ReservationController {
    
    private List<DashboardObserver> observers = new ArrayList<>();
    private ReservationDAO reservationDAO;
    
    public void addObserver(DashboardObserver observer) {
        observers.add(observer);
    }
    
    public void createReservation(Reservation reservation) throws ReservationException {
        reservationDAO.save(reservation);
        
        // Notify all observers
        for (DashboardObserver observer : observers) {
            observer.onReservationCreated(reservation);
        }
    }
}
```

### 12.4 Strategy Pattern (Pricing)

```java
// Interface for pricing strategies
public interface PricingStrategy {
    double calculatePrice(Room room, int numberOfNights);
}

// Concrete strategies
public class NormalPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        return room.getBasePrice() * numberOfNights;
    }
}

public class SeasonalPricingStrategy implements PricingStrategy {
    private double seasonalMultiplier;
    
    public SeasonalPricingStrategy(double multiplier) {
        this.seasonalMultiplier = multiplier;
    }
    
    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        return room.getBasePrice() * numberOfNights * seasonalMultiplier;
    }
}

public class CorporatePricingStrategy implements PricingStrategy {
    private double discountRate;
    
    public CorporatePricingStrategy(double discountRate) {
        this.discountRate = discountRate;
    }
    
    @Override
    public double calculatePrice(Room room, int numberOfNights) {
        double baseAmount = room.getBasePrice() * numberOfNights;
        return baseAmount * (1 - discountRate);
    }
}

// Usage in controller
public class ReservationController {
    
    public double calculateReservationCost(Reservation reservation, 
            PricingStrategy strategy) {
        int nights = calculateNights(reservation.getCheckInDate(), 
                reservation.getCheckOutDate());
        return strategy.calculatePrice(reservation.getRoom(), nights);
    }
}
```

---

## 13. UI/Component Guidelines

### 13.1 FlatLaf Theme Application

```java
// com/hotelms/HotelManagementApp.java
public class HotelManagementApp {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Apply macOS dark theme
                UIManager.setLookAndFeel(
                    new com.formdev.flatlaf.FlatDarkLaf()
                );
                
                // Create and show main window
                MainWindow mainWindow = new MainWindow();
                mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainWindow.setLocationRelativeTo(null);
                mainWindow.setVisible(true);
                
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        });
    }
}
```

### 13.2 Component Creation

```java
// ✓ CORRECT: Consistent UI component creation
public class ReservationPanel extends JPanel {
    
    private JTable reservationTable;
    private JButton createButton;
    private JButton editButton;
    private JButton checkInButton;
    
    public ReservationPanel() {
        initComponents();
        setupTable();
        setupListeners();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        JLabel titleLabel = new JLabel("Reservations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel);
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        reservationTable = new JTable();
        panel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        createButton = new JButton("Create");
        editButton = new JButton("Edit");
        checkInButton = new JButton("Check In");
        
        panel.add(createButton);
        panel.add(editButton);
        panel.add(checkInButton);
        
        return panel;
    }
}

// ✗ INCORRECT: Mixed UI component creation
public class ReservationPanel extends JPanel {
    
    public ReservationPanel() {
        JButton btn = new JButton("Create");  // Component created in constructor
        btn.setBounds(10, 10, 100, 30);       // Absolute positioning
        this.add(btn);
        
        JLabel lbl = new JLabel("Reservations");
        lbl.setBounds(120, 10, 200, 20);
        this.add(lbl);
        
        // Hard to maintain, no layout manager
    }
}
```

### 13.3 Dialog Implementation

```java
// com/hotelms/view/dialogs/ReservationDialog.java
public class ReservationDialog extends JDialog {
    
    private JComboBox<Guest> guestCombo;
    private JComboBox<Room> roomCombo;
    private JSpinner checkInSpinner;
    private JSpinner checkOutSpinner;
    private JButton createButton;
    private JButton cancelButton;
    
    private boolean confirmed = false;
    
    public ReservationDialog(Frame parent, List<Guest> guests, List<Room> rooms) {
        super(parent, "Create Reservation", true);
        initComponents(guests, rooms);
        setupLayout();
        setupListeners();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents(List<Guest> guests, List<Room> rooms) {
        guestCombo = new JComboBox<>(guests.toArray(new Guest[0]));
        roomCombo = new JComboBox<>(rooms.toArray(new Room[0]));
        
        checkInSpinner = new JSpinner(new SpinnerDateModel());
        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        
        createButton = new JButton("Create");
        cancelButton = new JButton("Cancel");
    }
    
    private void setupLayout() {
        JPanel contentPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        contentPanel.add(new JLabel("Guest:"));
        contentPanel.add(guestCombo);
        
        contentPanel.add(new JLabel("Room:"));
        contentPanel.add(roomCombo);
        
        contentPanel.add(new JLabel("Check-in:"));
        contentPanel.add(checkInSpinner);
        
        contentPanel.add(new JLabel("Check-out:"));
        contentPanel.add(checkOutSpinner);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setSize(400, 300);
    }
    
    private void setupListeners() {
        createButton.addActionListener(e -> {
            try {
                validateInputs();
                confirmed = true;
                dispose();
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
    }
    
    private void validateInputs() throws ValidationException {
        if (guestCombo.getSelectedIndex() == -1) {
            throw new ValidationException("Please select a guest");
        }
        if (roomCombo.getSelectedIndex() == -1) {
            throw new ValidationException("Please select a room");
        }
        
        Date checkIn = (Date) checkInSpinner.getValue();
        Date checkOut = (Date) checkOutSpinner.getValue();
        
        if (!checkOut.after(checkIn)) {
            throw new ValidationException("Check-out date must be after check-in");
        }
    }
    
    public boolean isConfirmed() {
        return confirmed;
    }
    
    public Guest getSelectedGuest() {
        return (Guest) guestCombo.getSelectedItem();
    }
    
    public Room getSelectedRoom() {
        return (Room) roomCombo.getSelectedItem();
    }
}
```

---

## 14. Git Workflow

### 14.1 Commit Message Format

```
<type>: <short summary> (50 chars max)

<detailed explanation (if needed, wrap at 72 chars)>

<optional footer>
```

**Types:** `feat`, `fix`, `refactor`, `docs`, `test`, `chore`

### 14.2 Commit Examples

```bash
# ✓ CORRECT: Clear, concise commits
git commit -m "feat: Add guest registration module with validation"

git commit -m "fix: Resolve SQL injection vulnerability in GuestDAO"

git commit -m "refactor: Extract pricing logic into PricingStrategy pattern"

git commit -m "feat: Implement Jasper Reports for occupancy analysis

- Create report template for room occupancy data
- Add date range filtering capability
- Include revenue breakdown by room type"

# ✗ INCORRECT: Vague or too lengthy
git commit -m "Update code"

git commit -m "Fixed some bugs and added features and improved performance and updated UI"

git commit -m "wip"
```

### 14.3 Branch Strategy

```
main
  └── develop
        └── feature/guest-registration
        └── feature/reports
        └── bugfix/reservation-validation
```

---

## 15. Testing & Validation

### 15.1 Input Validation Checklist

- [ ] Email format validated
- [ ] Phone number format validated
- [ ] Date ranges validated (checkout > checkin)
- [ ] Room capacity vs guest count validated
- [ ] Numeric fields validated for negative values
- [ ] Mandatory fields checked
- [ ] String length limits enforced
- [ ] Special characters sanitized

### 15.2 Database Operations Checklist

- [ ] All SQL queries use PreparedStatements
- [ ] NULL checks for retrieved values
- [ ] Foreign key constraints defined
- [ ] Unique constraints on unique columns
- [ ] Default values set appropriately
- [ ] Timestamps tracked (created_at, updated_at)
- [ ] Indexes created on frequently queried columns

### 15.3 Security Checklist

- [ ] Passwords hashed with bcrypt
- [ ] No hardcoded sensitive data
- [ ] SQL injection protection verified
- [ ] Input validation at UI layer
- [ ] Exception messages don't expose internals
- [ ] Database credentials in Constants class
- [ ] Connection retry logic working

### 15.4 Code Quality Checklist

- [ ] No warnings in IDE (NetBeans)
- [ ] Consistent naming conventions applied
- [ ] Comments explain "why", not "what"
- [ ] JavaDoc on public methods
- [ ] No dead code
- [ ] Proper exception handling
- [ ] Design patterns applied appropriately

---

## Appendix A: Quick Reference

### Entity Relationship Diagram (Simplified)
```
guests (1) ──── (N) reservations (1) ──── (1) rooms
   │                    │                      
   │                    └─────── (1) billing
   │                    │
   │                    └─────── (N) service_bookings (N) ──── services
   │
   └─────────────────────────────── (N) room_assignments (N) ──── staff
```

### Common Package Imports

```java
// Database
import java.sql.*;

// Collections
import java.util.*;

// Swing UI
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Custom packages
import com.hotelms.model.*;
import com.hotelms.dao.*;
import com.hotelms.controller.*;
import com.hotelms.exception.*;
import com.hotelms.util.*;
import com.hotelms.config.*;
import com.hotelms.database.*;
```

### Useful Constants Pattern

```java
public class Constants {
    // Database
    public static final String DB_HOST = "localhost";
    public static final int DB_PORT = 3306;
    
    // UI
    public static final int WINDOW_WIDTH = 1400;
    public static final int WINDOW_HEIGHT = 900;
    
    // Validation
    public static final int MAX_GUESTS = 6;
    public static final int MAX_EMAIL_LENGTH = 150;
    
    // Never instantiate
    private Constants() {
        throw new UnsupportedOperationException();
    }
}
```

---

## Appendix B: Code Review Checklist

Before committing code, verify:

- [ ] Follows naming conventions (camelCase for Java, snake_case for DB)
- [ ] Uses PreparedStatements for all SQL queries
- [ ] Includes appropriate error handling
- [ ] Has meaningful variable and method names
- [ ] Follows MVC architecture
- [ ] No hardcoded values (uses Constants)
- [ ] Comments explain complex logic
- [ ] JavaDoc on public methods
- [ ] Single Responsibility Principle maintained
- [ ] No code duplication
- [ ] Proper exception handling (not just try-catch)
- [ ] Input validation where required
- [ ] Database operations in DAO layer
- [ ] UI logic in View/Controller layers
- [ ] Business logic in appropriate layer

---

**End of Document**

Version 1.0 - June 2026
