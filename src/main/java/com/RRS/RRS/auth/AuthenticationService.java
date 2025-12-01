package com.RRS.RRS.auth;

import com.RRS.RRS.user.Customer;
import com.RRS.RRS.user.CustomerRepository;
import com.RRS.RRS.user.Employee;
import com.RRS.RRS.user.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(CustomerRepository customerRepository,
                                 EmployeeRepository employeeRepository,
                                 PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer registerCustomer(String name, String email, String rawPassword, String phone) {
        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        Customer c = new Customer();
        c.setName(name);
        c.setEmail(email);
        c.setPasswordHash(passwordEncoder.encode(rawPassword));
        c.setPhoneNumber(phone);
        c.setCreatedAt(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        c.setIsActive(true);

        return customerRepository.save(c);
    }

    public Customer loginCustomer(String email, String rawPassword) {
        Customer c = customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(rawPassword, c.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return c;
    }

    public Employee loginAdmin(String email, String rawPassword) {
        Employee e = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(rawPassword, e.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (!"Admin".equalsIgnoreCase(e.getRole())) {
            throw new IllegalArgumentException("Not an admin");
        }
        return e;
    }
}
