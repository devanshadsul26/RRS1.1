package com.RRS.RRS.reservation;

import com.RRS.RRS.user.Customer;
import com.RRS.RRS.user.Employee;
import com.RRS.RRS.table.TableEntity;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservationid")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customerid", nullable = false)
    private Customer customer;

    @Column(name = "reservationdate", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "reservationtime", nullable = false)
    private LocalTime reservationTime;

    @Column(name = "guestcount", nullable = false)
    private Integer guestCount;

    @Column(name = "locationpreference")
    private String locationPreference;

    @Column(name = "specialrequests")
    private String specialRequests;

    @Column(name = "status")
    private String status;   // Pending, Confirmed, Cancelled

    @ManyToOne
    @JoinColumn(name = "tableid")
    private TableEntity table;

    @ManyToOne
    @JoinColumn(name = "employeeid")
    private Employee employee;  // who confirmed/cancelled

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;

    @Column(name = "confirmedat")
    private LocalDateTime confirmedAt;

    @Column(name = "cancelledat")
    private LocalDateTime cancelledAt;

    // Getters and setters

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }
    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public Integer getGuestCount() {
        return guestCount;
    }
    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getLocationPreference() {
        return locationPreference;
    }
    public void setLocationPreference(String locationPreference) {
        this.locationPreference = locationPreference;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public TableEntity getTable() {
        return table;
    }
    public void setTable(TableEntity table) {
        this.table = table;
    }

    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }
    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
