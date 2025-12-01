package com.RRS.RRS.reservation;

import com.RRS.RRS.table.TableEntity;
import com.RRS.RRS.table.TableRepository;
import com.RRS.RRS.user.Customer;
import com.RRS.RRS.user.CustomerRepository;
import org.springframework.stereotype.Service;
import com.RRS.RRS.user.Employee;
import com.RRS.RRS.user.EmployeeRepository;
import com.RRS.RRS.audit.AuditLogService;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final TableRepository tableRepository;
    private final EmployeeRepository employeeRepository;
    private final AuditLogService auditLogService;



    public ReservationService(ReservationRepository reservationRepository,
                              CustomerRepository customerRepository,
                              TableRepository tableRepository,
                              EmployeeRepository employeeRepository,
                              AuditLogService auditLogService) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.tableRepository = tableRepository;
        this.employeeRepository = employeeRepository;
        this.auditLogService = auditLogService;
    }



    public Reservation createReservation(CreateReservationRequest req) {
        Customer customer = customerRepository.findById(req.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        LocalDate date = LocalDate.parse(req.getDate());
        LocalTime time = LocalTime.parse(req.getTime());

        // simple active-reservation check (max 2 active)
        long activeCount = reservationRepository.countByCustomerAndStatusIn(
                customer, List.of("Pending", "Confirmed")
        );
        if (activeCount >= 2) {
            throw new IllegalArgumentException("Maximum 2 active reservations allowed");
        }

        // 1) Get all active tables matching location and capacity
        List<TableEntity> candidateTables = tableRepository.findAll().stream()
                .filter(t -> Boolean.TRUE.equals(t.getIsActive()))
                .filter(t -> req.getLocationPreference() == null
                        || req.getLocationPreference().isBlank()
                        || req.getLocationPreference().equalsIgnoreCase(t.getLocationPreference()))
                .filter(t -> t.getSeatingCapacity() != null
                        && t.getSeatingCapacity() >= req.getGuestCount())
                .toList();

        if (candidateTables.isEmpty()) {
            throw new IllegalArgumentException("No tables match location/capacity");
        }

        // 2) Load all reservations for this date + time
        List<Reservation> sameSlot = reservationRepository
                .findByReservationDateAndReservationTime(date, time);

        // 3) Pick first candidate table that is not already used at this slot
        TableEntity freeTable = candidateTables.stream()
                .filter(t -> sameSlot.stream().noneMatch(r ->
                        r.getTable() != null &&
                                r.getTable().getId().equals(t.getId()) &&
                                !"Cancelled".equalsIgnoreCase(r.getStatus())))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No tables available for this time"));

        // 4) Build and save reservation
        Reservation r = new Reservation();
        r.setCustomer(customer);
        r.setReservationDate(date);
        r.setReservationTime(time);
        r.setGuestCount(req.getGuestCount());
        r.setLocationPreference(req.getLocationPreference());
        r.setSpecialRequests(req.getSpecialRequests());
        r.setStatus(req.getGuestCount() > 8 ? "Pending Confirmation" : "Confirmed");
        r.setTable(freeTable);
        r.setCreatedAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());

        Reservation saved = reservationRepository.save(r);

        auditLogService.logReservationAction(
                "Confirmed",
                saved,
                customer,
                null,
                "Reservation created"
        );

        return saved;


    }

    public List<Reservation> getReservationsForCustomer(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return reservationRepository.findByCustomer(customer);
    }

    public Reservation cancelReservation(Integer reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        r.setStatus("Cancelled");
        r.setCancelledAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());
        Reservation saved = reservationRepository.save(r);

        auditLogService.logReservationAction(
                "Cancelled",
                saved,
                saved.getCustomer(),
                null,
                "Customer cancelled reservation"
        );

        return saved;


    }

    public Reservation confirmReservation(Integer reservationId, Integer employeeId) {
        // 1. Load reservation
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        // Only pending reservations can be confirmed
        if (!"Pending Confirmation".equalsIgnoreCase(r.getStatus())) {
            throw new IllegalArgumentException("Reservation is not pending confirmation");
        }

        // 2. Load employee (admin)
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        LocalDate date = r.getReservationDate();
        LocalTime time = r.getReservationTime();

        // 3. Find tables that match location and capacity
        List<TableEntity> candidateTables = tableRepository.findAll().stream()
                .filter(t -> Boolean.TRUE.equals(t.getIsActive()))
                .filter(t -> r.getLocationPreference() == null
                        || r.getLocationPreference().isBlank()
                        || r.getLocationPreference().equalsIgnoreCase(t.getLocationPreference()))
                .filter(t -> t.getSeatingCapacity() != null
                        && t.getSeatingCapacity() >= r.getGuestCount())
                .toList();

        if (candidateTables.isEmpty()) {
            throw new IllegalArgumentException("No tables match location/capacity for confirmation");
        }

        // 4. Check which tables are already used at that date+time
        List<Reservation> sameSlot = reservationRepository
                .findByReservationDateAndReservationTime(date, time);

        TableEntity freeTable = candidateTables.stream()
                .filter(t -> sameSlot.stream().noneMatch(res ->
                        res.getTable() != null &&
                                res.getTable().getId().equals(t.getId()) &&
                                !"Cancelled".equalsIgnoreCase(res.getStatus())))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No tables available for this time"));

        // 5. Assign table and mark as confirmed
        r.setTable(freeTable);
        r.setStatus("Confirmed");
        r.setEmployee(e);
        r.setConfirmedAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());

        Reservation saved = reservationRepository.save(r);

        auditLogService.logReservationAction(
                "Confirmed",
                saved,
                saved.getCustomer(),
                e,
                "Admin confirmed reservation and assigned table " +
                        saved.getTable().getTableNumber()
        );

        return saved;

    }

    public Reservation adminCancelReservation(Integer reservationId, Integer employeeId) {
        // 1. Load reservation
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        // 2. Load employee
        Employee e = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        // 3. Mark as cancelled and record who did it
        r.setStatus("Cancelled");
        r.setEmployee(e);
        r.setCancelledAt(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());

        Reservation saved = reservationRepository.save(r);

        auditLogService.logReservationAction(
                "Cancelled",
                saved,
                saved.getCustomer(),
                e,
                "Admin cancelled reservation"
        );

        return saved;

    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }



}
