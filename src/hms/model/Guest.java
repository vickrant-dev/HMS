package hms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a guest in the hotel management system.
 */
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
    private final String guestType;
    private final String nationality;
    private final LocalDateTime createdAt;

    /**
     * Creates a new guest without an ID (for new records).
     *
     * @param firstName     The guest's first name
     * @param lastName      The guest's last name
     * @param email         The guest's email address
     * @param phone         The guest's phone number
     * @param address       The guest's physical address
     * @param idProofType   The type of ID proof (e.g., "Passport", "Driver's License")
     * @param idProofNumber The ID proof document number
     * @param dateOfBirth   The guest's date of birth
     * @param guestType     The guest type (e.g., "Regular", "VIP", "Corporate")
     * @param nationality   The guest's nationality
     */
    public Guest(String firstName, String lastName, String email, String phone,
                 String address, String idProofType, String idProofNumber,
                 LocalDate dateOfBirth, String guestType, String nationality) {
        this.guestId = 0;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.idProofType = idProofType;
        this.idProofNumber = idProofNumber;
        this.dateOfBirth = dateOfBirth;
        this.guestType = guestType;
        this.nationality = nationality;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Creates a guest with all fields (for database reconstruction).
     *
     * @param guestId       The guest's unique ID
     * @param firstName     The guest's first name
     * @param lastName      The guest's last name
     * @param email         The guest's email address
     * @param phone         The guest's phone number
     * @param address       The guest's physical address
     * @param idProofType   The type of ID proof
     * @param idProofNumber The ID proof document number
     * @param dateOfBirth   The guest's date of birth
     * @param guestType     The guest type (e.g., "Regular", "VIP", "Corporate")
     * @param nationality   The guest's nationality
     * @param createdAt     The timestamp when the record was created
     */
    public Guest(int guestId, String firstName, String lastName, String email,
                 String phone, String address, String idProofType,
                 String idProofNumber, LocalDate dateOfBirth,
                 String guestType, String nationality,
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
        this.guestType = guestType;
        this.nationality = nationality;
        this.createdAt = createdAt;
    }

    /** Returns the guest's unique ID. */
    public int getGuestId() {
        return guestId;
    }

    /** Returns the guest's first name. */
    public String getFirstName() {
        return firstName;
    }

    /** Returns the guest's last name. */
    public String getLastName() {
        return lastName;
    }

    /** Returns the guest's email address. */
    public String getEmail() {
        return email;
    }

    /** Returns the guest's phone number. */
    public String getPhone() {
        return phone;
    }

    /** Returns the guest's physical address. */
    public String getAddress() {
        return address;
    }

    /** Returns the type of ID proof. */
    public String getIdProofType() {
        return idProofType;
    }

    /** Returns the ID proof document number. */
    public String getIdProofNumber() {
        return idProofNumber;
    }

    /** Returns the guest's date of birth. */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /** Returns the guest type (e.g., "Regular", "VIP"). */
    public String getGuestType() {
        return guestType;
    }

    /** Returns the guest's nationality. */
    public String getNationality() {
        return nationality;
    }

    /** Returns the timestamp when the record was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
