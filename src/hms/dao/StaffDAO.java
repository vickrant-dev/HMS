package hms.dao;

import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
import hms.model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    public Staff save(Staff staff) throws DatabaseException {
        String sql = "INSERT INTO staff (first_name, last_name, email, phone, "
                   + "position, department, salary, joining_date, status, "
                   + "password_hash) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, staff.getFirstName());
            pstmt.setString(2, staff.getLastName());
            pstmt.setString(3, staff.getEmail());
            pstmt.setString(4, staff.getPhone());
            pstmt.setString(5, staff.getPosition());
            pstmt.setString(6, staff.getDepartment());
            pstmt.setObject(7, staff.getSalary());
            pstmt.setDate(8, staff.getJoiningDate() != null
                    ? java.sql.Date.valueOf(staff.getJoiningDate()) : null);
            pstmt.setString(9, staff.getStatus());
            pstmt.setString(10, staff.getPasswordHash());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save staff");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Staff(
                            generatedKeys.getInt(1),
                            staff.getFirstName(),
                            staff.getLastName(),
                            staff.getEmail(),
                            staff.getPhone(),
                            staff.getPosition(),
                            staff.getDepartment(),
                            staff.getSalary(),
                            staff.getJoiningDate(),
                            staff.getStatus(),
                            staff.getPasswordHash(),
                            staff.getCreatedAt()
                    );
                }
                throw new DatabaseException("Failed to retrieve generated staff ID");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save staff: " + e.getMessage(), e);
        }
    }

    public Staff getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM staff WHERE staff_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStaff(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve staff: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Staff> getAll() throws DatabaseException {
        String sql = "SELECT * FROM staff ORDER BY first_name, last_name";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<Staff> staffList = new ArrayList<>();
            while (rs.next()) {
                staffList.add(mapResultSetToStaff(rs));
            }
            return staffList;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve staff: " + e.getMessage(), e);
        }
    }

    public void update(Staff staff) throws DatabaseException {
        String sql = "UPDATE staff SET first_name = ?, last_name = ?, email = ?, "
                   + "phone = ?, position = ?, department = ?, salary = ?, "
                   + "joining_date = ?, status = ?, password_hash = ? "
                   + "WHERE staff_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staff.getFirstName());
            pstmt.setString(2, staff.getLastName());
            pstmt.setString(3, staff.getEmail());
            pstmt.setString(4, staff.getPhone());
            pstmt.setString(5, staff.getPosition());
            pstmt.setString(6, staff.getDepartment());
            pstmt.setObject(7, staff.getSalary());
            pstmt.setDate(8, staff.getJoiningDate() != null
                    ? java.sql.Date.valueOf(staff.getJoiningDate()) : null);
            pstmt.setString(9, staff.getStatus());
            pstmt.setString(10, staff.getPasswordHash());
            pstmt.setInt(11, staff.getStaffId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update staff: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM staff WHERE staff_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete staff: " + e.getMessage(), e);
        }
    }

    public List<Staff> filterByDepartment(String department) throws DatabaseException {
        String sql = "SELECT * FROM staff WHERE department = ? ORDER BY first_name, last_name";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, department);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Staff> staffList = new ArrayList<>();
                while (rs.next()) {
                    staffList.add(mapResultSetToStaff(rs));
                }
                return staffList;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to filter staff by department: " + e.getMessage(), e);
        }
    }

    public List<Staff> filterByPosition(String position) throws DatabaseException {
        String sql = "SELECT * FROM staff WHERE position = ? ORDER BY first_name, last_name";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, position);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Staff> staffList = new ArrayList<>();
                while (rs.next()) {
                    staffList.add(mapResultSetToStaff(rs));
                }
                return staffList;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to filter staff by position: " + e.getMessage(), e);
        }
    }

    public List<Staff> filterByStatus(String status) throws DatabaseException {
        String sql = "SELECT * FROM staff WHERE status = ? ORDER BY first_name, last_name";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Staff> staffList = new ArrayList<>();
                while (rs.next()) {
                    staffList.add(mapResultSetToStaff(rs));
                }
                return staffList;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to filter staff by status: " + e.getMessage(), e);
        }
    }

    public boolean existsByEmail(String email) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM staff WHERE email = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to check staff email existence: " + e.getMessage(), e);
        }

        return false;
    }

    private Staff mapResultSetToStaff(ResultSet rs) throws SQLException {
        java.sql.Date joinSql = rs.getDate("joining_date");
        LocalDate joiningDate = joinSql != null ? joinSql.toLocalDate() : null;

        return new Staff(
                rs.getInt("staff_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("position"),
                rs.getString("department"),
                rs.getObject("salary", Double.class),
                joiningDate,
                rs.getString("status"),
                rs.getString("password_hash"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
