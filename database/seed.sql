USE hotel_management_system;

-- ============================================
-- 1. guests (12)
-- ============================================
INSERT INTO guests (first_name, last_name, email, phone, address, id_proof_type, id_proof_number, date_of_birth) VALUES
('John', 'Smith', 'john.smith@email.com', '0771234567', '12 Kings Road, Colombo 01', 'Passport', 'P1234567', '1985-03-15'),
('Sarah', 'Johnson', 'sarah.j@email.com', '0772345678', '45 Park Street, Kandy', 'Driving License', 'DL876543', '1990-07-22'),
('Michael', 'Chen', 'mchen@email.com', '0773456789', '78 Galle Road, Colombo 03', 'Passport', 'P2345678', '1982-11-08'),
('Emily', 'Davis', 'emily.davis@email.com', '0774567890', '23 Temple Road, Nugegoda', 'Driving License', 'DL123456', '1995-05-30'),
('James', 'Wilson', 'jwilson@email.com', '0775678901', '56 Hill Street, Colombo 07', NULL, NULL, '1978-09-12'),
('Emma', 'Brown', 'emma.brown@email.com', '0776789012', '90 Lake Drive, Negombo', 'Passport', 'P3456789', '1988-01-25'),
('David', 'Lee', 'david.lee@email.com', '0777890123', '34 Ocean View, Galle', 'Driving License', 'DL654321', '1992-06-18'),
('Sophia', 'Garcia', 'sophia.g@email.com', '0778901234', '67 Green Lane, Colombo 05', 'Passport', 'P4567890', '1987-04-05'),
('Robert', 'Taylor', 'rtaylor@email.com', '0779012345', '12 River Road, Kandy', NULL, NULL, '1965-12-20'),
('Olivia', 'Martinez', 'olivia.m@email.com', '0770123456', '89 Sunset Blvd, Colombo 04', 'Driving License', 'DL345678', '1993-08-14'),
('William', 'Anderson', 'wanderson@email.com', '0771122334', '55 Mountain View, Nuwara Eliya', 'Passport', 'P5678901', '1980-02-28'),
('Isabella', 'Thomas', 'isabella.t@email.com', '0772233445', '22 Garden Road, Colombo 08', 'Driving License', 'DL234567', '1996-10-10');

-- ============================================
-- 2. rooms (24)
-- ============================================
INSERT INTO rooms (room_number, room_type, capacity, base_price, status, floor) VALUES
('101', 'Single', 1, 100.00, 'available', 1),
('102', 'Single', 1, 105.00, 'available', 1),
('103', 'Single', 1, 110.00, 'maintenance', 1),
('104', 'Single', 1, 100.00, 'available', 1),
('105', 'Single', 1, 115.00, 'available', 1),
('106', 'Single', 1, 130.00, 'occupied', 1),
('201', 'Double', 2, 160.00, 'available', 2),
('202', 'Double', 2, 170.00, 'occupied', 2),
('203', 'Double', 2, 175.00, 'available', 2),
('204', 'Double', 2, 180.00, 'occupied', 2),
('205', 'Double', 2, 190.00, 'reserved', 2),
('206', 'Double', 2, 200.00, 'available', 2),
('207', 'Double', 2, 210.00, 'available', 2),
('208', 'Double', 2, 195.00, 'occupied', 2),
('301', 'Suite', 3, 320.00, 'available', 3),
('302', 'Suite', 3, 350.00, 'occupied', 3),
('303', 'Suite', 3, 380.00, 'available', 3),
('304', 'Suite', 4, 400.00, 'occupied', 3),
('305', 'Suite', 4, 420.00, 'reserved', 3),
('306', 'Suite', 4, 450.00, 'available', 3),
('401', 'Deluxe', 3, 220.00, 'occupied', 4),
('402', 'Deluxe', 3, 250.00, 'available', 4),
('501', 'Deluxe', 4, 280.00, 'available', 5),
('502', 'Deluxe', 4, 300.00, 'reserved', 5);

