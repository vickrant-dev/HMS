package hms.model;

import java.time.LocalDateTime;

/**
 * Represents a booking of a service within a reservation.
 */
public final class ServiceBooking {

    private final int serviceBookingId;
    private final Reservation reservation;
    private final Service service;
    private final LocalDateTime bookingDate;
    private final int quantity;
    private final double totalPrice;
    private final String status;
    private final String notes;

    /**
     * Creates a new service booking without an ID (for new records).
     *
     * @param reservation The associated reservation
     * @param service     The booked service
     * @param quantity    The quantity ordered
     * @param totalPrice  The total price for this service booking
     * @param status      The booking status (e.g., "pending", "completed")
     * @param notes       Additional notes for this service booking
     */
    public ServiceBooking(Reservation reservation, Service service,
                          int quantity, double totalPrice, String status,
                          String notes) {
        this.serviceBookingId = 0;
        this.reservation = reservation;
        this.service = service;
        this.bookingDate = LocalDateTime.now();
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.notes = notes;
    }

    /**
     * Creates a service booking with all fields (for database reconstruction).
     *
     * @param serviceBookingId The service booking's unique ID
     * @param reservation      The associated reservation
     * @param service          The booked service
     * @param bookingDate      The date when the booking was made
     * @param quantity         The quantity ordered
     * @param totalPrice       The total price for this service booking
     * @param status           The booking status
     * @param notes            Additional notes for this service booking
     */
    public ServiceBooking(int serviceBookingId, Reservation reservation,
                          Service service, LocalDateTime bookingDate,
                          int quantity, double totalPrice, String status,
                          String notes) {
        this.serviceBookingId = serviceBookingId;
        this.reservation = reservation;
        this.service = service;
        this.bookingDate = bookingDate;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
        this.notes = notes;
    }

    /** Returns the service booking's unique ID. */
    public int getServiceBookingId() {
        return serviceBookingId;
    }

    /** Returns the associated reservation. */
    public Reservation getReservation() {
        return reservation;
    }

    /** Returns the booked service. */
    public Service getService() {
        return service;
    }

    /** Returns the date when the booking was made. */
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    /** Returns the quantity ordered. */
    public int getQuantity() {
        return quantity;
    }

    /** Returns the total price for this service booking. */
    public double getTotalPrice() {
        return totalPrice;
    }

    /** Returns the booking status. */
    public String getStatus() {
        return status;
    }

    /** Returns additional notes for this service booking. */
    public String getNotes() {
        return notes;
    }

    /** Returns the associated reservation's ID. */
    public int getReservationId() {
        return reservation.getReservationId();
    }

    /** Returns the booked service's ID. */
    public int getServiceId() {
        return service.getServiceId();
    }
}
