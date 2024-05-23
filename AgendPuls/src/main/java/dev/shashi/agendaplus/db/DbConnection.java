package dev.shashi.agendaplus.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection dbConnection;
    private static Connection connection;

    private DbConnection() throws SQLException {

        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/agendapro",
                "root", // Username
                "Ijse@123" // Password
        );
    }


    public static DbConnection getInstance() throws SQLException {
        if (dbConnection == null) {
            synchronized (DbConnection.class) {
                if (dbConnection == null) {
                    dbConnection = new DbConnection();
                }
            }
        }
        return dbConnection;
    }

    public Connection getConnection() throws SQLException {

        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/agendapro",
                    "root", // Username
                    "Ijse@123" // Password
            );
        }
        return connection;
    }
}