// src/main/java/com/RRS/RRS/report/ReservationReportService.java
package com.RRS.RRS.report;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

public class ReservationReportService {

    // src/main/java/com/RRS/RRS/report/ReservationReportService.java
    public List<ReservationReportRow> findReservationsByDate(String dateString) {
        List<ReservationReportRow> list = new ArrayList<>();

        String sql = """
        SELECT r.reservationid, r.reservationdate, r.reservationtime, r.guestcount,
               c.name AS customer_name, c.email AS customer_email
        FROM reservations r
        JOIN customers c ON r.customerid = c.customerid
        WHERE r.reservationdate = ?
        ORDER BY r.reservationtime
        """;

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            LocalDate date = LocalDate.parse(dateString);          // expects yyyy-MM-dd
            ps.setDate(1, java.sql.Date.valueOf(date));           // pass date to SQL [web:14][web:17]

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservationReportRow row = new ReservationReportRow();
                    row.setReservationId(rs.getInt("reservationid"));
                    row.setReservationDate(rs.getDate("reservationdate").toLocalDate());
                    row.setReservationTime(rs.getTime("reservationtime").toLocalTime());
                    row.setGuestCount(rs.getInt("guestcount"));
                    row.setCustomerName(rs.getString("customer_name"));
                    row.setCustomerEmail(rs.getString("customer_email"));
                    list.add(row);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
