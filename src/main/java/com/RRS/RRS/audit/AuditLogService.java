package com.RRS.RRS.audit;

import com.RRS.RRS.reservation.Reservation;
import com.RRS.RRS.user.Customer;
import com.RRS.RRS.user.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logReservationAction(String actionType,
                                     Reservation reservation,
                                     Customer customer,
                                     Employee employee,
                                     String details) {
        AuditLog log = new AuditLog();
        log.setActionType(actionType);
        log.setReservation(reservation);
        log.setCustomer(customer);
        log.setEmployee(employee);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
