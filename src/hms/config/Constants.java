package hms.config;

public final class Constants {

    private Constants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Database Configuration
    public static final String DB_HOST = "localhost";
    public static final int DB_PORT = 3306;
    public static final String DB_NAME = "hotel_management_system";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";

    // Connection Settings
    public static final int DB_CONNECTION_TIMEOUT = 30;
    public static final int DB_MAX_RETRIES = 3;
    public static final int DB_RETRY_DELAY_MS = 2000;

    // UI Constants
    public static final int WINDOW_WIDTH = 1400;
    public static final int WINDOW_HEIGHT = 900;
    public static final String APP_TITLE = "Hotel Management System";
    public static final String THEME = "dark"; // "dark" or "light"

    // Room Types
    public static final String ROOM_TYPE_SINGLE = "Single";
    public static final String ROOM_TYPE_DOUBLE = "Double";
    public static final String ROOM_TYPE_SUITE = "Suite";
    public static final String ROOM_TYPE_DELUXE = "Deluxe";

    // Room Status
    public static final String ROOM_STATUS_AVAILABLE = "available";
    public static final String ROOM_STATUS_OCCUPIED = "occupied";
    public static final String ROOM_STATUS_MAINTENANCE = "maintenance";
    public static final String ROOM_STATUS_RESERVED = "reserved";

    // Reservation Status
    public static final String RES_STATUS_PENDING = "pending";
    public static final String RES_STATUS_CONFIRMED = "confirmed";
    public static final String RES_STATUS_CHECKED_IN = "checked_in";
    public static final String RES_STATUS_CHECKED_OUT = "checked_out";
    public static final String RES_STATUS_CANCELLED = "cancelled";

    // Billing Payment Status
    public static final String PAYMENT_PENDING = "pending";
    public static final String PAYMENT_PARTIAL = "partial";
    public static final String PAYMENT_PAID = "paid";
    public static final String PAYMENT_REFUNDED = "refunded";

    // Staff Status
    public static final String STAFF_ACTIVE = "active";
    public static final String STAFF_INACTIVE = "inactive";
    public static final String STAFF_ON_LEAVE = "on_leave";

    // Service Booking Status
    public static final String SVC_BOOKING_PENDING = "pending";
    public static final String SVC_BOOKING_COMPLETED = "completed";
    public static final String SVC_BOOKING_CANCELLED = "cancelled";

    // Room Assignment Status
    public static final String ASSIGN_PENDING = "pending";
    public static final String ASSIGN_IN_PROGRESS = "in_progress";
    public static final String ASSIGN_COMPLETED = "completed";

    // Reservation Constants
    public static final String RES_ID_PREFIX = "RES";
    public static final int MIN_GUESTS = 1;
    public static final int MAX_GUESTS = 6;

    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_EMAIL_LENGTH = 150;
    public static final int MAX_PHONE_LENGTH = 15;

    // Tax & Pricing
    public static final double DEFAULT_TAX_RATE = 0.10;
    public static final double LATE_CHECKOUT_CHARGE = 5000.0;

    // Service Types
    public static final String SERVICE_TYPE_FOOD = "Food";
    public static final String SERVICE_TYPE_LAUNDRY = "Laundry";
    public static final String SERVICE_TYPE_SPA = "Spa";
    public static final String SERVICE_TYPE_CONFERENCE = "Conference";
    public static final String SERVICE_TYPE_TRANSPORT = "Transport";
    public static final String SERVICE_TYPE_BEVERAGE = "Beverage";

    // Assignment Types
    public static final String ASSIGN_TYPE_CLEANING = "Cleaning";
    public static final String ASSIGN_TYPE_MAINTENANCE = "Maintenance";
    public static final String ASSIGN_TYPE_INSPECTION = "Inspection";

    // Report Paths
    public static final String JASPER_REPORT_PATH = "/hms/reports/";
    public static final String REPORT_OUTPUT_DIR = "reports/";

    // Error Messages
    public static final String ERROR_DB_CONNECTION = "Failed to connect to database. Please ensure XAMPP is running.";
    public static final String ERROR_INVALID_EMAIL = "Invalid email format";
    public static final String ERROR_INVALID_PHONE = "Invalid phone number";
    public static final String ERROR_INVALID_NAME = "Name can only contain letters and spaces";
    public static final String ERROR_ROOM_NOT_AVAILABLE = "Room is not available for the selected dates";
    public static final String ERROR_RESERVATION_NOT_FOUND = "Reservation not found";
    public static final String ERROR_GUEST_NOT_FOUND = "Guest not found";
    public static final String ERROR_INVALID_DATES = "Check-out date must be after check-in date";
}
