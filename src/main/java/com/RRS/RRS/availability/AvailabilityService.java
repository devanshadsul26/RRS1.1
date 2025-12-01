package com.RRS.RRS.availability;

import com.RRS.RRS.reservation.Reservation;
import com.RRS.RRS.reservation.ReservationRepository;
import com.RRS.RRS.table.TableEntity;
import com.RRS.RRS.table.TableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AvailabilityService {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public AvailabilityService(TableRepository tableRepository,
                               ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    public boolean isAvailable(LocalDate date,
                               LocalTime time,
                               int guestCount,
                               String locationPreference) {

        List<TableEntity> candidateTables = tableRepository.findAll().stream()
                .filter(t -> Boolean.TRUE.equals(t.getIsActive()))
                .filter(t -> locationPreference == null
                        || locationPreference.isBlank()
                        || locationPreference.equalsIgnoreCase(t.getLocationPreference()))
                .filter(t -> t.getSeatingCapacity() != null
                        && t.getSeatingCapacity() >= guestCount)
                .toList();

        if (candidateTables.isEmpty()) {
            return false;
        }

        List<Reservation> sameSlot = reservationRepository
                .findByReservationDateAndReservationTime(date, time);

        return candidateTables.stream().anyMatch(t ->
                sameSlot.stream().noneMatch(r ->
                        r.getTable() != null &&
                                r.getTable().getId().equals(t.getId()) &&
                                !"Cancelled".equalsIgnoreCase(r.getStatus()))
        );
    }
}