-- ============================================
-- 3. staff (6)
-- ============================================
INSERT INTO staff (first_name, last_name, email, phone, position, department, salary, joining_date, status) VALUES
('Alice', 'Johnson', 'alice.johnson@hms.com', '0779988776', 'Manager', 'Administration', 5500.00, '2023-01-15', 'active'),
('Bob', 'Williams', 'bob.williams@hms.com', '0778877665', 'Receptionist', 'Front Desk', 2800.00, '2023-03-01', 'active'),
('Carol', 'Davis', 'carol.davis@hms.com', '0777766554', 'Receptionist', 'Front Desk', 2600.00, '2023-06-12', 'active'),
('Daniel', 'Brown', 'daniel.brown@hms.com', '0776655443', 'Housekeeper', 'Housekeeping', 2200.00, '2024-02-20', 'active'),
('Eva', 'Martinez', 'eva.martinez@hms.com', '0775544332', 'Housekeeper', 'Housekeeping', 2100.00, '2024-04-10', 'on_leave'),
('Frank', 'Wilson', 'frank.wilson@hms.com', '0774433221', 'Maintenance', 'Maintenance', 2400.00, '2023-09-05', 'active');

-- ============================================
-- 4. services (8)
-- ============================================
INSERT INTO services (service_name, service_type, price, description, is_available) VALUES
('Breakfast', 'Food', 15.00, 'Continental breakfast served 7-10 AM', TRUE),
('Lunch', 'Food', 25.00, 'Three-course lunch served 12-2 PM', TRUE),
('Dinner', 'Food', 40.00, 'Four-course dinner served 7-10 PM', TRUE),
('Laundry - Wash', 'Laundry', 10.00, 'Standard laundry wash and fold service', TRUE),
('Laundry - Dry Clean', 'Laundry', 20.00, 'Professional dry cleaning service', TRUE),
('Spa - Massage', 'Spa', 80.00, 'Full body massage (60 min)', TRUE),
('Spa - Sauna', 'Spa', 50.00, 'Sauna access per session (45 min)', TRUE),
('Conference Room', 'Conference', 200.00, 'Conference room rental per hour', FALSE);

-- ============================================
-- 5. reservations (10)
-- ============================================
-- Guest-reservation mapping:
--   guest 1 (John Smith)    → res 1  & 6
--   guest 2 (Sarah Johnson) → res 2  & 7
--   guest 3 (Michael Chen)  → res 3
--   guest 4 (Emily Davis)   → res 4
--   guest 5 (James Wilson)  → res 5
--   guest 6 (Emma Brown)    → res 8
--   guest 7 (David Lee)     → res 9
--   guest 8 (Sophia Garcia) → res 10

INSERT INTO reservations (guest_id, room_id, check_in_date, check_out_date, number_of_guests, status, total_amount, display_id) VALUES
-- 1: Checked out (June 10-12, Room 106 - Single)
(1, 6, '2026-06-10', '2026-06-12', 1, 'checked_out', 260.00, 'RES-20260610-00001'),
-- 2: Checked out (June 12-15, Room 202 - Double)
(2, 7, '2026-06-12', '2026-06-15', 2, 'checked_out', 510.00, 'RES-20260612-00002'),
-- 3: Checked out (June 14-16, Room 302 - Suite)
(3, 16, '2026-06-14', '2026-06-16', 2, 'checked_out', 700.00, 'RES-20260614-00003'),
-- 4: Checked in (June 15-18, Room 204 - Double)
(4, 9, '2026-06-15', '2026-06-18', 2, 'checked_in', 540.00, 'RES-20260615-00004'),
-- 5: Checked in (June 16-19, Room 304 - Suite)
(5, 18, '2026-06-16', '2026-06-19', 3, 'checked_in', 1200.00, 'RES-20260616-00005'),
-- 6: Checked in (June 16-20, Room 401 - Deluxe)
(1, 21, '2026-06-16', '2026-06-20', 2, 'checked_in', 880.00, 'RES-20260616-00006'),
-- 7: Confirmed (June 20-23, Room 205 - Double)
(2, 10, '2026-06-20', '2026-06-23', 2, 'confirmed', 570.00, 'RES-20260620-00007'),
-- 8: Confirmed (June 22-26, Room 305 - Suite)
(6, 19, '2026-06-22', '2026-06-26', 3, 'confirmed', 1680.00, 'RES-20260622-00008'),
-- 9: Pending (July 01-03, Room 502 - Deluxe)
(7, 24, '2026-07-01', '2026-07-03', 2, 'pending', 600.00, 'RES-20260701-00009'),
-- 10: Cancelled (was June 05-07, Room 208 - Double)
(8, 15, '2026-06-05', '2026-06-07', 1, 'cancelled', 390.00, 'RES-20260605-00010');

