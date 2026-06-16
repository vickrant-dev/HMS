package hms.model;

import java.time.LocalDateTime;

public final class Room {

    private final int roomId;
    private final String roomNumber;
    private final String roomType;
    private final int capacity;
    private final double basePrice;
    private final String status;
    private final int floor;
    private final LocalDateTime createdAt;

    public Room(String roomNumber, String roomType, int capacity,
                double basePrice, String status, int floor) {
        this.roomId = 0;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.status = status;
        this.floor = floor;
        this.createdAt = LocalDateTime.now();
    }

    public Room(int roomId, String roomNumber, String roomType, int capacity,
                double basePrice, String status, int floor,
                LocalDateTime createdAt) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.status = status;
        this.floor = floor;
        this.createdAt = createdAt;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getStatus() {
        return status;
    }

    public int getFloor() {
        return floor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
