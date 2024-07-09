package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Payment;
import org.example.oristationbackend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface paymentRepository extends JpaRepository<Payment, Integer> {
}
