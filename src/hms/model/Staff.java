package hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a staff member of the hotel.
 */
public final class Staff {

    private final int staffId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String position;
    private final String department;
    private final Double salary;
    private final LocalDate joiningDate;
    private final String status;
    private final String passwordHash;
    private final LocalDateTime createdAt;

    /**
     * Creates a new staff member without an ID (for new records).
     *
     * @param firstName    The staff member's first name
     * @param lastName     The staff member's last name
     * @param email        The staff member's email address
     * @param phone        The staff member's phone number
     * @param position     The staff member's position (e.g., "Manager", "Receptionist")
     * @param department   The department they belong to
     * @param salary       The staff member's salary
     * @param joiningDate  The date the staff member joined
     * @param status       The staff member's employment status
     * @param passwordHash The bcrypt hash of the staff member's password
     */
    public Staff(String firstName, String lastName, String email, String phone,
                 String position, String department, Double salary,
                 LocalDate joiningDate, String status, String passwordHash) {
        this.staffId = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.department = department;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.status = status;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Creates a staff member with all fields (for database reconstruction).
     *
     * @param staffId      The staff member's unique ID
     * @param firstName    The staff member's first name
     * @param lastName     The staff member's last name
     * @param email        The staff member's email address
     * @param phone        The staff member's phone number
     * @param position     The staff member's position
     * @param department   The department they belong to
     * @param salary       The staff member's salary
     * @param joiningDate  The date the staff member joined
     * @param status       The staff member's employment status
     * @param passwordHash The bcrypt hash of the staff member's password
     * @param createdAt    The timestamp when the record was created
     */
    public Staff(int staffId, String firstName, String lastName, String email,
                 String phone, String position, String department,
                 Double salary, LocalDate joiningDate, String status,
                 String passwordHash, LocalDateTime createdAt) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.department = department;
        this.salary = salary;
        this.joiningDate = joiningDate;
        this.status = status;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    /** Returns the staff member's unique ID. */
    public int getStaffId() {
        return staffId;
    }

    /** Returns the staff member's first name. */
    public String getFirstName() {
        return firstName;
    }

    /** Returns the staff member's last name. */
    public String getLastName() {
        return lastName;
    }

    /** Returns the staff member's email address. */
    public String getEmail() {
        return email;
    }

    /** Returns the staff member's phone number. */
    public String getPhone() {
        return phone;
    }

    /** Returns the staff member's position. */
    public String getPosition() {
        return position;
    }

    /** Returns the department the staff member belongs to. */
    public String getDepartment() {
        return department;
    }

    /** Returns the staff member's salary. */
    public Double getSalary() {
        return salary;
    }

    /** Returns the staff member's joining date. */
    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    /** Returns the staff member's employment status. */
    public String getStatus() {
        return status;
    }

    /** Returns the bcrypt hash of the staff member's password. */
    public String getPasswordHash() {
        return passwordHash;
    }

    /** Returns the timestamp when the record was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
