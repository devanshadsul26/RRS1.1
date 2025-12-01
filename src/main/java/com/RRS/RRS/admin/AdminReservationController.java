package com.RRS.RRS.admin;

import com.RRS.RRS.reservation.Reservation;
import com.RRS.RRS.reservation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reservations")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<?> confirm(@PathVariable Integer id,
                                     @RequestParam Integer employeeId) {
        Reservation r = reservationService.confirmReservation(id, employeeId);
        return ResponseEntity.ok("Reservation " + r.getId() + " confirmed by employee " +
                r.getEmployee().getId());
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Integer id,
                                    @RequestParam Integer employeeId) {
        Reservation r = reservationService.adminCancelReservation(id, employeeId);
        return ResponseEntity.ok("Reservation " + r.getId() + " cancelled by employee " +
                r.getEmployee().getId());
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> list = reservationService.getAllReservations();
        return ResponseEntity.ok(list);
    }

}
