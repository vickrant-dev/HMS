package hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public int getStaffId() {
        return staffId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public Double getSalary() {
        return salary;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public String getStatus() {
        return status;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
