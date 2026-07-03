package hms.controller;

import hms.config.Constants;
import hms.dao.BillingDAO;
import hms.dao.ServiceBookingDAO;
import hms.exception.DatabaseException;
import hms.exception.ValidationException;
import hms.model.Billing;
import hms.model.Guest;
import hms.model.Reservation;
import hms.service.CorporatePricingStrategy;
import hms.service.NormalPricingStrategy;
import hms.service.PricingStrategy;
import hms.service.SeasonalPricingStrategy;
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

        PricingStrategy strategy;
        Guest guest = reservation.getGuest();
        if (guest != null && "Corporate".equals(guest.getGuestType())) {
            strategy = new CorporatePricingStrategy(0.15);
        } else {
            int month = reservation.getCheckInDate().getMonthValue();
            if (month >= 6 && month <= 8 || month == 12) {
                strategy = new SeasonalPricingStrategy(1.5);
            } else {
                strategy = new NormalPricingStrategy();
            }
        }
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

    public Billing adjustBill(int billingId, double otherCharges,
                               double discountAmount, double lateCharge,
                               String notes)
            throws ValidationException, DatabaseException {

        if (otherCharges < 0) {
            throw new ValidationException("Other charges cannot be negative");
        }
        if (discountAmount < 0) {
            throw new ValidationException("Discount cannot be negative");
        }
        if (lateCharge < 0) {
            throw new ValidationException("Late charge cannot be negative");
        }

        Billing existing = billingDAO.getById(billingId);
        if (existing == null) {
            throw new ValidationException("Billing record not found");
        }

        double roomCharge = existing.getRoomCharge();
        double serviceCharge = existing.getServiceCharge();

        // net other charges after adjustments
        double netOtherCharges = otherCharges + lateCharge - discountAmount;
        if (netOtherCharges < 0) netOtherCharges = 0;

        double taxAmount = (roomCharge + serviceCharge + netOtherCharges)
                * Constants.DEFAULT_TAX_RATE;
        double totalBill = roomCharge + serviceCharge + netOtherCharges + taxAmount;

        billingDAO.updateCharges(billingId, netOtherCharges, discountAmount, lateCharge,
                taxAmount, totalBill, notes);

        return billingDAO.getById(billingId);
    }

    @Deprecated
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
