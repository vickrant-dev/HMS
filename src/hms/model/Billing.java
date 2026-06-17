package hms.model;

import java.time.LocalDateTime;

public final class Billing {

    private final int billingId;
    private final Reservation reservation;
    private final double roomCharge;
    private final double serviceCharge;
    private final double otherCharges;
    private final double taxAmount;
    private final double totalBill;
    private final String paymentStatus;
    private final LocalDateTime paymentDate;
    private final String notes;

    public Billing(Reservation reservation, double roomCharge,
                   double serviceCharge, double otherCharges,
                   double taxAmount, double totalBill,
                   String paymentStatus, LocalDateTime paymentDate,
                   String notes) {
        this.billingId = 0;
        this.reservation = reservation;
        this.roomCharge = roomCharge;
        this.serviceCharge = serviceCharge;
        this.otherCharges = otherCharges;
        this.taxAmount = taxAmount;
        this.totalBill = totalBill;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.notes = notes;
    }

    public Billing(int billingId, Reservation reservation, double roomCharge,
                   double serviceCharge, double otherCharges,
                   double taxAmount, double totalBill,
                   String paymentStatus, LocalDateTime paymentDate,
                   String notes) {
        this.billingId = billingId;
        this.reservation = reservation;
        this.roomCharge = roomCharge;
        this.serviceCharge = serviceCharge;
        this.otherCharges = otherCharges;
        this.taxAmount = taxAmount;
        this.totalBill = totalBill;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.notes = notes;
    }

    public int getBillingId() {
        return billingId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public double getRoomCharge() {
        return roomCharge;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public double getOtherCharges() {
        return otherCharges;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public String getNotes() {
        return notes;
    }

    public int getReservationId() {
        return reservation.getReservationId();
    }
}
