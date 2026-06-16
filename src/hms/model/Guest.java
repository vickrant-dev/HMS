package hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class Guest {

    private final int guestId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String address;
    private final String idProofType;
    private final String idProofNumber;
    private final LocalDate dateOfBirth;
    private final LocalDateTime createdAt;

    public Guest(String firstName, String lastName, String email, String phone,
                 String address, String idProofType, String idProofNumber,
                 LocalDate dateOfBirth) {
        this.guestId = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.idProofType = idProofType;
        this.idProofNumber = idProofNumber;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = LocalDateTime.now();
    }

    public Guest(int guestId, String firstName, String lastName, String email,
                 String phone, String address, String idProofType,
                 String idProofNumber, LocalDate dateOfBirth,
                 LocalDateTime createdAt) {
        this.guestId = guestId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.idProofType = idProofType;
        this.idProofNumber = idProofNumber;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
    }

    public int getGuestId() {
        return guestId;
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

    public String getAddress() {
        return address;
    }

    public String getIdProofType() {
        return idProofType;
    }

    public String getIdProofNumber() {
        return idProofNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
