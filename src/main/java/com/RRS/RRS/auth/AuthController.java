package com.RRS.RRS.auth;

import com.RRS.RRS.user.Customer;
import com.RRS.RRS.user.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authService;

    public AuthController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Customer c = authService.registerCustomer(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone()
        );
        return ResponseEntity.ok("Registered customer with id " + c.getId());
    }

    @PostMapping("/login/customer")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest request) {
        Customer c = authService.loginCustomer(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(
                java.util.Map.of(
                        "customerId", c.getId(),
                        "message", "Customer login ok"
                )
        );
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> loginAdmin(@RequestBody LoginRequest request) {
        Employee e = authService.loginAdmin(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(
                java.util.Map.of(
                        "employeeId", e.getId(),
                        "message", "Admin login ok"
                )
        );
    }
}
