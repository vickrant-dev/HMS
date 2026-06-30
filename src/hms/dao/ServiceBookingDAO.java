package hms.dao;

import hms.config.Constants;
import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
import hms.model.Guest;
import hms.model.Reservation;
import hms.model.Room;
import hms.model.Service;
import hms.model.ServiceBooking;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceBookingDAO {

    private static final String SELECT_JOIN =
            "SELECT sb.service_booking_id, sb.reservation_id, sb.service_id, "
          + "sb.booking_date, sb.quantity, sb.total_price, sb.status, "
           + "r.reservation_id, r.display_id, r.guest_id, r.room_id, "
          + "r.check_in_date, r.check_out_date, r.booking_date, "
          + "r.number_of_guests, r.status, r.total_amount, r.notes, "
          + "r.created_by_staff_id, r.created_at AS res_created_at, "
          + "g.guest_id AS g_guest_id, g.first_name, g.last_name, "
          + "g.email, g.phone, g.address, g.id_proof_type, "
          + "g.id_proof_number, g.date_of_birth, g.guest_type, g.nationality, "
          + "g.created_at AS g_created_at, "
          + "rm.room_id AS rm_room_id, rm.room_number, rm.room_type, "
          + "rm.capacity, rm.base_price, rm.status AS rm_status, "
          + "rm.floor, rm.created_at AS rm_created_at, "
          + "s.service_id AS s_service_id, s.service_name, s.service_type, "
          + "s.price, s.description, s.is_available, s.created_at AS s_created_at "
          + "FROM service_bookings sb "
          + "JOIN reservations r ON sb.reservation_id = r.reservation_id "
          + "JOIN guests g ON r.guest_id = g.guest_id "
          + "JOIN rooms rm ON r.room_id = rm.room_id "
          + "JOIN services s ON sb.service_id = s.service_id ";

    public ServiceBooking save(ServiceBooking serviceBooking) throws DatabaseException {
        String sql = "INSERT INTO service_bookings (reservation_id, service_id, "
                   + "booking_date, quantity, total_price, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, serviceBooking.getReservationId());
            pstmt.setInt(2, serviceBooking.getServiceId());
            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(serviceBooking.getBookingDate()));
            pstmt.setInt(4, serviceBooking.getQuantity());
            pstmt.setObject(5, serviceBooking.getTotalPrice());
            pstmt.setString(6, serviceBooking.getStatus());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save service booking");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new ServiceBooking(
                            generatedKeys.getInt(1),
                            serviceBooking.getReservation(),
                            serviceBooking.getService(),
                            serviceBooking.getBookingDate(),
                            serviceBooking.getQuantity(),
                            serviceBooking.getTotalPrice(),
                            serviceBooking.getStatus()
                    );
                }
                throw new DatabaseException(
                        "Failed to retrieve generated service booking ID");
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to save service booking: " + e.getMessage(), e);
        }
    }

    public ServiceBooking getById(int id) throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE sb.service_booking_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToServiceBooking(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve service booking: " + e.getMessage(), e);
        }

        return null;
    }

    public List<ServiceBooking> getAll() throws DatabaseException {
        String sql = SELECT_JOIN + "ORDER BY sb.booking_date DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<ServiceBooking> bookings = new ArrayList<>();
            while (rs.next()) {
                bookings.add(mapResultSetToServiceBooking(rs));
            }
            return bookings;

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve service bookings: " + e.getMessage(), e);
        }
    }

    public void update(ServiceBooking serviceBooking) throws DatabaseException {
        String sql = "UPDATE service_bookings SET quantity = ?, total_price = ?, "
                   + "status = ? WHERE service_booking_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, serviceBooking.getQuantity());
            pstmt.setObject(2, serviceBooking.getTotalPrice());
            pstmt.setString(3, serviceBooking.getStatus());
            pstmt.setInt(4, serviceBooking.getServiceBookingId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to update service booking: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM service_bookings WHERE service_booking_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to delete service booking: " + e.getMessage(), e);
        }
    }

    public List<ServiceBooking> getByReservationId(int reservationId)
            throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE sb.reservation_id = ? ORDER BY sb.booking_date";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<ServiceBooking> bookings = new ArrayList<>();
                while (rs.next()) {
                    bookings.add(mapResultSetToServiceBooking(rs));
                }
                return bookings;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve service bookings by reservation: "
                    + e.getMessage(), e);
        }
    }

    public double calculateServiceCharges(int reservationId) throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(total_price), 0) FROM service_bookings "
                   + "WHERE reservation_id = ? AND status != ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationId);
            pstmt.setString(2, Constants.SVC_BOOKING_CANCELLED);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to calculate service charges: " + e.getMessage(), e);
        }

        return 0.0;
    }

    private ServiceBooking mapResultSetToServiceBooking(ResultSet rs)
            throws SQLException {
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

        Reservation reservation = new Reservation(
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

        Service service = new Service(
                rs.getInt("s_service_id"),
                rs.getString("service_name"),
                rs.getString("service_type"),
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getBoolean("is_available"),
                rs.getTimestamp("s_created_at").toLocalDateTime()
        );

        return new ServiceBooking(
                rs.getInt("service_booking_id"),
                reservation,
                service,
                rs.getTimestamp("booking_date").toLocalDateTime(),
                rs.getInt("quantity"),
                rs.getObject("total_price", Double.class),
                rs.getString("status")
        );
    }
}
