package hms.controller;

import hms.config.Constants;
import hms.dao.ReservationDAO;
import hms.exception.DatabaseException;
import hms.exception.ValidationException;
import hms.model.Guest;
import hms.model.Reservation;
import hms.model.Room;
import hms.service.CorporatePricingStrategy;
import hms.service.NormalPricingStrategy;
import hms.service.PricingStrategy;
import hms.service.SeasonalPricingStrategy;
import hms.util.DateUtil;
import hms.util.StringUtil;
import hms.util.ValidationUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationController {

    private final ReservationDAO reservationDAO;
    private final RoomController roomController;
    private final List<DashboardObserver> observers;

    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
        this.roomController = new RoomController();
        this.observers = new ArrayList<>();
    }

    public void addObserver(DashboardObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(DashboardObserver observer) {
        observers.remove(observer);
    }

    public Reservation createReservation(Guest guest, Room room, LocalDate checkInDate,
                                         LocalDate checkOutDate, int numberOfGuests,
                                         Integer createdByStaffId, String notes)
            throws ValidationException, DatabaseException {

        validateReservationInput(guest, room, checkInDate, checkOutDate, numberOfGuests);

        if (room.getCapacity() < numberOfGuests) {
            throw new ValidationException("Room capacity exceeded");
        }

        List<Room> available = roomController.checkAvailability(checkInDate, checkOutDate);
        boolean isAvailable = available.stream().anyMatch(r -> r.getRoomId() == room.getRoomId());
        if (!isAvailable) {
            throw new ValidationException(Constants.ERROR_ROOM_NOT_AVAILABLE);
        }

        double totalAmount = calculateTotalAmount(guest, room, checkInDate, checkOutDate);

        String displayId = StringUtil.generateReservationId();
        Reservation reservation = new Reservation(
                guest, room, checkInDate, checkOutDate, numberOfGuests,
                Constants.RES_STATUS_CONFIRMED, totalAmount, displayId, createdByStaffId,
                notes
        );

        Reservation saved = reservationDAO.save(reservation);

        roomController.updateRoomStatus(room.getRoomId(), Constants.ROOM_STATUS_RESERVED);

        notifyCreated(saved);

        return saved;
    }

    public Reservation getReservationById(int id) throws DatabaseException {
        return reservationDAO.getById(id);
    }

    public List<Reservation> getAllReservations() throws DatabaseException {
        return reservationDAO.getAll();
    }

    public void updateReservation(Reservation reservation)
            throws ValidationException, DatabaseException {

        if (reservation == null || reservation.getReservationId() <= 0) {
            throw new ValidationException("Invalid reservation");
        }

        Reservation existing = reservationDAO.getById(reservation.getReservationId());
        if (existing == null) {
            throw new ValidationException(Constants.ERROR_RESERVATION_NOT_FOUND);
        }

        if (Constants.RES_STATUS_CANCELLED.equals(existing.getStatus())
                || Constants.RES_STATUS_CHECKED_OUT.equals(existing.getStatus())) {
            throw new ValidationException("Cannot modify a cancelled or checked-out reservation");
        }

        validateReservationInput(
                reservation.getGuest(), reservation.getRoom(),
                reservation.getCheckInDate(), reservation.getCheckOutDate(),
                reservation.getNumberOfGuests()
        );

        boolean datesChanged = !existing.getCheckInDate().equals(reservation.getCheckInDate())
                || !existing.getCheckOutDate().equals(reservation.getCheckOutDate());
        boolean roomChanged = existing.getRoomId() != reservation.getRoomId();

        if (datesChanged || roomChanged) {
            List<Room> available = roomController.checkAvailability(
                    reservation.getCheckInDate(), reservation.getCheckOutDate());
            boolean isAvailable = available.stream()
                    .anyMatch(r -> r.getRoomId() == reservation.getRoomId());
            if (!isAvailable) {
                throw new ValidationException(Constants.ERROR_ROOM_NOT_AVAILABLE);
            }
        }

        reservationDAO.update(reservation);
    }

    public void cancelReservation(int reservationId, String cancellationNotes)
            throws ValidationException, DatabaseException {

        Reservation reservation = reservationDAO.getById(reservationId);
        if (reservation == null) {
            throw new ValidationException(Constants.ERROR_RESERVATION_NOT_FOUND);
        }

        if (Constants.RES_STATUS_CHECKED_OUT.equals(reservation.getStatus())
                || Constants.RES_STATUS_CANCELLED.equals(reservation.getStatus())) {
            throw new ValidationException("Reservation already finalized or cancelled");
        }

        reservationDAO.updateStatus(reservationId, Constants.RES_STATUS_CANCELLED, cancellationNotes);
        roomController.updateRoomStatus(
                reservation.getRoomId(), Constants.ROOM_STATUS_AVAILABLE);
    }

    public void checkIn(int reservationId, String checkInNotes)
            throws ValidationException, DatabaseException {

        Reservation reservation = reservationDAO.getById(reservationId);
        if (reservation == null) {
            throw new ValidationException(Constants.ERROR_RESERVATION_NOT_FOUND);
        }

        if (!Constants.RES_STATUS_CONFIRMED.equals(reservation.getStatus())) {
            throw new ValidationException(
                    "Only confirmed reservations can be checked in");
        }

        String notes = checkInNotes != null && !checkInNotes.isEmpty()
                ? (reservation.getNotes() != null ? reservation.getNotes() + "\n" : "") + "Check-in: " + checkInNotes
                : reservation.getNotes();
        reservationDAO.updateStatus(reservationId, Constants.RES_STATUS_CHECKED_IN, notes);
        roomController.updateRoomStatus(
                reservation.getRoomId(), Constants.ROOM_STATUS_OCCUPIED);

        notifyCheckIn(reservation);
    }

    public void checkOut(int reservationId)
            throws ValidationException, DatabaseException {

        Reservation reservation = reservationDAO.getById(reservationId);
        if (reservation == null) {
            throw new ValidationException(Constants.ERROR_RESERVATION_NOT_FOUND);
        }

        if (!Constants.RES_STATUS_CHECKED_IN.equals(reservation.getStatus())) {
            throw new ValidationException(
                    "Only checked-in reservations can be checked out");
        }

        reservationDAO.updateStatus(reservationId, Constants.RES_STATUS_CHECKED_OUT);
        roomController.updateRoomStatus(
                reservation.getRoomId(), Constants.ROOM_STATUS_AVAILABLE);

        notifyCheckOut(reservation);
    }

    public List<Reservation> getByGuestId(int guestId) throws DatabaseException {
        return reservationDAO.getByGuestId(guestId);
    }

    public List<Reservation> getByStatus(String status) throws DatabaseException {
        return reservationDAO.getByStatus(status);
    }

    public List<Reservation> getByDateRange(LocalDate start, LocalDate end)
            throws DatabaseException {
        return reservationDAO.getByDateRange(start, end);
    }

    public List<Reservation> getTodayCheckIns() throws DatabaseException {
        return reservationDAO.getTodayCheckIns();
    }

    public List<Reservation> getTodayCheckOuts() throws DatabaseException {
        return reservationDAO.getTodayCheckOuts();
    }

    private void validateReservationInput(Guest guest, Room room,
                                          LocalDate checkInDate, LocalDate checkOutDate,
                                          int numberOfGuests)
            throws ValidationException {

        if (guest == null) {
            throw new ValidationException("Guest is required");
        }
        if (room == null) {
            throw new ValidationException("Room is required");
        }
        if (checkInDate == null || checkInDate.isBefore(LocalDate.now())) {
            throw new ValidationException("Check-in must be today or a future date");
        }
        if (checkOutDate == null || checkOutDate.isBefore(LocalDate.now())) {
            throw new ValidationException("Check-out must be today or a future date");
        }
        if (checkOutDate != null && checkInDate != null
                && !checkOutDate.isAfter(checkInDate)) {
            throw new ValidationException(Constants.ERROR_INVALID_DATES);
        }
        if (!ValidationUtil.isInRange(numberOfGuests,
                Constants.MIN_GUESTS, Constants.MAX_GUESTS)) {
            throw new ValidationException(
                    "Number of guests must be between " + Constants.MIN_GUESTS
                    + " and " + Constants.MAX_GUESTS);
        }
    }

    private PricingStrategy selectPricingStrategy(Guest guest, LocalDate checkIn) {
        if (guest == null) return new NormalPricingStrategy();
        if ("Corporate".equals(guest.getGuestType())) {
            return new CorporatePricingStrategy(0.15);
        }
        int month = checkIn.getMonthValue();
        if (month >= 6 && month <= 8 || month == 12) {
            return new SeasonalPricingStrategy(1.5);
        }
        return new NormalPricingStrategy();
    }

    private double calculateTotalAmount(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        long nights = DateUtil.calculateNights(checkIn, checkOut);
        if (nights <= 0) {
            return 0.0;
        }
        PricingStrategy strategy = selectPricingStrategy(guest, checkIn);
        return strategy.calculatePrice(room, (int) nights);
    }

    private void notifyCreated(Reservation reservation) {
        for (DashboardObserver observer : observers) {
            observer.onReservationCreated(reservation);
        }
    }

    private void notifyCheckIn(Reservation reservation) {
        for (DashboardObserver observer : observers) {
            observer.onCheckIn(reservation);
        }
    }

    private void notifyCheckOut(Reservation reservation) {
        for (DashboardObserver observer : observers) {
            observer.onCheckOut(reservation);
        }
    }
}
