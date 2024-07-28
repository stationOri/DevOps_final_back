package org.example.oristationbackend.repository;

import org.example.oristationbackend.dto.restaurant.RestReservationResDto;
import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
  int countByRestaurantAndResDatetimeAndStatusIn(Restaurant restaurant, Timestamp startTimestamp, List<ReservationStatus> statuses);
  @Query("SELECT SUM(r.payment.amount) FROM Reservation r WHERE r.restaurant.restId = :restId AND r.status = :status AND FUNCTION('MONTH', r.statusChangedDate) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', r.statusChangedDate) = FUNCTION('YEAR', CURRENT_DATE)")
  int findTotalNoshowAmountByRestId(@Param("restId") int restId, @Param("status") ReservationStatus status);
  @Query("SELECT r " +
          "FROM Reservation r " +
          "WHERE r.restaurant.restId = :restId " +
          "AND r.resDatetime BETWEEN :startTimestamp AND :endTimestamp")
  List<Reservation> findReservationsByDateRange(
          @Param("restId") int restId,
          @Param("startTimestamp") Timestamp startTimestamp,
          @Param("endTimestamp") Timestamp endTimestamp
  );
  @Query("SELECT r " +
          "FROM Reservation r " +
          "WHERE r.restaurant.restId = :restId " +
          "AND r.status = :status " + // 쿼리와 조건 사이에 공백 추가
          "AND r.resDatetime BETWEEN :startTimestamp AND :endTimestamp")
  List<Reservation> findReservationsByDateRangeAndStatus(
          @Param("restId") int restId,
          @Param("startTimestamp") Timestamp startTimestamp,
          @Param("endTimestamp") Timestamp endTimestamp,
          @Param("status") ReservationStatus status // 파라미터 사이에 쉼표 추가
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
  @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.userId = :userId AND r.status = :status")
  int countReservationsByStatus(@Param("userId") int userId, @Param("status") ReservationStatus status);

  @Query("SELECT COUNT(r) FROM Reservation r WHERE r.user.userId = :userId AND r.status = :status AND r.resDatetime < CURRENT_TIMESTAMP")
  int countPastReservations(@Param("userId") int userId, @Param("status") ReservationStatus status);

  @Query("SELECT COUNT(DISTINCT r.restaurant) FROM Reservation r WHERE r.user.userId = :userId AND r.status = :status AND r.resDatetime < CURRENT_TIMESTAMP")
  int countVisitedRestaurants(@Param("userId") int userId, @Param("status") ReservationStatus status);

  // 추천 식당 조회 쿼리 (평점 4.0 이상, 최근 3개월간 예약이 가장 많은 식당 7개)
  @Query("SELECT r.restaurant, COUNT(r) AS revCount " +
      "FROM Reservation r " +
      "WHERE r.resDatetime >= :threeMonthsAgo AND r.status = :status " +
      "AND r.restaurant.restaurantInfo.restGrade >= 4.0 " +
      "GROUP BY r.restaurant " +
      "ORDER BY revCount DESC")
  List<Object[]> findRecommendedRestaurants(@Param("threeMonthsAgo") LocalDateTime threeMonthsAgo,
                                            @Param("status") ReservationStatus status);

  // 최근 2주 동안 예약이 가장 많은 식당 조회
  @Query("SELECT r.restaurant, COUNT(r) AS reservationCount " +
      "FROM Reservation r " +
      "WHERE r.resDatetime >= :twoWeeksAgo AND r.status = :status " +
      "GROUP BY r.restaurant " +
      "ORDER BY reservationCount DESC")
  List<Object[]> findHotRestaurants(@Param("twoWeeksAgo") LocalDateTime twoWeeksAgo,
                                    @Param("status") ReservationStatus status);
  @Query("SELECT r FROM Reservation r WHERE DATE(r.resDatetime) = DATE(:today) AND r.status = :status")
  List<Reservation> findReservationsByToday(@Param("today") Timestamp today, @Param("status") ReservationStatus status);




}
