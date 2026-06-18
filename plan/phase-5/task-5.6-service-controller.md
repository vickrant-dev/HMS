# Task 5.6 — ServiceController.java

**Package:** `hms.controller`
**File:** `src/hms/controller/ServiceController.java`

## Description
Orchestrates service catalog CRUD and service booking management linked to reservations.

## Methods

| # | Method | Returns | Description |
|---|--------|---------|-------------|
| 1 | `createService(Service)` | `Service` | Validate, check name uniqueness, DAO.save |
| 2 | `getServiceById(int)` | `Service` | DAO.getById |
| 3 | `getAllServices()` | `List<Service>` | DAO.getAll |
| 4 | `updateService(Service)` | `void` | Validate, check uniqueness, DAO.update |
| 5 | `deleteService(int)` | `void` | DAO.delete |
| 6 | `filterByType(String)` | `List<Service>` | DAO.filterByType |
| 7 | `filterByAvailability(boolean)` | `List<Service>` | DAO.filterByAvailability |
| 8 | `toggleAvailability(int)` | `void` | Fetch, toggle isAvailable, reconstruct, DAO.update |
| 9 | `createServiceBooking(Reservation, Service, int)` | `ServiceBooking` | Validate, calculate totalPrice, DAO.save |
| 10 | `getBookingById(int)` | `ServiceBooking` | DAO.getById |
| 11 | `getAllBookings()` | `List<ServiceBooking>` | DAO.getAll |
| 12 | `updateBooking(ServiceBooking)` | `void` | DAO.update |
| 13 | `cancelBooking(int)` | `void` | Fetch, reconstruct with CANCELLED, DAO.update |
| 14 | `getBookingsByReservation(int)` | `List<ServiceBooking>` | DAO.getByReservationId |

## Dependencies
- ServiceDAO (Phase 3.6)
- ServiceBookingDAO (Phase 3.7)
- ValidationUtil (Phase 4.1)
- Constants (Phase 0.5)

## Checklist
- [x] Create checklist file
- [x] Write `ServiceController.java`
- [x] Verify Clean & Build
