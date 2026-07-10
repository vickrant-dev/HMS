package hms.model;

import java.time.LocalDateTime;

/**
 * Represents a room in the hotel.
 */
public final class Room {

    private final int roomId;
    private final String roomNumber;
    private final String roomType;
    private final int capacity;
    private final double basePrice;
    private final String description;
    private final String status;
    private final int floor;
    private final LocalDateTime createdAt;

    /**
     * Creates a new room without an ID (for new records).
     *
     * @param roomNumber The room number
     * @param roomType   The type of room (e.g., "Single", "Double", "Suite")
     * @param capacity   The maximum number of guests
     * @param basePrice  The base price per night
     * @param description The room description
     * @param status     The room status (e.g., "available", "occupied")
     * @param floor      The floor number
     */
    public Room(String roomNumber, String roomType, int capacity,
                double basePrice, String description, String status, int floor) {
        this.roomId = 0;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.description = description;
        this.status = status;
        this.floor = floor;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Creates a room with all fields (for database reconstruction).
     *
     * @param roomId      The room's unique ID
     * @param roomNumber  The room number
     * @param roomType    The type of room
     * @param capacity    The maximum number of guests
     * @param basePrice   The base price per night
     * @param description The room description
     * @param status      The room status
     * @param floor       The floor number
     * @param createdAt   The timestamp when the record was created
     */
    public Room(int roomId, String roomNumber, String roomType, int capacity,
                double basePrice, String description, String status, int floor,
                LocalDateTime createdAt) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.description = description;
        this.status = status;
        this.floor = floor;
        this.createdAt = createdAt;
    }

    /** Returns the room's unique ID. */
    public int getRoomId() {
        return roomId;
    }

    /** Returns the room number. */
    public String getRoomNumber() {
        return roomNumber;
    }

    /** Returns the type of room. */
    public String getRoomType() {
        return roomType;
    }

    /** Returns the maximum number of guests. */
    public int getCapacity() {
        return capacity;
    }

    /** Returns the base price per night. */
    public double getBasePrice() {
        return basePrice;
    }

    /** Returns the room description. */
    public String getDescription() {
        return description;
    }

    /** Returns the room status. */
    public String getStatus() {
        return status;
    }

    /** Returns the floor number. */
    public int getFloor() {
        return floor;
    }

    /** Returns the timestamp when the record was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
