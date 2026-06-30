package hms.dao;

import hms.config.Constants;
import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
import hms.model.Guest;
import hms.model.Reservation;
import hms.model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    private static final String SELECT_JOIN =
            "SELECT r.reservation_id, r.display_id, r.guest_id, r.room_id, "
          + "r.check_in_date, r.check_out_date, r.booking_date, "
          + "r.number_of_guests, r.status, r.total_amount, r.notes, "
          + "r.created_by_staff_id, r.created_at AS res_created_at, "
          + "g.guest_id AS g_guest_id, g.first_name, g.last_name, "
          + "g.email, g.phone, g.address, g.id_proof_type, "
          + "g.id_proof_number, g.date_of_birth, g.guest_type, g.nationality, "
          + "g.created_at AS g_created_at, "
          + "rm.room_id AS rm_room_id, rm.room_number, rm.room_type, "
          + "rm.capacity, rm.base_price, rm.status AS rm_status, "
          + "rm.floor, rm.created_at AS rm_created_at "
          + "FROM reservations r "
          + "JOIN guests g ON r.guest_id = g.guest_id "
          + "JOIN rooms rm ON r.room_id = rm.room_id ";

    public Reservation save(Reservation reservation) throws DatabaseException {
        String sql = "INSERT INTO reservations (guest_id, room_id, check_in_date, "
                   + "check_out_date, booking_date, number_of_guests, status, "
                   + "total_amount, created_by_staff_id, display_id, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, reservation.getGuestId());
            pstmt.setInt(2, reservation.getRoomId());
            pstmt.setDate(3, java.sql.Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(4, java.sql.Date.valueOf(reservation.getCheckOutDate()));
            pstmt.setTimestamp(5, java.sql.Timestamp.valueOf(reservation.getBookingDate()));
            pstmt.setInt(6, reservation.getNumberOfGuests());
            pstmt.setString(7, reservation.getStatus());
            pstmt.setDouble(8, reservation.getTotalAmount());
            pstmt.setObject(9, reservation.getCreatedByStaffId());
            pstmt.setString(10, reservation.getDisplayId());
            pstmt.setString(11, reservation.getNotes());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save reservation");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Reservation(
                            generatedKeys.getInt(1),
                            reservation.getDisplayId(),
                            reservation.getGuest(),
                            reservation.getRoom(),
                            reservation.getCheckInDate(),
                            reservation.getCheckOutDate(),
                            reservation.getBookingDate(),
                            reservation.getNumberOfGuests(),
                            reservation.getStatus(),
                            reservation.getTotalAmount(),
                            reservation.getCreatedByStaffId(),
                            reservation.getCreatedAt(),
                            reservation.getNotes()
                    );
                }
                throw new DatabaseException("Failed to retrieve generated reservation ID");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save reservation: " + e.getMessage(), e);
        }
    }

    public Reservation getById(int id) throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE r.reservation_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve reservation: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Reservation> getAll() throws DatabaseException {
        String sql = SELECT_JOIN + "ORDER BY r.booking_date DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<Reservation> reservations = new ArrayList<>();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
            return reservations;

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve reservations: " + e.getMessage(), e);
        }
    }

    public void update(Reservation reservation) throws DatabaseException {
        String sql = "UPDATE reservations SET room_id = ?, check_in_date = ?, "
                   + "check_out_date = ?, number_of_guests = ?, status = ?, "
                   + "total_amount = ?, created_by_staff_id = ?, notes = ? "
                   + "WHERE reservation_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservation.getRoomId());
            pstmt.setDate(2, java.sql.Date.valueOf(reservation.getCheckInDate()));
            pstmt.setDate(3, java.sql.Date.valueOf(reservation.getCheckOutDate()));
            pstmt.setInt(4, reservation.getNumberOfGuests());
            pstmt.setString(5, reservation.getStatus());
            pstmt.setDouble(6, reservation.getTotalAmount());
            pstmt.setObject(7, reservation.getCreatedByStaffId());
            pstmt.setString(8, reservation.getNotes());
            pstmt.setInt(9, reservation.getReservationId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to update reservation: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to delete reservation: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getByGuestId(int guestId) throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE r.guest_id = ? ORDER BY r.check_in_date DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, guestId);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Reservation> reservations = new ArrayList<>();
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
                return reservations;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve reservations by guest: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getByRoomId(int roomId) throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE r.room_id = ? ORDER BY r.check_in_date DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Reservation> reservations = new ArrayList<>();
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
                return reservations;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve reservations by room: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getByDateRange(LocalDate start, LocalDate end)
            throws DatabaseException {
        String sql = SELECT_JOIN
                   + "WHERE (r.check_in_date BETWEEN ? AND ? "
                   + "OR r.check_out_date BETWEEN ? AND ?) "
                   + "ORDER BY r.check_in_date";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(start));
            pstmt.setDate(2, java.sql.Date.valueOf(end));
            pstmt.setDate(3, java.sql.Date.valueOf(start));
            pstmt.setDate(4, java.sql.Date.valueOf(end));

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Reservation> reservations = new ArrayList<>();
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
                return reservations;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve reservations by date range: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getByStatus(String status) throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE r.status = ? ORDER BY r.booking_date DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Reservation> reservations = new ArrayList<>();
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
                return reservations;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve reservations by status: " + e.getMessage(), e);
        }
    }

    public void updateStatus(int reservationId, String status) throws DatabaseException {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, reservationId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to update reservation status: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getTodayCheckIns() throws DatabaseException {
        String sql = SELECT_JOIN
                   + "WHERE r.check_in_date = CURDATE() "
                   + "AND r.status IN (?, ?) "
                   + "ORDER BY r.check_in_date";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Constants.RES_STATUS_CONFIRMED);
            pstmt.setString(2, Constants.RES_STATUS_CHECKED_IN);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Reservation> reservations = new ArrayList<>();
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
                return reservations;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve today's check-ins: " + e.getMessage(), e);
        }
    }

    public List<Reservation> getTodayCheckOuts() throws DatabaseException {
        String sql = SELECT_JOIN
                   + "WHERE r.check_out_date = CURDATE() "
                   + "AND r.status = ? "
                   + "ORDER BY r.check_out_date";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Constants.RES_STATUS_CHECKED_IN);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Reservation> reservations = new ArrayList<>();
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
                return reservations;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve today's check-outs: " + e.getMessage(), e);
        }
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        java.sql.Date dobSql = rs.getDate("date_of_birth");
        LocalDate dateOfBirth = dobSql != null ? dobSql.toLocalDate() : null;

        Guest guest = new Guest(
                rs.getInt("g_guest_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("id_proof_type"),
                rs.getString("id_proof_number"),
                dateOfBirth,
                rs.getString("guest_type"),
                rs.getString("nationality"),
                rs.getTimestamp("g_created_at").toLocalDateTime()
        );

        Room room = new Room(
                rs.getInt("rm_room_id"),
                rs.getString("room_number"),
                rs.getString("room_type"),
                rs.getInt("capacity"),
                rs.getDouble("base_price"),
                rs.getString("rm_status"),
                rs.getInt("floor"),
                rs.getTimestamp("rm_created_at").toLocalDateTime()
        );

        return new Reservation(
                rs.getInt("reservation_id"),
                rs.getString("display_id"),
                guest,
                room,
                rs.getDate("check_in_date").toLocalDate(),
                rs.getDate("check_out_date").toLocalDate(),
                rs.getTimestamp("booking_date").toLocalDateTime(),
                rs.getInt("number_of_guests"),
                rs.getString("status"),
                rs.getDouble("total_amount"),
                rs.getObject("created_by_staff_id", Integer.class),
                rs.getTimestamp("res_created_at").toLocalDateTime(),
                rs.getString("notes")
        );
    }
}
