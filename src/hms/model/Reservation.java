package hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Reservation {

    private final int reservationId;
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

    public Reservation(Guest guest, Room room, LocalDate checkInDate,
                       LocalDate checkOutDate, int numberOfGuests,
                       String status, double totalAmount,
                       Integer createdByStaffId) {
        this.reservationId = 0;
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

    public Reservation(int reservationId, Guest guest, Room room,
                       LocalDate checkInDate, LocalDate checkOutDate,
                       LocalDateTime bookingDate, int numberOfGuests,
                       String status, double totalAmount,
                       Integer createdByStaffId, LocalDateTime createdAt) {
        this.reservationId = reservationId;
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

    public int getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public Integer getCreatedByStaffId() {
        return createdByStaffId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getGuestId() {
        return guest.getGuestId();
    }

    public int getRoomId() {
        return room.getRoomId();
    }
}
