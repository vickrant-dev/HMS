package hms.controller;

import hms.config.Constants;
import hms.dao.RoomAssignmentDAO;
import hms.dao.StaffDAO;
import hms.exception.DatabaseException;
import hms.exception.ValidationException;
import hms.model.Room;
import hms.model.RoomAssignment;
import hms.model.Staff;
import hms.util.PasswordUtil;
import hms.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class StaffController {

    private final StaffDAO staffDAO;
    private final RoomAssignmentDAO assignmentDAO;

    public StaffController() {
        this.staffDAO = new StaffDAO();
        this.assignmentDAO = new RoomAssignmentDAO();
    }

    public Staff createStaff(Staff staff) throws ValidationException, DatabaseException {
        validateStaff(staff);

        if (staffDAO.existsByEmail(staff.getEmail())) {
            throw new ValidationException("Email already registered");
        }

        String hashedPassword = PasswordUtil.hashPassword(staff.getPasswordHash());

        Staff staffToSave = new Staff(
                staff.getFirstName(), staff.getLastName(), staff.getEmail(),
                staff.getPhone(), staff.getPosition(), staff.getDepartment(),
                staff.getSalary(), staff.getJoiningDate(), staff.getStatus(),
                hashedPassword
        );

        return staffDAO.save(staffToSave);
    }

    public Staff getStaffById(int id) throws DatabaseException {
        return staffDAO.getById(id);
    }

    public List<Staff> getAllStaff() throws DatabaseException {
        return staffDAO.getAll();
    }

    public Staff authenticate(String email, String password) throws DatabaseException {
        Staff staff = staffDAO.findByEmail(email);
        if (staff == null) return null;
        if (PasswordUtil.verifyPassword(password, staff.getPasswordHash())) {
            return staff;
        }
        return null;
    }

    public void updateStaff(Staff staff) throws ValidationException, DatabaseException {
        if (staff == null || staff.getStaffId() <= 0) {
            throw new ValidationException("Invalid staff ID");
        }

        validateStaff(staff);

        Staff existing = staffDAO.getById(staff.getStaffId());
        if (existing == null) {
            throw new ValidationException("Staff not found");
        }

        if (!existing.getEmail().equals(staff.getEmail())
                && staffDAO.existsByEmail(staff.getEmail())) {
            throw new ValidationException("Email already registered");
        }

        String passwordHash;
        if (!existing.getPasswordHash().equals(staff.getPasswordHash())) {
            passwordHash = PasswordUtil.hashPassword(staff.getPasswordHash());
        } else {
            passwordHash = existing.getPasswordHash();
        }

        Staff staffToUpdate = new Staff(
                staff.getStaffId(),
                staff.getFirstName(), staff.getLastName(),
                staff.getEmail(), staff.getPhone(),
                staff.getPosition(), staff.getDepartment(),
                staff.getSalary(), staff.getJoiningDate(),
                staff.getStatus(), passwordHash,
                existing.getCreatedAt()
        );

        staffDAO.update(staffToUpdate);
    }

    public void deleteStaff(int id) throws DatabaseException {
        staffDAO.delete(id);
    }

    public List<Staff> filterByDepartment(String department) throws DatabaseException {
        return staffDAO.filterByDepartment(department);
    }

    public List<Staff> filterByPosition(String position) throws DatabaseException {
        return staffDAO.filterByPosition(position);
    }

    public List<Staff> filterByStatus(String status) throws DatabaseException {
        return staffDAO.filterByStatus(status);
    }

    public RoomAssignment createRoomAssignment(Room room, Staff staff,
                                               LocalDate assignmentDate,
                                               String assignmentType, String notes)
            throws ValidationException, DatabaseException {

        if (room == null) {
            throw new ValidationException("Room is required");
        }
        if (staff == null) {
            throw new ValidationException("Staff is required");
        }
        if (assignmentDate == null) {
            throw new ValidationException("Assignment date is required");
        }
        if (!ValidationUtil.isNotEmpty(assignmentType)) {
            throw new ValidationException("Assignment type is required");
        }

        RoomAssignment assignment = new RoomAssignment(
                room, staff, assignmentDate, assignmentType,
                Constants.ASSIGN_PENDING, notes
        );

        return assignmentDAO.save(assignment);
    }

    public RoomAssignment getAssignmentById(int id) throws DatabaseException {
        return assignmentDAO.getById(id);
    }

    public List<RoomAssignment> getAllAssignments() throws DatabaseException {
        return assignmentDAO.getAll();
    }

    public void updateAssignment(RoomAssignment assignment) throws DatabaseException {
        assignmentDAO.update(assignment);
    }

    public void deleteAssignment(int id) throws DatabaseException {
        assignmentDAO.delete(id);
    }

    public List<RoomAssignment> getAssignmentsByRoom(int roomId) throws DatabaseException {
        return assignmentDAO.getByRoomId(roomId);
    }

    public List<RoomAssignment> getAssignmentsByStaff(int staffId) throws DatabaseException {
        return assignmentDAO.getByStaffId(staffId);
    }

    public void updateAssignmentStatus(int assignmentId, String status)
            throws DatabaseException {
        assignmentDAO.updateStatus(assignmentId, status);
    }

    private void validateStaff(Staff staff) throws ValidationException {
        if (staff == null) {
            throw new ValidationException("Staff cannot be null");
        }

        if (!ValidationUtil.isValidName(staff.getFirstName())) {
            throw new ValidationException(Constants.ERROR_INVALID_NAME);
        }

        if (!ValidationUtil.isValidName(staff.getLastName())) {
            throw new ValidationException(Constants.ERROR_INVALID_NAME);
        }

        if (!ValidationUtil.isValidEmail(staff.getEmail())) {
            throw new ValidationException(Constants.ERROR_INVALID_EMAIL);
        }

        if (!ValidationUtil.isValidPhone(staff.getPhone())) {
            throw new ValidationException(Constants.ERROR_INVALID_PHONE);
        }

        if (!ValidationUtil.isNotEmpty(staff.getPosition())) {
            throw new ValidationException("Position is required");
        }

        if (!ValidationUtil.isNotEmpty(staff.getDepartment())) {
            throw new ValidationException("Department is required");
        }

        if (staff.getSalary() == null || !ValidationUtil.isPositive(staff.getSalary())) {
            throw new ValidationException("Salary must be positive");
        }

        if (!ValidationUtil.isNotEmpty(staff.getStatus())) {
            throw new ValidationException("Status is required");
        }

        if (!ValidationUtil.isValidLength(staff.getFirstName(), 50)) {
            throw new ValidationException("First name must not exceed 50 characters");
        }

        if (!ValidationUtil.isValidLength(staff.getLastName(), 50)) {
            throw new ValidationException("Last name must not exceed 50 characters");
        }

        if (!ValidationUtil.isValidLength(staff.getPosition(), 50)) {
            throw new ValidationException("Position must not exceed 50 characters");
        }

        if (!ValidationUtil.isValidLength(staff.getDepartment(), 50)) {
            throw new ValidationException("Department must not exceed 50 characters");
        }

        String pw = staff.getPasswordHash();
        if (pw == null || pw.length() < Constants.MIN_PASSWORD_LENGTH) {
            throw new ValidationException(
                "Password must be at least " + Constants.MIN_PASSWORD_LENGTH + " characters");
        }
    }
}
