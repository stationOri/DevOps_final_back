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


  @Query("SELECT r " +
          "FROM Reservation r " +
          "WHERE r.restaurant.restId = :restId " +
          "AND r.resDatetime BETWEEN :startTimestamp AND :endTimestamp")
  List<Reservation> findReservationsByDateRange(
          @Param("restId") int restId,
          @Param("startTimestamp") Timestamp startTimestamp,
          @Param("endTimestamp") Timestamp endTimestamp
  );


  // 많이 방문한 식당 조회
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

  // 사용자 예약 내역 조회
  @Query("SELECT r FROM Reservation r WHERE r.user.userId = :userId AND r.resDatetime >= :oneYearAgo ORDER BY r.resDatetime DESC")
  List<Reservation> findRecentReservationsByUserId(@Param("userId") int userId, @Param("oneYearAgo") Timestamp oneYearAgo);

  // 사용자가 예약/방문한 식당 수 조회
  @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.userId = :userId AND r.status IN (:statuses) AND r.resDatetime < CURRENT_DATE")
  int countNowReservations(@Param("userId") int userId, @Param("statuses") List<ReservationStatus> statuses);

  @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.userId = :userId AND r.status = :status")
  int countPastReservations(@Param("userId") int userId, @Param("status") ReservationStatus status);

  @Query("SELECT COUNT(DISTINCT r.restaurant.restId) FROM Reservation r WHERE r.user.userId = :userId AND r.status = :status")
  int countVisitedRestaurants(@Param("userId") int userId, @Param("status") ReservationStatus status);

}
