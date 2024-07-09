package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface waitingRepository extends JpaRepository<Waiting, Integer> {
}
