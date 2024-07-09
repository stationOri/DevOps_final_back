package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface reservationRepository extends JpaRepository<Reservation, Integer> {
}
