package hms.database;

import hms.config.Constants;
import hms.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private int retryCount = 0;

    private DatabaseConnection() {
    }


    // synchronized prevents race conditions. One thread at a given time.
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws DatabaseException {
        if (connection == null || !isConnectionValid()) {
            connectToDatabase();
        }
        return connection;
    }

    private void connectToDatabase() throws DatabaseException {
        while (retryCount < Constants.DB_MAX_RETRIES) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                String url = "jdbc:mysql://" + Constants.DB_HOST + ":" + Constants.DB_PORT
                           + "/" + Constants.DB_NAME;

                connection = DriverManager.getConnection(url,
                        Constants.DB_USER, Constants.DB_PASSWORD);

                retryCount = 0;
                return;

            } catch (ClassNotFoundException | SQLException e) {
                retryCount++;

                if (retryCount < Constants.DB_MAX_RETRIES) {
                    try {
                        // pauses current thread for DB_RETRY_DELAY_MS time before re-trying.
                        Thread.sleep(Constants.DB_RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new DatabaseException(Constants.ERROR_DB_CONNECTION + ": " + e.getMessage());
                }
            }
        }
    }

    private boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed()
                && connection.isValid(Constants.DB_CONNECTION_TIMEOUT);
        } catch (SQLException e) {
            return false;
        }
    }

    public void closeConnection() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to close connection: " + e.getMessage());
        }
    }
}
