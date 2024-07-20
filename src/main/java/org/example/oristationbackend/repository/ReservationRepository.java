package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
  int countByRestaurantAndResDatetimeAndStatusIn(Restaurant restaurant, Timestamp startTimestamp, List<ReservationStatus> statuses);

  @Query("SELECT res.restaurant, COUNT(res) AS revCount " +
      "FROM Reservation res " +
      "WHERE res.user.userId = :userId AND res.resDatetime BETWEEN :startDate AND :endDate " +
      "AND res.status = :status " +
      "GROUP BY res.restaurant " +
      "ORDER BY revCount DESC")
  List<Object[]> findMostReservedRestaurantsByUser(@Param("userId") int userId,
                                                   @Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate,
                                                   @Param("status") ReservationStatus status);
}
