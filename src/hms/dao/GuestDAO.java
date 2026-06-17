package hms.dao;

import hms.config.Constants;
import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
import hms.model.Guest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    public Guest save(Guest guest) throws DatabaseException {
        String sql = "INSERT INTO guests (first_name, last_name, email, phone, "
                   + "address, id_proof_type, id_proof_number, date_of_birth) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getEmail());
            pstmt.setString(4, guest.getPhone());
            pstmt.setString(5, guest.getAddress());
            pstmt.setString(6, guest.getIdProofType());
            pstmt.setString(7, guest.getIdProofNumber());
            pstmt.setDate(8, guest.getDateOfBirth() != null
                    ? java.sql.Date.valueOf(guest.getDateOfBirth())
                    : null);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save guest");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Guest(
                            generatedKeys.getInt(1),
                            guest.getFirstName(),
                            guest.getLastName(),
                            guest.getEmail(),
                            guest.getPhone(),
                            guest.getAddress(),
                            guest.getIdProofType(),
                            guest.getIdProofNumber(),
                            guest.getDateOfBirth(),
                            guest.getCreatedAt()
                    );
                }
                throw new DatabaseException("Failed to retrieve generated guest ID");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save guest: " + e.getMessage(), e);
        }
    }

    public Guest getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM guests WHERE guest_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve guest: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Guest> getAll() throws DatabaseException {
        String sql = "SELECT * FROM guests ORDER BY first_name, last_name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<Guest> guests = new ArrayList<>();
            while (rs.next()) {
                guests.add(mapResultSetToGuest(rs));
            }
            return guests;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve guests: " + e.getMessage(), e);
        }
    }

    public void update(Guest guest) throws DatabaseException {
        String sql = "UPDATE guests SET first_name = ?, last_name = ?, phone = ?, "
                   + "address = ?, id_proof_type = ?, id_proof_number = ?, "
                   + "date_of_birth = ? WHERE guest_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, guest.getFirstName());
            pstmt.setString(2, guest.getLastName());
            pstmt.setString(3, guest.getPhone());
            pstmt.setString(4, guest.getAddress());
            pstmt.setString(5, guest.getIdProofType());
            pstmt.setString(6, guest.getIdProofNumber());
            pstmt.setDate(7, guest.getDateOfBirth() != null
                    ? java.sql.Date.valueOf(guest.getDateOfBirth())
                    : null);
            pstmt.setInt(8, guest.getGuestId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update guest: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM guests WHERE guest_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete guest: " + e.getMessage(), e);
        }
    }

    public List<Guest> searchByName(String name) throws DatabaseException {
        String sql = "SELECT * FROM guests WHERE first_name LIKE ? OR last_name LIKE ? "
                   + "ORDER BY first_name, last_name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String pattern = "%" + name + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Guest> guests = new ArrayList<>();
                while (rs.next()) {
                    guests.add(mapResultSetToGuest(rs));
                }
                return guests;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to search guests by name: " + e.getMessage(), e);
        }
    }

    public Guest searchByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM guests WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToGuest(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to search guest by email: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Guest> searchByPhone(String phone) throws DatabaseException {
        String sql = "SELECT * FROM guests WHERE phone LIKE ? ORDER BY first_name, last_name";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + phone + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Guest> guests = new ArrayList<>();
                while (rs.next()) {
                    guests.add(mapResultSetToGuest(rs));
                }
                return guests;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to search guests by phone: " + e.getMessage(), e);
        }
    }

    public boolean existsByEmail(String email) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM guests WHERE email = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to check email existence: " + e.getMessage(), e);
        }

        return false;
    }

    private Guest mapResultSetToGuest(ResultSet rs) throws SQLException {
        // Converting legacy date format to modern date format that is apparently
        // more cleaner and represents a pure date (YYYY-MM-DD)
        java.sql.Date dobSql = rs.getDate("date_of_birth");
        LocalDate dateOfBirth = dobSql != null ? dobSql.toLocalDate() : null;

        return new Guest(
                rs.getInt("guest_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("id_proof_type"),
                rs.getString("id_proof_number"),
                dateOfBirth,
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
