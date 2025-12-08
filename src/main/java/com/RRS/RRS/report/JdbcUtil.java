
package com.RRS.RRS.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcUtil {

    private static final String URL = "jdbc:postgresql://localhost:5432/restaurantbookingmvp";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Anushka@13";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
