package com.RRS.RRS.admin;

import com.RRS.RRS.audit.AuditLog;
import com.RRS.RRS.audit.AuditLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminAuditController {

    private final AuditLogRepository auditLogRepository;

    public AdminAuditController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping("/audit-log")
    public ResponseEntity<List<AuditLog>> getAuditLog() {
        List<AuditLog> logs = auditLogRepository.findAll();
        return ResponseEntity.ok(logs);
    }

    @DeleteMapping("/audit-log/{id}")
    public ResponseEntity<?> deleteAuditLog(@PathVariable int id) {
        if (!auditLogRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        auditLogRepository.deleteById(id);
        return ResponseEntity.ok("Audit log deleted");
    }

}
