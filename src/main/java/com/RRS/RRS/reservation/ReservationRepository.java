package com.RRS.RRS.reservation;

import com.RRS.RRS.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByCustomer(Customer customer);

    List<Reservation> findByReservationDateAndReservationTime(LocalDate date, LocalTime time);

    long countByCustomerAndStatusIn(Customer customer, List<String> statuses);
}