-- ============================================
-- 6. billing (3) — only for checked_out reservations
-- ============================================
-- Res 1: John Smith — 2 nights Single (Room 106, $130/night)
--   Room: 130 * 2 = 260.00
--   Services: none
--   Tax: 260 * 0.10 = 26.00
--   Total: 260.00 + 26.00 = 286.00
INSERT INTO billing (reservation_id, room_charge, service_charge, other_charges, tax_amount, total_bill, payment_status, payment_date, notes)
VALUES (1, 260.00, 0.00, 0.00, 26.00, 286.00, 'paid', '2026-06-12 10:30:00', 'Paid via credit card');

-- Res 2: Sarah Johnson — 3 nights Double (Room 202, $170/night)
--   Room: 170 * 3 = 510.00
--   Services: Breakfast * 3 = 45.00
--   Tax: (510 + 45) * 0.10 = 55.50
--   Total: 510.00 + 45.00 + 55.50 = 610.50
INSERT INTO billing (reservation_id, room_charge, service_charge, other_charges, tax_amount, total_bill, payment_status, payment_date, notes)
VALUES (2, 510.00, 45.00, 0.00, 55.50, 610.50, 'paid', '2026-06-15 09:15:00', 'Paid via bank transfer');

-- Res 3: Michael Chen — 2 nights Suite (Room 302, $350/night)
--   Room: 350 * 2 = 700.00
--   Services: Dinner * 2 = 80.00
--   Other: Late checkout charge = 50.00
--   Tax: (700 + 80 + 50) * 0.10 = 83.00
--   Total: 700.00 + 80.00 + 50.00 + 83.00 = 913.00
INSERT INTO billing (reservation_id, room_charge, service_charge, other_charges, tax_amount, total_bill, payment_status, payment_date, notes)
VALUES (3, 700.00, 80.00, 50.00, 83.00, 913.00, 'partial', '2026-06-16 11:45:00', 'Partial payment of 500.00 received, balance pending');

-- ============================================
-- 7. service_bookings (5)
-- ============================================
-- Res 2 (Sarah Johnson, checked_out) — Breakfast x3
INSERT INTO service_bookings (reservation_id, service_id, quantity, total_price, status)
VALUES (2, 1, 3, 45.00, 'completed');

-- Res 3 (Michael Chen, checked_out) — Dinner x2
INSERT INTO service_bookings (reservation_id, service_id, quantity, total_price, status)
VALUES (3, 3, 2, 80.00, 'completed');

-- Res 4 (Emily Davis, checked_in) — Breakfast x3, Laundry x1, Spa Massage x1
INSERT INTO service_bookings (reservation_id, service_id, quantity, total_price, status)
VALUES (4, 1, 3, 45.00, 'pending'),
       (4, 4, 1, 10.00, 'pending'),
       (4, 6, 1, 80.00, 'pending');

-- Res 5 (James Wilson, checked_in) — Dinner x2
INSERT INTO service_bookings (reservation_id, service_id, quantity, total_price, status)
VALUES (5, 3, 2, 80.00, 'pending');

-- ============================================
-- 8. room_assignments (4)
-- ============================================
-- Daniel Brown — Room 103 maintenance cleaning, Room 101 regular cleaning
INSERT INTO room_assignments (room_id, staff_id, assignment_date, assignment_type, status, notes)
VALUES (3, 4, '2026-06-16', 'Cleaning', 'in_progress', 'Post-maintenance deep clean required'),
       (1, 4, '2026-06-15', 'Cleaning', 'completed', 'Daily cleaning completed');

-- Frank Wilson — Room 302 AC maintenance, Room 103 general maintenance
INSERT INTO room_assignments (room_id, staff_id, assignment_date, assignment_type, status, notes)
VALUES (16, 6, '2026-06-16', 'Maintenance', 'in_progress', 'AC unit needs servicing'),
       (3, 6, '2026-06-14', 'Maintenance', 'completed', 'Light bulb replacement and plumbing check');
