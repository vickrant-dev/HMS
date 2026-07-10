package hms.controller;

import hms.config.Constants;
import hms.dao.GuestDAO;
import hms.exception.DatabaseException;
import hms.exception.ValidationException;
import hms.model.Guest;
import hms.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class GuestController {

    private final GuestDAO guestDAO;

    public GuestController() {
        this.guestDAO = new GuestDAO();
    }

    public Guest createGuest(Guest guest) throws ValidationException, DatabaseException {
        validateGuest(guest);

        if (guestDAO.existsByEmail(guest.getEmail())) {
            throw new ValidationException("Email already registered");
        }

        return guestDAO.save(guest);
    }

    public Guest getGuestById(int id) throws DatabaseException {
        return guestDAO.getById(id);
    }

    public List<Guest> getAllGuests() throws DatabaseException {
        return guestDAO.getAll();
    }

    public void updateGuest(Guest guest) throws ValidationException, DatabaseException {
        if (guest == null || guest.getGuestId() <= 0) {
            throw new ValidationException("Invalid guest ID");
        }

        if (!ValidationUtil.isValidName(guest.getFirstName())) {
            throw new ValidationException(Constants.ERROR_INVALID_NAME);
        }

        if (!ValidationUtil.isValidName(guest.getLastName())) {
            throw new ValidationException(Constants.ERROR_INVALID_NAME);
        }

        if (!ValidationUtil.isValidEmail(guest.getEmail())) {
            throw new ValidationException(Constants.ERROR_INVALID_EMAIL);
        }

        if (!ValidationUtil.isValidPhone(guest.getPhone())) {
            throw new ValidationException(Constants.ERROR_INVALID_PHONE);
        }

        Guest existing = guestDAO.getById(guest.getGuestId());
        if (existing == null) {
            throw new ValidationException(Constants.ERROR_GUEST_NOT_FOUND);
        }

        if (!existing.getEmail().equals(guest.getEmail())
                && guestDAO.existsByEmail(guest.getEmail())) {
            throw new ValidationException("Email already registered");
        }

        guestDAO.update(guest);
    }

    public void deleteGuest(int id) throws DatabaseException {
        guestDAO.delete(id);
    }

    public List<Guest> searchByName(String name) throws DatabaseException {
        if (name == null || name.isBlank()) {
            return guestDAO.getAll();
        }
        return guestDAO.searchByName(name.trim());
    }

    private Guest searchByEmail(String email) throws DatabaseException {
        if (email == null || email.isBlank()) {
            return null;
        }
        return guestDAO.searchByEmail(email.trim());
    }

    private List<Guest> searchByPhone(String phone) throws DatabaseException {
        if (phone == null || phone.isBlank()) {
            return guestDAO.getAll();
        }
        return guestDAO.searchByPhone(phone.trim());
    }

    public List<Guest> searchGuests(String keyword) throws DatabaseException {
        if (keyword == null || keyword.isBlank()) {
            return guestDAO.getAll();
        }

        String trimmed = keyword.trim();
        List<Guest> results = new ArrayList<>();

        List<Guest> byName = guestDAO.searchByName(trimmed);
        results.addAll(byName);

        Guest byEmail = guestDAO.searchByEmail(trimmed);
        if (byEmail != null && !results.contains(byEmail)) {
            results.add(byEmail);
        }

        List<Guest> byPhone = guestDAO.searchByPhone(trimmed);
        for (Guest g : byPhone) {
            if (!results.contains(g)) {
                results.add(g);
            }
        }

        return results;
    }

    private void validateGuest(Guest guest) throws ValidationException {
        if (guest == null) {
            throw new ValidationException("Guest cannot be null");
        }

        if (!ValidationUtil.isValidName(guest.getFirstName())) {
            throw new ValidationException(Constants.ERROR_INVALID_NAME);
        }

        if (!ValidationUtil.isValidName(guest.getLastName())) {
            throw new ValidationException(Constants.ERROR_INVALID_NAME);
        }

        if (!ValidationUtil.isValidEmail(guest.getEmail())) {
            throw new ValidationException(Constants.ERROR_INVALID_EMAIL);
        }

        if (!ValidationUtil.isValidPhone(guest.getPhone())) {
            throw new ValidationException(Constants.ERROR_INVALID_PHONE);
        }

        if (!ValidationUtil.isValidLength(guest.getFirstName(), 50)) {
            throw new ValidationException("First name must not exceed 50 characters");
        }

        if (!ValidationUtil.isValidLength(guest.getLastName(), 50)) {
            throw new ValidationException("Last name must not exceed 50 characters");
        }

        if (!ValidationUtil.isValidLength(guest.getAddress(), 200)) {
            throw new ValidationException("Address must not exceed 200 characters");
        }

        if (!ValidationUtil.isValidLength(guest.getIdProofNumber(), 50)) {
            throw new ValidationException("ID proof number must not exceed 50 characters");
        }
    }
}
