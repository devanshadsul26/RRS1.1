// src/main/java/com/RRS/RRS/report/ReservationReportController.java
package com.RRS.RRS.report;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationReportController {

    private final ReservationReportService service = new ReservationReportService();


    @GetMapping("/api/report/reservations-by-date")
    public List<ReservationReportRow> getReservationsByDate(
            @RequestParam("date") String dateString) {
        return service.findReservationsByDate(dateString);
    }

}
