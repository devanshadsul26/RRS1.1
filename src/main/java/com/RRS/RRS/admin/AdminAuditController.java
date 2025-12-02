package com.RRS.RRS.admin;

import com.RRS.RRS.audit.AuditLog;
import com.RRS.RRS.audit.AuditLogRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    @GetMapping("/audit-log/export")
    public ResponseEntity<?> exportAuditLog() {
        List<AuditLog> logs = auditLogRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Audit Log");

            // Header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Log ID");
            headerRow.createCell(1).setCellValue("Action");
            headerRow.createCell(2).setCellValue("Reservation ID");
            headerRow.createCell(3).setCellValue("Customer Name");
            headerRow.createCell(4).setCellValue("Customer Email");
            headerRow.createCell(5).setCellValue("Customer Phone");
            headerRow.createCell(6).setCellValue("Res Date");
            headerRow.createCell(7).setCellValue("Res Time");
            headerRow.createCell(8).setCellValue("Guests");
            headerRow.createCell(9).setCellValue("Special Requests");
            headerRow.createCell(10).setCellValue("Employee ID");
            headerRow.createCell(11).setCellValue("Details");
            headerRow.createCell(12).setCellValue("Logged At");

            int rowNum = 1;
            for (AuditLog log : logs) {
                Row row = sheet.createRow(rowNum++);

                var customer = log.getCustomer();
                var reservation = log.getReservation();
                var employee = log.getEmployee();

                row.createCell(0).setCellValue(log.getId() != null ? log.getId() : 0);
                row.createCell(1).setCellValue(log.getActionType() != null ? log.getActionType() : "");
                row.createCell(2).setCellValue(
                        reservation != null && reservation.getId() != null ? reservation.getId() : 0
                );
                row.createCell(3).setCellValue(
                        customer != null && customer.getName() != null ? customer.getName() : ""
                );
                row.createCell(4).setCellValue(
                        customer != null && customer.getEmail() != null ? customer.getEmail() : ""
                );
                row.createCell(5).setCellValue(
                        customer != null && customer.getPhoneNumber() != null ? customer.getPhoneNumber() : ""
                );
                row.createCell(6).setCellValue(
                        reservation != null && reservation.getReservationDate() != null
                                ? reservation.getReservationDate().toString() : ""
                );
                row.createCell(7).setCellValue(
                        reservation != null && reservation.getReservationTime() != null
                                ? reservation.getReservationTime().toString() : ""
                );
                row.createCell(8).setCellValue(
                        reservation != null && reservation.getGuestCount() != null
                                ? reservation.getGuestCount() : 0
                );
                row.createCell(9).setCellValue(
                        reservation != null && reservation.getSpecialRequests() != null
                                ? reservation.getSpecialRequests() : ""
                );
                row.createCell(10).setCellValue(
                        employee != null && employee.getId() != null ? employee.getId() : 0
                );
                row.createCell(11).setCellValue(log.getDetails() != null ? log.getDetails() : "");
                row.createCell(12).setCellValue(
                        log.getTimestamp() != null ? log.getTimestamp().toString() : ""
                );
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            byte[] bytes = out.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "audit_log.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating Excel file");
        }
    }


}
