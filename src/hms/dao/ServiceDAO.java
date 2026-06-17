package hms.dao;

import hms.database.DatabaseConnection;
import hms.exception.DatabaseException;
import hms.model.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public Service save(Service service) throws DatabaseException {
        String sql = "INSERT INTO services (service_name, service_type, price, "
                   + "description, is_available) "
                   + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getServiceType());
            pstmt.setDouble(3, service.getPrice());
            pstmt.setString(4, service.getDescription());
            pstmt.setBoolean(5, service.isAvailable());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Failed to save service");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return new Service(
                            generatedKeys.getInt(1),
                            service.getServiceName(),
                            service.getServiceType(),
                            service.getPrice(),
                            service.getDescription(),
                            service.isAvailable(),
                            service.getCreatedAt()
                    );
                }
                throw new DatabaseException("Failed to retrieve generated service ID");
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save service: " + e.getMessage(), e);
        }
    }

    public Service getById(int id) throws DatabaseException {
        String sql = "SELECT * FROM services WHERE service_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve service: " + e.getMessage(), e);
        }

        return null;
    }

    public List<Service> getAll() throws DatabaseException {
        String sql = "SELECT * FROM services ORDER BY service_name";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<Service> services = new ArrayList<>();
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
            return services;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve services: " + e.getMessage(), e);
        }
    }

    public void update(Service service) throws DatabaseException {
        String sql = "UPDATE services SET service_name = ?, service_type = ?, "
                   + "price = ?, description = ?, is_available = ? "
                   + "WHERE service_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, service.getServiceName());
            pstmt.setString(2, service.getServiceType());
            pstmt.setDouble(3, service.getPrice());
            pstmt.setString(4, service.getDescription());
            pstmt.setBoolean(5, service.isAvailable());
            pstmt.setInt(6, service.getServiceId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to update service: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM services WHERE service_id = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete service: " + e.getMessage(), e);
        }
    }

    public List<Service> filterByType(String serviceType) throws DatabaseException {
        String sql = "SELECT * FROM services WHERE service_type = ? ORDER BY service_name";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, serviceType);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Service> services = new ArrayList<>();
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
                return services;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to filter services by type: " + e.getMessage(), e);
        }
    }

    public List<Service> filterByAvailability(boolean available) throws DatabaseException {
        String sql = "SELECT * FROM services WHERE is_available = ? ORDER BY service_name";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, available);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Service> services = new ArrayList<>();
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
                return services;
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to filter services by availability: " + e.getMessage(), e);
        }
    }

    public boolean existsByServiceName(String serviceName) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM services WHERE service_name = ?";

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, serviceName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException(
                    "Failed to check service name existence: " + e.getMessage(), e);
        }

        return false;
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        return new Service(
                rs.getInt("service_id"),
                rs.getString("service_name"),
                rs.getString("service_type"),
                rs.getDouble("price"),
                rs.getString("description"),
                rs.getBoolean("is_available"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
