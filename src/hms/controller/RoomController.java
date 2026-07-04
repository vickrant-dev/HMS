package hms.controller;

import hms.config.Constants;
import hms.dao.RoomDAO;
import hms.exception.DatabaseException;
import hms.exception.ValidationException;
import hms.model.Room;
import hms.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class RoomController {

    private final RoomDAO roomDAO;

    public RoomController() {
        this.roomDAO = new RoomDAO();
    }

    public Room createRoom(Room room) throws ValidationException, DatabaseException {
        validateRoom(room);

        if (roomDAO.existsByRoomNumber(room.getRoomNumber())) {
            throw new ValidationException("Room number already exists");
        }

        return roomDAO.save(room);
    }

    public Room getRoomById(int id) throws DatabaseException {
        return roomDAO.getById(id);
    }

    public List<Room> getAllRooms() throws DatabaseException {
        return roomDAO.getAll();
    }

    public void updateRoom(Room room) throws ValidationException, DatabaseException {
        if (room.getRoomId() <= 0) {
            throw new ValidationException("Invalid room ID");
        }

        validateRoom(room);

        Room existing = roomDAO.getById(room.getRoomId());
        if (existing == null) {
            throw new ValidationException("Room not found");
        }

        if (!existing.getRoomNumber().equals(room.getRoomNumber())
                && roomDAO.existsByRoomNumber(room.getRoomNumber())) {
            throw new ValidationException("Room number already exists");
        }

        roomDAO.update(room);
    }

    public void deleteRoom(int id) throws DatabaseException {
        roomDAO.delete(id);
    }

    public List<Room> searchRooms(String keyword) throws DatabaseException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return roomDAO.getAll();
        }
        return roomDAO.searchByRoomNumber(keyword);
    }

    public List<Room> filterByStatus(String status) throws DatabaseException {
        return roomDAO.filterByStatus(status);
    }

    public List<Room> filterByType(String roomType) throws DatabaseException {
        return roomDAO.filterByType(roomType);
    }

    public List<Room> filterByPriceRange(double min, double max) throws DatabaseException {
        return roomDAO.filterByPriceRange(min, max);
    }

    public List<Room> checkAvailability(LocalDate checkIn, LocalDate checkOut)
            throws DatabaseException {
        return roomDAO.checkAvailability(checkIn, checkOut);
    }

    public void updateRoomStatus(int roomId, String newStatus)
            throws ValidationException, DatabaseException {
        Room existing = roomDAO.getById(roomId);
        if (existing == null) {
            throw new ValidationException("Room not found");
        }

        Room updated = new Room(
                existing.getRoomId(),
                existing.getRoomNumber(),
                existing.getRoomType(),
                existing.getCapacity(),
                existing.getBasePrice(),
                existing.getDescription(),
                newStatus,
                existing.getFloor(),
                existing.getCreatedAt()
        );

        roomDAO.update(updated);
    }

    public void markForMaintenance(int roomId)
            throws ValidationException, DatabaseException {
        updateRoomStatus(roomId, Constants.ROOM_STATUS_MAINTENANCE);
    }

    private void validateRoom(Room room) throws ValidationException {
        if (room == null) {
            throw new ValidationException("Room cannot be null");
        }

        if (!ValidationUtil.isNotEmpty(room.getRoomNumber())) {
            throw new ValidationException("Room number is required");
        }

        if (!ValidationUtil.isNotEmpty(room.getRoomType())) {
            throw new ValidationException("Room type is required");
        }

        if (!ValidationUtil.isPositive(room.getCapacity())) {
            throw new ValidationException("Capacity must be positive");
        }

        if (!ValidationUtil.isPositive(room.getBasePrice())) {
            throw new ValidationException("Price must be positive");
        }

        if (!ValidationUtil.isNotEmpty(room.getStatus())) {
            throw new ValidationException("Status is required");
        }

        if (room.getFloor() < 0) {
            throw new ValidationException("Floor must be zero or positive");
        }
    }
}
