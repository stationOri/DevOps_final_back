package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
  int countByRestaurantAndResDatetimeBetweenAndStatusIn(Restaurant restaurant, Timestamp startTimestamp, Timestamp endTimestamp, List<ReservationStatus> statuses);
}
