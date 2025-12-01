package com.RRS.RRS.audit;

import com.RRS.RRS.reservation.Reservation;
import com.RRS.RRS.user.Customer;
import com.RRS.RRS.user.Employee;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditlog")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logid")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reservationid")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "employeeid")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "customerid")
    private Customer customer;

    @Column(name = "actiontype")
    private String actionType; // Created, Confirmed, Cancelled

    @Column(name = "details")
    private String details;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    // getters & setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
