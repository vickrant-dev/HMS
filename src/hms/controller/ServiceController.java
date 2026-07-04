package hms.controller;

import hms.config.Constants;
import hms.dao.ServiceBookingDAO;
import hms.dao.ServiceDAO;
import hms.exception.DatabaseException;
import hms.exception.ValidationException;
import hms.model.Reservation;
import hms.model.Service;
import hms.model.ServiceBooking;
import hms.util.ValidationUtil;

import java.util.List;

public class ServiceController {

    private final ServiceDAO serviceDAO;
    private final ServiceBookingDAO serviceBookingDAO;

    public ServiceController() {
        this.serviceDAO = new ServiceDAO();
        this.serviceBookingDAO = new ServiceBookingDAO();
    }

    public Service createService(Service service)
            throws ValidationException, DatabaseException {
        validateService(service);

        if (serviceDAO.existsByServiceName(service.getServiceName())) {
            throw new ValidationException("Service name already exists");
        }

        return serviceDAO.save(service);
    }

    public Service getServiceById(int id) throws DatabaseException {
        return serviceDAO.getById(id);
    }

    public List<Service> getAllServices() throws DatabaseException {
        return serviceDAO.getAll();
    }

    public void updateService(Service service)
            throws ValidationException, DatabaseException {
        if (service == null || service.getServiceId() <= 0) {
            throw new ValidationException("Invalid service ID");
        }

        validateService(service);

        Service existing = serviceDAO.getById(service.getServiceId());
        if (existing == null) {
            throw new ValidationException("Service not found");
        }

        if (!existing.getServiceName().equals(service.getServiceName())
                && serviceDAO.existsByServiceName(service.getServiceName())) {
            throw new ValidationException("Service name already exists");
        }

        serviceDAO.update(service);
    }

    public void deleteService(int id) throws DatabaseException {
        serviceDAO.delete(id);
    }

    public List<Service> filterByType(String serviceType) throws DatabaseException {
        return serviceDAO.filterByType(serviceType);
    }

    public List<Service> filterByAvailability(boolean available) throws DatabaseException {
        return serviceDAO.filterByAvailability(available);
    }

    public void toggleAvailability(int serviceId)
            throws ValidationException, DatabaseException {
        Service existing = serviceDAO.getById(serviceId);
        if (existing == null) {
            throw new ValidationException("Service not found");
        }

        Service toggled = new Service(
                existing.getServiceId(),
                existing.getServiceName(),
                existing.getServiceType(),
                existing.getPrice(),
                existing.getDescription(),
                !existing.isAvailable(),
                existing.getCreatedAt()
        );

        serviceDAO.update(toggled);
    }

    public ServiceBooking createServiceBooking(Reservation reservation,
                                               Service service, int quantity,
                                               String notes)
            throws ValidationException, DatabaseException {

        if (reservation == null) {
            throw new ValidationException("Reservation is required");
        }
        if (service == null) {
            throw new ValidationException("Service is required");
        }
        if (!service.isAvailable()) {
            throw new ValidationException("Service is not currently available");
        }
        if (!ValidationUtil.isPositive(quantity)) {
            throw new ValidationException("Quantity must be positive");
        }

        double totalPrice = quantity * service.getPrice();

        ServiceBooking booking = new ServiceBooking(
                reservation, service, quantity, totalPrice,
                Constants.SVC_BOOKING_PENDING, notes
        );

        return serviceBookingDAO.save(booking);
    }

    public ServiceBooking getBookingById(int id) throws DatabaseException {
        return serviceBookingDAO.getById(id);
    }

    public List<ServiceBooking> getAllBookings() throws DatabaseException {
        return serviceBookingDAO.getAll();
    }

    public void updateBooking(ServiceBooking booking) throws DatabaseException {
        serviceBookingDAO.update(booking);
    }

    public void cancelBooking(int bookingId)
            throws ValidationException, DatabaseException {
        ServiceBooking existing = serviceBookingDAO.getById(bookingId);
        if (existing == null) {
            throw new ValidationException("Service booking not found");
        }

        ServiceBooking cancelled = new ServiceBooking(
                existing.getServiceBookingId(),
                existing.getReservation(),
                existing.getService(),
                existing.getBookingDate(),
                existing.getQuantity(),
                existing.getTotalPrice(),
                Constants.SVC_BOOKING_CANCELLED,
                existing.getNotes()
        );

        serviceBookingDAO.update(cancelled);
    }

    public List<ServiceBooking> getBookingsByReservation(int reservationId)
            throws DatabaseException {
        return serviceBookingDAO.getByReservationId(reservationId);
    }

    private void validateService(Service service) throws ValidationException {
        if (service == null) {
            throw new ValidationException("Service cannot be null");
        }

        if (!ValidationUtil.isNotEmpty(service.getServiceName())) {
            throw new ValidationException("Service name is required");
        }

        if (!ValidationUtil.isNotEmpty(service.getServiceType())) {
            throw new ValidationException("Service type is required");
        }

        if (!ValidationUtil.isPositive(service.getPrice())) {
            throw new ValidationException("Price must be positive");
        }

        if (!ValidationUtil.isValidLength(service.getServiceName(), 100)) {
            throw new ValidationException("Service name must not exceed 100 characters");
        }

        if (!ValidationUtil.isValidLength(service.getServiceType(), 50)) {
            throw new ValidationException("Service type must not exceed 50 characters");
        }

        if (!ValidationUtil.isValidLength(service.getDescription(), 500)) {
            throw new ValidationException("Description must not exceed 500 characters");
        }
    }
}
