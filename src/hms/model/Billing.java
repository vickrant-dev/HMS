package hms.model;

import java.time.LocalDateTime;

/**
 * Represents a billing record for a reservation.
 */
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

    /**
     * Creates a new billing record without an ID (for new records).
     *
     * @param reservation   The associated reservation
     * @param roomCharge    The charge for the room
     * @param serviceCharge The charge for services
     * @param otherCharges  Any other charges
     * @param taxAmount     The tax amount applied
     * @param totalBill     The total bill amount
     * @param paymentStatus The payment status (e.g., "Paid", "Pending")
     * @param paymentDate   The date of payment
     * @param notes         Additional notes
     */
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

    /**
     * Creates a billing record with all fields (for database reconstruction).
     *
     * @param billingId     The billing record's unique ID
     * @param reservation   The associated reservation
     * @param roomCharge    The charge for the room
     * @param serviceCharge The charge for services
     * @param otherCharges  Any other charges
     * @param taxAmount     The tax amount applied
     * @param totalBill     The total bill amount
     * @param paymentStatus The payment status
     * @param paymentDate   The date of payment
     * @param notes         Additional notes
     */
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

    /** Returns the billing record's unique ID. */
    public int getBillingId() {
        return billingId;
    }

    /** Returns the associated reservation. */
    public Reservation getReservation() {
        return reservation;
    }

    /** Returns the charge for the room. */
    public double getRoomCharge() {
        return roomCharge;
    }

    /** Returns the charge for services. */
    public double getServiceCharge() {
        return serviceCharge;
    }

    /** Returns any other charges. */
    public double getOtherCharges() {
        return otherCharges;
    }

    /** Returns the tax amount applied. */
    public double getTaxAmount() {
        return taxAmount;
    }

    /** Returns the total bill amount. */
    public double getTotalBill() {
        return totalBill;
    }

    /** Returns the payment status. */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /** Returns the date of payment. */
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    /** Returns additional notes. */
    public String getNotes() {
        return notes;
    }

    /** Returns the associated reservation's ID. */
    public int getReservationId() {
        return reservation.getReservationId();
    }
}
