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
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String url = "jdbc:mySQL://localhost:3306/estatebasic";
            String username = "root";
            String password = "Dovancong2004@";
            c = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connected to database successfully!");
        }
        catch (Exception e){
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.out.println("💡 Using mock data instead...");
            // Không throw exception, chỉ trả về null để dùng mock data
            return null;
        }
        return c;
    }
    
    public static void closeConnection(Connection c){
        try {
            if (c!= null){
                c.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Method để bật database connection khi cần
//    public static void enableDatabase() {
//        System.out.println("Database connection enabled. Make sure your database is running.");
//    }
}
