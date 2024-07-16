package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
  int countByRestaurantAndResDatetimeBetween(Restaurant restaurant, Timestamp resDatetime, Timestamp resDatetime2);
}
