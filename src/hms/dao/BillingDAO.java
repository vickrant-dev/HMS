package hms.dao;

import hms.config.Constants;
import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
import hms.model.Billing;
import hms.model.Guest;
import hms.model.Reservation;
import hms.model.Room;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    private static final String SELECT_JOIN =
            "SELECT b.billing_id, b.reservation_id, b.room_charge, "
          + "b.service_charge, b.other_charges, b.tax_amount, "
          + "b.total_bill, b.discount_amount, b.late_charge, b.payment_status, b.payment_date, b.notes AS billing_notes, "
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
          + "rm.floor, rm.created_at AS rm_created_at "
          + "FROM billing b "
          + "JOIN reservations r ON b.reservation_id = r.reservation_id "
          + "JOIN guests g ON r.guest_id = g.guest_id "
          + "JOIN rooms rm ON r.room_id = rm.room_id ";

    public Billing save(Billing billing) throws DatabaseException {
        String sql = "INSERT INTO billing (reservation_id, room_charge, "
                   + "service_charge, other_charges, tax_amount, total_bill, "
                   + "payment_status, payment_date, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, billing.getReservationId());
            pstmt.setDouble(2, billing.getRoomCharge());
            pstmt.setDouble(3, billing.getServiceCharge());
            pstmt.setDouble(4, billing.getOtherCharges());
            pstmt.setDouble(5, billing.getTaxAmount());
            pstmt.setDouble(6, billing.getTotalBill());
            pstmt.setString(7, billing.getPaymentStatus());
            pstmt.setTimestamp(8, billing.getPaymentDate() != null
                    ? java.sql.Timestamp.valueOf(billing.getPaymentDate())
                    : null);
            pstmt.setString(9, billing.getNotes());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save billing");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Billing(
                            generatedKeys.getInt(1),
                            billing.getReservation(),
                            billing.getRoomCharge(),
                            billing.getServiceCharge(),
                            billing.getOtherCharges(),
                            billing.getTaxAmount(),
                            billing.getTotalBill(),
                            billing.getDiscountAmount(),
                            billing.getLateCharge(),
                            billing.getPaymentStatus(),
                            billing.getPaymentDate(),
                            billing.getNotes()
                    );
                }
                throw new DatabaseException("Failed to retrieve generated billing ID");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save billing: " + e.getMessage(), e);
        }
    }

    public Billing getById(int id) throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE b.billing_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBilling(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve billing: " + e.getMessage(), e);
        }

        return null;
    }

    public Billing getByReservationId(int reservationId) throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE b.reservation_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reservationId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBilling(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve billing by reservation: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Billing> getAll() throws DatabaseException {
        String sql = SELECT_JOIN + "ORDER BY b.billing_id DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<Billing> billings = new ArrayList<>();
            while (rs.next()) {
                billings.add(mapResultSetToBilling(rs));
            }
            return billings;

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve billings: " + e.getMessage(), e);
        }
    }

    public void updatePaymentStatus(int billingId, String status,
                                     LocalDateTime paymentDate)
            throws DatabaseException {
        String sql = "UPDATE billing SET payment_status = ?, payment_date = ? "
                   + "WHERE billing_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setTimestamp(2, paymentDate != null
                    ? java.sql.Timestamp.valueOf(paymentDate)
                    : null);
            pstmt.setInt(3, billingId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to update payment status: " + e.getMessage(), e);
        }
    }

    public void updateCharges(int billingId, double otherCharges,
                                double discountAmount, double lateCharge,
                                double taxAmount, double totalBill,
                                String notes) throws DatabaseException {
        String sql = "UPDATE billing SET other_charges = ?, discount_amount = ?, "
                   + "late_charge = ?, tax_amount = ?, "
                   + "total_bill = ?, notes = ? WHERE billing_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, otherCharges);
            pstmt.setDouble(2, discountAmount);
            pstmt.setDouble(3, lateCharge);
            pstmt.setDouble(4, taxAmount);
            pstmt.setDouble(5, totalBill);
            pstmt.setString(6, notes);
            pstmt.setInt(7, billingId);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to update billing charges: " + e.getMessage(), e);
        }
    }

    public double getRevenueByDateRange(LocalDate start, LocalDate end)
            throws DatabaseException {
        String sql = "SELECT COALESCE(SUM(total_bill), 0) FROM billing "
                   + "WHERE payment_status IN (?, ?) "
                   + "AND payment_date BETWEEN ? AND ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Constants.PAYMENT_PAID);
            pstmt.setString(2, Constants.PAYMENT_PARTIAL);
            pstmt.setTimestamp(3, java.sql.Timestamp.valueOf(start.atStartOfDay()));
            pstmt.setTimestamp(4, java.sql.Timestamp.valueOf(end.atTime(23, 59, 59)));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to calculate revenue: " + e.getMessage(), e);
        }

        return 0.0;
    }

    private Billing mapResultSetToBilling(ResultSet rs) throws SQLException {
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

        java.sql.Timestamp payTs = rs.getTimestamp("payment_date");
        LocalDateTime paymentDate = payTs != null ? payTs.toLocalDateTime() : null;

        return new Billing(
                rs.getInt("billing_id"),
                reservation,
                rs.getDouble("room_charge"),
                rs.getDouble("service_charge"),
                rs.getDouble("other_charges"),
                rs.getDouble("tax_amount"),
                rs.getDouble("total_bill"),
                rs.getDouble("discount_amount"),
                rs.getDouble("late_charge"),
                rs.getString("payment_status"),
                paymentDate,
                rs.getString("billing_notes")
        );
    }
}
