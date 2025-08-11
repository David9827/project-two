/*
package com.javaweb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

    // Flag để bật/tắt database connection
    private static final boolean DATABASE_ENABLED = true;

    public static Connection getConnection() throws SQLException {
        if (!DATABASE_ENABLED) {
            // Trả về null khi database bị tắt - service sẽ dùng mock data
            return null;
        }

        Connection c = null;
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String url = "jdbc:mySQL://localhost:3306/estatebasic";
            String username = "root";
            String password = "Dovancong2004@";
            c = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connected to database successfully!");
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            // Không throw exception, chỉ trả về null để dùng mock data
            return null;
        }
        return c;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
package com.javaweb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JDBCUtil {

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/estatebasic2";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Dovancong2004@";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";

    // Connection pool settings
    private static final String CONNECTION_PARAMS =
            "?useSSL=false" +
                    "&allowPublicKeyRetrieval=true" +
                    "&useUnicode=true" +
                    "&characterEncoding=UTF-8" +
                    "&serverTimezone=Asia/Ho_Chi_Minh" +
                    "&autoReconnect=true" +
                    "&failOverReadOnly=false" +
                    "&maxReconnects=3";

    public static Connection getConnection() throws SQLException {
        Connection connection = null;

        try {
            // Register MySQL JDBC driver
            Class.forName(DRIVER_CLASS);

            // Create connection with full URL and parameters
            String fullUrl = DB_URL + CONNECTION_PARAMS;
            connection = DriverManager.getConnection(fullUrl, DB_USERNAME, DB_PASSWORD);

            // Test connection
            if (connection != null && !connection.isClosed()) {
                System.out.println("✅ Database connected successfully!");

                // Set connection properties for better performance
                connection.setAutoCommit(true);
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

                return connection;
            } else {
                throw new SQLException("Connection is null or closed");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found: " + e.getMessage());
            throw new SQLException("MySQL JDBC Driver not found", e);

        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.err.println("📋 Connection details:");
            System.err.println("   - URL: " + DB_URL);
            System.err.println("   - Username: " + DB_USERNAME);
            System.err.println("   - Driver: " + DRIVER_CLASS);

            // Common error messages and solutions
            if (e.getMessage().contains("Access denied")) {
                System.err.println("💡 Solution: Check username and password");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("💡 Solution: Create database 'estatebasic' first");
            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("💡 Solution: Check if MySQL server is running on port 3306");
            } else if (e.getMessage().contains("No suitable driver")) {
                System.err.println("💡 Solution: Add MySQL JDBC driver to classpath");
            }

            throw e;
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Unexpected database error", e);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("🔒 Database connection closed successfully");
                }
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Utility method to test connection
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            System.err.println("❌ Connection test failed: " + e.getMessage());
            return false;
        }
    }

    // Get connection info for debugging
    public static Map<String, String> getConnectionInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("url", DB_URL);
        info.put("username", DB_USERNAME);
        info.put("driver", DRIVER_CLASS);
        info.put("params", CONNECTION_PARAMS);
        return info;
    }
}
