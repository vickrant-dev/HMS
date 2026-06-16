package hms.dao;

import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
import hms.model.Room;
import hms.model.RoomAssignment;
import hms.model.Staff;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomAssignmentDAO {

    private static final String SELECT_JOIN =
            "SELECT ra.assignment_id, ra.room_id, ra.staff_id, "
          + "ra.assignment_date, ra.assignment_type, ra.status, ra.notes, "
          + "rm.room_id AS rm_room_id, rm.room_number, rm.room_type, "
          + "rm.capacity, rm.base_price, rm.status AS rm_status, "
          + "rm.floor, rm.created_at AS rm_created_at, "
          + "st.staff_id AS st_staff_id, st.first_name, st.last_name, "
          + "st.email, st.phone, st.position, st.department, "
          + "st.salary, st.joining_date, st.status AS st_status, "
          + "st.password_hash, st.created_at AS st_created_at "
          + "FROM room_assignments ra "
          + "JOIN rooms rm ON ra.room_id = rm.room_id "
          + "JOIN staff st ON ra.staff_id = st.staff_id ";

    public RoomAssignment save(RoomAssignment assignment) throws DatabaseException {
        String sql = "INSERT INTO room_assignments (room_id, staff_id, "
                   + "assignment_date, assignment_type, status, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, assignment.getRoomId());
            pstmt.setInt(2, assignment.getStaffId());
            pstmt.setDate(3, java.sql.Date.valueOf(assignment.getAssignmentDate()));
            pstmt.setString(4, assignment.getAssignmentType());
            pstmt.setString(5, assignment.getStatus());
            pstmt.setString(6, assignment.getNotes());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save room assignment");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new RoomAssignment(
                            generatedKeys.getInt(1),
                            assignment.getRoom(),
                            assignment.getStaff(),
                            assignment.getAssignmentDate(),
                            assignment.getAssignmentType(),
                            assignment.getStatus(),
                            assignment.getNotes()
                    );
                }
                throw new DatabaseException(
                        "Failed to retrieve generated assignment ID");
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to save room assignment: " + e.getMessage(), e);
        }
    }

    public RoomAssignment getById(int id) throws DatabaseException {
        String sql = SELECT_JOIN + "WHERE ra.assignment_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoomAssignment(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve room assignment: " + e.getMessage(), e);
        }

        return null;
    }

    public List<RoomAssignment> getAll() throws DatabaseException {
        String sql = SELECT_JOIN + "ORDER BY ra.assignment_date DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<RoomAssignment> assignments = new ArrayList<>();
            while (rs.next()) {
                assignments.add(mapResultSetToRoomAssignment(rs));
            }
            return assignments;

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve room assignments: " + e.getMessage(), e);
        }
    }

    public void update(RoomAssignment assignment) throws DatabaseException {
        String sql = "UPDATE room_assignments SET assignment_type = ?, "
                   + "status = ?, notes = ? WHERE assignment_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, assignment.getAssignmentType());
            pstmt.setString(2, assignment.getStatus());
            pstmt.setString(3, assignment.getNotes());
            pstmt.setInt(4, assignment.getAssignmentId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to update room assignment: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM room_assignments WHERE assignment_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to delete room assignment: " + e.getMessage(), e);
        }
    }

    public List<RoomAssignment> getByRoomId(int roomId) throws DatabaseException {
        String sql = SELECT_JOIN
                   + "WHERE ra.room_id = ? ORDER BY ra.assignment_date DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<RoomAssignment> assignments = new ArrayList<>();
                while (rs.next()) {
                    assignments.add(mapResultSetToRoomAssignment(rs));
                }
                return assignments;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve assignments by room: " + e.getMessage(), e);
        }
    }

    public List<RoomAssignment> getByStaffId(int staffId) throws DatabaseException {
        String sql = SELECT_JOIN
                   + "WHERE ra.staff_id = ? ORDER BY ra.assignment_date DESC";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, staffId);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<RoomAssignment> assignments = new ArrayList<>();
                while (rs.next()) {
                    assignments.add(mapResultSetToRoomAssignment(rs));
                }
                return assignments;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve assignments by staff: " + e.getMessage(), e);
        }
    }

    public void updateStatus(int assignmentId, String status) throws DatabaseException {
        String sql = "UPDATE room_assignments SET status = ? WHERE assignment_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, assignmentId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to update assignment status: " + e.getMessage(), e);
        }
    }

    public List<RoomAssignment> getByDate(LocalDate date) throws DatabaseException {
        String sql = SELECT_JOIN
                   + "WHERE ra.assignment_date = ? ORDER BY ra.assignment_date";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, java.sql.Date.valueOf(date));

            try (ResultSet rs = pstmt.executeQuery()) {
                List<RoomAssignment> assignments = new ArrayList<>();
                while (rs.next()) {
                    assignments.add(mapResultSetToRoomAssignment(rs));
                }
                return assignments;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to retrieve assignments by date: " + e.getMessage(), e);
        }
    }

    private RoomAssignment mapResultSetToRoomAssignment(ResultSet rs)
            throws SQLException {
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

        java.sql.Date joinSql = rs.getDate("joining_date");
        LocalDate joiningDate = joinSql != null ? joinSql.toLocalDate() : null;

        Staff staff = new Staff(
                rs.getInt("st_staff_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("position"),
                rs.getString("department"),
                rs.getObject("salary", Double.class),
                joiningDate,
                rs.getString("st_status"),
                rs.getString("password_hash"),
                rs.getTimestamp("st_created_at").toLocalDateTime()
        );

        return new RoomAssignment(
                rs.getInt("assignment_id"),
                room,
                staff,
                rs.getDate("assignment_date").toLocalDate(),
                rs.getString("assignment_type"),
                rs.getString("status"),
                rs.getString("notes")
        );
    }
}
