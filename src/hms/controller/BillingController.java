package hms.controller;

import hms.config.Constants;
import hms.dao.BillingDAO;
import hms.dao.ServiceBookingDAO;
import hms.exception.DatabaseException;
import hms.exception.ValidationException;
import hms.model.Billing;
import hms.model.Reservation;
import hms.service.NormalPricingStrategy;
import hms.service.PricingStrategy;
import hms.util.DateUtil;
import hms.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BillingController {

    private final BillingDAO billingDAO;
    private final ServiceBookingDAO serviceBookingDAO;

    public BillingController() {
        this.billingDAO = new BillingDAO();
        this.serviceBookingDAO = new ServiceBookingDAO();
    }

    public Billing generateBill(Reservation reservation, double otherCharges,
                                String notes) throws ValidationException, DatabaseException {

        if (reservation == null) {
            throw new ValidationException("Reservation is required");
        }

        long nights = DateUtil.calculateNights(
                reservation.getCheckInDate(), reservation.getCheckOutDate());
        if (nights <= 0) {
            throw new ValidationException("Invalid reservation dates");
        }

        PricingStrategy strategy = new NormalPricingStrategy();
        double roomCharge = strategy.calculatePrice(reservation.getRoom(), (int) nights);

        double serviceCharge = serviceBookingDAO.calculateServiceCharges(
                reservation.getReservationId());

        if (!ValidationUtil.isPositive(roomCharge)) {
            throw new ValidationException("Room charge must be positive");
        }
        if (otherCharges < 0) {
            throw new ValidationException("Other charges cannot be negative");
        }

        double taxAmount = (roomCharge + serviceCharge + otherCharges)
                * Constants.DEFAULT_TAX_RATE;
        double totalBill = roomCharge + serviceCharge + otherCharges + taxAmount;

        Billing billing = new Billing(
                reservation, roomCharge, serviceCharge, otherCharges,
                taxAmount, totalBill,
                Constants.PAYMENT_PENDING, null, notes
        );

        return billingDAO.save(billing);
    }

    public Billing getBillById(int id) throws DatabaseException {
        return billingDAO.getById(id);
    }

    public Billing getBillByReservationId(int reservationId) throws DatabaseException {
        return billingDAO.getByReservationId(reservationId);
    }

    public List<Billing> getAllBills() throws DatabaseException {
        return billingDAO.getAll();
    }

    public void recordPayment(int billingId, String paymentStatus)
            throws ValidationException, DatabaseException {

        if (!isValidPaymentStatus(paymentStatus)) {
            throw new ValidationException("Invalid payment status");
        }

        Billing billing = billingDAO.getById(billingId);
        if (billing == null) {
            throw new ValidationException("Billing record not found");
        }

        billingDAO.updatePaymentStatus(billingId, paymentStatus, LocalDateTime.now());
    }

    public double getRevenueByDateRange(LocalDate start, LocalDate end)
            throws DatabaseException {
        return billingDAO.getRevenueByDateRange(start, end);
    }

    public double calculateServiceCharges(int reservationId) throws DatabaseException {
        return serviceBookingDAO.calculateServiceCharges(reservationId);
    }

    private boolean isValidPaymentStatus(String status) {
        return Constants.PAYMENT_PENDING.equals(status)
                || Constants.PAYMENT_PARTIAL.equals(status)
                || Constants.PAYMENT_PAID.equals(status)
                || Constants.PAYMENT_REFUNDED.equals(status);
    }
}
