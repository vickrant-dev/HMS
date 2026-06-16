package hms.model;

import java.time.LocalDateTime;

public final class Billing {

    private final int billingId;
    private final Reservation reservation;
    private final Guest guest;
    private final double totalAmount;
    private final double amountPaid;
    private final String paymentStatus;
    private final LocalDateTime paymentDate;
    private final String paymentMethod;
    private final LocalDateTime createdAt;

    public Billing(Reservation reservation, Guest guest, double totalAmount,
                   double amountPaid, String paymentStatus,
                   LocalDateTime paymentDate, String paymentMethod) {
        this.billingId = 0;
        this.reservation = reservation;
        this.guest = guest;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.createdAt = LocalDateTime.now();
    }

    public Billing(int billingId, Reservation reservation, Guest guest,
                   double totalAmount, double amountPaid,
                   String paymentStatus, LocalDateTime paymentDate,
                   String paymentMethod, LocalDateTime createdAt) {
        this.billingId = billingId;
        this.reservation = reservation;
        this.guest = guest;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
    }

    public int getBillingId() {
        return billingId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public Guest getGuest() {
        return guest;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getReservationId() {
        return reservation.getReservationId();
    }

    public int getGuestId() {
        return guest.getGuestId();
    }
}
