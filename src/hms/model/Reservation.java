package hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a room reservation.
 */
public final class Reservation {

    private final int reservationId;
    private final String displayId;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final LocalDateTime bookingDate;
    private final int numberOfGuests;
    private final String status;
    private final double totalAmount;
    private final Integer createdByStaffId;
    private final LocalDateTime createdAt;

    /**
     * Creates a new reservation without an ID (for new records).
     *
     * @param guest            The guest making the reservation
     * @param room             The reserved room
     * @param checkInDate      The check-in date
     * @param checkOutDate     The check-out date
     * @param numberOfGuests   The number of guests
     * @param status           The reservation status (e.g., "confirmed", "checked-in")
     * @param totalAmount      The total amount charged
     * @param displayId        The display ID in format RES-YYYYMMDD-XXXXX
     * @param createdByStaffId The staff ID who created the reservation (nullable)
     */
    public Reservation(Guest guest, Room room, LocalDate checkInDate,
                       LocalDate checkOutDate, int numberOfGuests,
                       String status, double totalAmount,
                       String displayId, Integer createdByStaffId) {
        this.reservationId = 0;
        this.displayId = displayId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingDate = LocalDateTime.now();
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdByStaffId = createdByStaffId;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Creates a reservation with all fields (for database reconstruction).
     *
     * @param reservationId    The reservation's unique ID
     * @param displayId        The display ID in format RES-YYYYMMDD-XXXXX
     * @param guest            The guest making the reservation
     * @param room             The reserved room
     * @param checkInDate      The check-in date
     * @param checkOutDate     The check-out date
     * @param bookingDate      The date when the booking was made
     * @param numberOfGuests   The number of guests
     * @param status           The reservation status
     * @param totalAmount      The total amount charged
     * @param createdByStaffId The staff ID who created the reservation (nullable)
     * @param createdAt        The timestamp when the record was created
     */
    public Reservation(int reservationId, String displayId, Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate,
                       LocalDateTime bookingDate, int numberOfGuests,
                       String status, double totalAmount,
                       Integer createdByStaffId, LocalDateTime createdAt) {
        this.reservationId = reservationId;
        this.displayId = displayId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingDate = bookingDate;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdByStaffId = createdByStaffId;
        this.createdAt = createdAt;
    }

    /** Returns the reservation's unique ID. */
    public int getReservationId() {
        return reservationId;
    }

    /** Returns the display ID in format RES-YYYYMMDD-XXXXX. */
    public String getDisplayId() {
        return displayId;
    }

    /** Returns the guest who made the reservation. */
    public Guest getGuest() {
        return guest;
    }

    /** Returns the reserved room. */
    public Room getRoom() {
        return room;
    }

    /** Returns the check-in date. */
    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    /** Returns the check-out date. */
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    /** Returns the date when the booking was made. */
    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    /** Returns the number of guests. */
    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    /** Returns the reservation status. */
    public String getStatus() {
        return status;
    }

    /** Returns the total amount charged. */
    public double getTotalAmount() {
        return totalAmount;
    }

    /** Returns the staff ID who created the reservation (nullable). */
    public Integer getCreatedByStaffId() {
        return createdByStaffId;
    }

    /** Returns the timestamp when the record was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /** Returns the guest's ID. */
    public int getGuestId() {
        return guest.getGuestId();
    }

    /** Returns the reserved room's ID. */
    public int getRoomId() {
        return room.getRoomId();
    }
}
