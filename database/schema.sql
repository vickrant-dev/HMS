CREATE DATABASE IF NOT EXISTS hotel_management_system;

USE hotel_management_system;

-- Drop tables before creating.
DROP TABLE IF EXISTS room_assignments;
DROP TABLE IF EXISTS service_bookings;
DROP TABLE IF EXISTS billing;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS rooms;
DROP TABLE IF EXISTS guests;

-- 1. guests
CREATE TABLE guests (
    guest_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT,
    id_proof_type VARCHAR(50),
    id_proof_number VARCHAR(50),
    date_of_birth DATE,
    guest_type VARCHAR(50) DEFAULT 'Regular',
    nationality VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. rooms
CREATE TABLE rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    status ENUM('available', 'occupied', 'maintenance', 'reserved') NOT NULL DEFAULT 'available',
    floor INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_rooms_status (status),
    INDEX idx_rooms_type (room_type)
);

-- 3. staff
CREATE TABLE staff (
    staff_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    position VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    salary DECIMAL(12, 2),
    joining_date DATE NOT NULL,
    status ENUM('active', 'inactive', 'on_leave') NOT NULL DEFAULT 'active',
    password_hash VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_staff_position (position),
    INDEX idx_staff_department (department)
);

-- 4. services
CREATE TABLE services (
    service_id INT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(150) NOT NULL,
    service_type VARCHAR(100),
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. reservations
CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    display_id VARCHAR(20),
    guest_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    number_of_guests INT NOT NULL,
    status ENUM('pending', 'confirmed', 'checked_in', 'checked_out', 'cancelled') NOT NULL DEFAULT 'pending',
    total_amount DECIMAL(12, 2),
    created_by_staff_id INT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,

    FOREIGN KEY (guest_id) REFERENCES guests(guest_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (created_by_staff_id) REFERENCES staff(staff_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_reservations_guest_id (guest_id),
    INDEX idx_reservations_room_id (room_id),
    INDEX idx_reservations_check_in (check_in_date),
    INDEX idx_reservations_check_out (check_out_date),
    INDEX idx_reservations_status (status),
    INDEX idx_reservations_created_by (created_by_staff_id),
    INDEX idx_reservations_display_id (display_id)
);

-- 6. billing
CREATE TABLE billing (
    billing_id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT NOT NULL,
    room_charge DECIMAL(12, 2) NOT NULL,
    service_charge DECIMAL(12, 2) DEFAULT 0,
    other_charges DECIMAL(12, 2) DEFAULT 0,
    discount_amount DECIMAL(12, 2) DEFAULT 0,
    late_charge DECIMAL(12, 2) DEFAULT 0,
    tax_amount DECIMAL(12, 2) DEFAULT 0,
    total_bill DECIMAL(12, 2) NOT NULL,
    payment_status ENUM('pending', 'partial', 'paid', 'refunded') NOT NULL DEFAULT 'pending',
    amount_paid DECIMAL(12, 2) DEFAULT 0,
    payment_method VARCHAR(50),
    transaction_id VARCHAR(100),
    payment_notes TEXT,
    payment_date TIMESTAMP NULL,
    notes TEXT,

    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_billing_reservation_id (reservation_id),
    INDEX idx_billing_payment_status (payment_status)
);

-- 7. service_bookings
CREATE TABLE service_bookings (
    service_booking_id INT PRIMARY KEY AUTO_INCREMENT,
    reservation_id INT NOT NULL,
    service_id INT NOT NULL,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INT DEFAULT 1,
    total_price DECIMAL(12, 2),
    status ENUM('pending', 'completed', 'cancelled') NOT NULL DEFAULT 'pending',

    FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_service_bookings_reservation_id (reservation_id),
    INDEX idx_service_bookings_service_id (service_id)
);

-- 8. room_assignments
CREATE TABLE room_assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    room_id INT NOT NULL,
    staff_id INT NOT NULL,
    assignment_date DATE NOT NULL,
    assignment_type VARCHAR(100),
    status ENUM('pending', 'in_progress', 'completed') NOT NULL DEFAULT 'pending',
    notes TEXT,

    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_assignments_room_id (room_id),
    INDEX idx_assignments_staff_id (staff_id),
    INDEX idx_assignments_date (assignment_date)
);
