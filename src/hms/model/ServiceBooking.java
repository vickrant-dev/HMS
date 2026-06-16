package hms.model;

import java.time.LocalDateTime;

public final class ServiceBooking {

    private final int serviceBookingId;
    private final Reservation reservation;
    private final Service service;
    private final LocalDateTime bookingDate;
    private final int quantity;
    private final Double totalPrice;
    private final String status;

    public ServiceBooking(Reservation reservation, Service service,
                          int quantity, Double totalPrice, String status) {
        this.serviceBookingId = 0;
        this.reservation = reservation;
        this.service = service;
        this.bookingDate = LocalDateTime.now();
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public ServiceBooking(int serviceBookingId, Reservation reservation,
                          Service service, LocalDateTime bookingDate,
                          int quantity, Double totalPrice, String status) {
        this.serviceBookingId = serviceBookingId;
        this.reservation = reservation;
        this.service = service;
        this.bookingDate = bookingDate;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getServiceBookingId() {
        return serviceBookingId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Service getService() {
        return service;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public int getReservationId() {
        return reservation.getReservationId();
    }

    public int getServiceId() {
        return service.getServiceId();
    }
}
