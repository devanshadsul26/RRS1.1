package com.RRS.RRS.availability;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequestMapping("/api/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> check(
            @RequestParam String date,           // "2025-12-01"
            @RequestParam String time,           // "19:30"
            @RequestParam Integer guestCount,
            @RequestParam(required = false) String locationPreference) {

        boolean available = availabilityService.isAvailable(
                LocalDate.parse(date),
                LocalTime.parse(time),
                guestCount,
                locationPreference
        );

        return ResponseEntity.ok(
                Map.of("available", available)
        );
    }
}
