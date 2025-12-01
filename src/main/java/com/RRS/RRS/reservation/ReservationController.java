package com.RRS.RRS.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create")
    public ResponseEntity<ReservationResponse> create(@RequestBody CreateReservationRequest request) {
        Reservation r = reservationService.createReservation(request);

        ReservationResponse resp = new ReservationResponse();
        resp.setReservationId(r.getId());
        resp.setStatus(r.getStatus());
        resp.setMessage("Reservation created");

        return ResponseEntity.ok(resp);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> listByCustomer(@RequestParam Integer customerId) {
        List<Reservation> list = reservationService.getReservationsForCustomer(customerId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable Integer id) {
        Reservation r = reservationService.cancelReservation(id);
        return ResponseEntity.ok("Cancelled reservation " + r.getId());
    }


}
