package com.javaweb.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    public static Connection getConnection() throws SQLException {
        Connection c = null;
        try{
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String url = "jdbc:mySQL://localhost:3306/estatebasic";
            String username = "root";
            String password = "Dovancong2004@";
            c = DriverManager.getConnection(url, username, password);
        }
        catch (Exception e){
            e.printStackTrace();
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
}
