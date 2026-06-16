package hms.dao;

import hms.config.Constants;
import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
import hms.model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public Room save(Room room) throws DatabaseException {
        String sql = "INSERT INTO rooms (room_number, room_type, capacity, "
                   + "base_price, status, floor) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, room.getRoomNumber());
            pstmt.setString(2, room.getRoomType());
            pstmt.setInt(3, room.getCapacity());
            pstmt.setDouble(4, room.getBasePrice());
            pstmt.setString(5, room.getStatus());
            pstmt.setInt(6, room.getFloor());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save room");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Room(
                            generatedKeys.getInt(1),
                            room.getRoomNumber(),
                            room.getRoomType(),
                            room.getCapacity(),
                            room.getBasePrice(),
                            room.getStatus(),
                            room.getFloor(),
                            room.getCreatedAt()
                    );
                }
                throw new DatabaseException("Failed to retrieve generated room ID");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save room: " + e.getMessage(), e);
        }
    }

    public Room getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoom(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve room: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Room> getAll() throws DatabaseException {
        String sql = "SELECT * FROM rooms ORDER BY room_number";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<Room> rooms = new ArrayList<>();
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve rooms: " + e.getMessage(), e);
        }
    }

    public void update(Room room) throws DatabaseException {
        String sql = "UPDATE rooms SET room_type = ?, capacity = ?, base_price = ?, "
                   + "status = ?, floor = ? WHERE room_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, room.getRoomType());
            pstmt.setInt(2, room.getCapacity());
            pstmt.setDouble(3, room.getBasePrice());
            pstmt.setString(4, room.getStatus());
            pstmt.setInt(5, room.getFloor());
            pstmt.setInt(6, room.getRoomId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update room: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM rooms WHERE room_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete room: " + e.getMessage(), e);
        }
    }

    public List<Room> filterByStatus(String status) throws DatabaseException {
        String sql = "SELECT * FROM rooms WHERE status = ? ORDER BY room_number";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Room> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to filter rooms by status: " + e.getMessage(), e);
        }
    }

    public List<Room> filterByType(String roomType) throws DatabaseException {
        String sql = "SELECT * FROM rooms WHERE room_type = ? ORDER BY room_number";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, roomType);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Room> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to filter rooms by type: " + e.getMessage(), e);
        }
    }

    public List<Room> filterByPriceRange(double min, double max) throws DatabaseException {
        String sql = "SELECT * FROM rooms WHERE base_price BETWEEN ? AND ? ORDER BY room_number";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, min);
            pstmt.setDouble(2, max);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Room> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to filter rooms by price range: " + e.getMessage(), e);
        }
    }

    public List<Room> checkAvailability(LocalDate checkIn, LocalDate checkOut)
            throws DatabaseException {
        String sql = "SELECT * FROM rooms WHERE status = ? AND room_id NOT IN ("
                   + "SELECT room_id FROM reservations "
                   + "WHERE check_in_date < ? AND check_out_date > ? "
                   + "AND status NOT IN (?, ?)) ORDER BY room_number";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Constants.ROOM_STATUS_AVAILABLE);
            pstmt.setDate(2, java.sql.Date.valueOf(checkOut));
            pstmt.setDate(3, java.sql.Date.valueOf(checkIn));
            pstmt.setString(4, Constants.RES_STATUS_CANCELLED);
            pstmt.setString(5, Constants.RES_STATUS_CHECKED_OUT);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Room> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(mapResultSetToRoom(rs));
                }
                return rooms;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to check room availability: " + e.getMessage(), e);
        }
    }

    public boolean existsByRoomNumber(String roomNumber) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM rooms WHERE room_number = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, roomNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to check room number existence: " + e.getMessage(), e);
        }

        return false;
    }

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("room_id"),
                rs.getString("room_number"),
                rs.getString("room_type"),
                rs.getInt("capacity"),
                rs.getDouble("base_price"),
                rs.getString("status"),
                rs.getInt("floor"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
