package org.example.oristationbackend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestAvailableResDto;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.RestaurantOpen;
import org.example.oristationbackend.entity.type.MinuteType;
import org.example.oristationbackend.entity.type.OpenDay;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.example.oristationbackend.repository.RestaurantOpenRepository;
import org.example.oristationbackend.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestAvailableResService {

  @PersistenceContext
  private EntityManager entityManager;

  private final RestaurantOpenRepository restaurantOpenRepository;
  private final RestaurantInfoRepository restaurantInfoRepository;
  private final ReservationRepository reservationRepository;

  // 특정 식당의 특정 날짜의 예약 가능 시간을 반환
  public RestAvailableResDto getAvailableTimes(int restId, LocalDate date) {
    RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restId)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));

    DayOfWeek dayOfWeek = date.getDayOfWeek();
    OpenDay openDay = determineOpenDay(dayOfWeek);

    List<RestaurantOpen> openTimes = restaurantOpenRepository.findByRestaurantAndRestDay(restaurantInfo.getRestaurant(), openDay);

    RestAvailableResDto dto = new RestAvailableResDto();
    dto.setRestId(restId);
    dto.setRestDay(openDay);

    for (RestaurantOpen openTime : openTimes) {
      Map<String, Boolean> times = calculateAvailableTimes(openTime, restaurantInfo, date);
      dto.addAvailability(date.toString(), times);
    }

    return dto;
  }

  // DayOfWeek 값을 기반으로 OpenDay 열거형을 결정
  private OpenDay determineOpenDay(DayOfWeek dayOfWeek) {
    switch (dayOfWeek) {
      case MONDAY:
        return OpenDay.MON;
      case TUESDAY:
        return OpenDay.TUE;
      case WEDNESDAY:
        return OpenDay.WED;
      case THURSDAY:
        return OpenDay.THU;
      case FRIDAY:
        return OpenDay.FRI;
      case SATURDAY:
        return OpenDay.SAT;
      case SUNDAY:
        return OpenDay.SUN;
      default:
        throw new IllegalArgumentException("Unsupported day of the week: " + dayOfWeek);
    }
  }

  // 특정 시간대의 예약 가능 여부를 계산
  private Map<String, Boolean> calculateAvailableTimes(RestaurantOpen openTime, RestaurantInfo restaurantInfo, LocalDate date) {
    Map<String, Boolean> times = new HashMap<>();
    int intervalInMinutes = restaurantInfo.getRestReserveInterval() == MinuteType.ONEHOUR ? 60 : 30;

    LocalTime currentTime = LocalTime.parse(openTime.getRestOpen());
    LocalTime lastOrderTime = LocalTime.parse(openTime.getRestLastorder());

    // 브레이크 시간 처리: null 및 빈 문자열에 대한 처리
    LocalTime breakStartTime = parseTimeOrNull(openTime.getRestBreakstart());
    LocalTime breakEndTime = parseTimeOrNull(openTime.getRestBreakend());

    // 현재 날짜와 시간
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    LocalDate today = now.toLocalDate();
    LocalTime nowTime = now.toLocalTime();

    // 현재 날짜와 3시간 후 시간
    ZonedDateTime nowPlusThreeHours = now.plusHours(3);
    LocalTime threeHoursLater = nowPlusThreeHours.toLocalTime();

    while (currentTime.isBefore(lastOrderTime) || currentTime.equals(lastOrderTime)) {
      // 현재 시간과 날짜를 비교
      ZonedDateTime currentDateTime = ZonedDateTime.of(date, currentTime, ZoneId.of("Asia/Seoul"));
      ZonedDateTime currentDateTimeNow = ZonedDateTime.of(today, nowTime, ZoneId.of("Asia/Seoul"));

      // 현재 시간 이전인지 여부
      boolean isBeforeNow = currentDateTime.isBefore(currentDateTimeNow);

      // 현재 시간부터 3시간 후까지의 시간대인지 여부
      boolean isWithinThreeHours = !currentDateTime.isBefore(currentDateTimeNow) &&
          !currentDateTime.isAfter(currentDateTimeNow.plusHours(3));

      if (isBeforeNow || isWithinThreeHours) {
        // 현재 시간 이전이거나 현재 시간부터 3시간 후까지는 예약 불가능
        times.put(currentTime.toString(), false);
      } else {
        // 나머지 시간대는 예약 가능 여부 체크
        boolean isInBreakPeriod = false;
        if (breakStartTime != null && breakEndTime != null) {
          isInBreakPeriod = currentTime.isAfter(breakStartTime) && currentTime.isBefore(breakEndTime);
        }

        if (!isInBreakPeriod) {
          times.put(currentTime.toString(), isReservationPossible(currentTime, restaurantInfo, date));
        }
      }

      currentTime = currentTime.plusMinutes(intervalInMinutes);
    }

    return times;
  }

  // 문자열을 LocalTime으로 파싱하거나 null을 반환하는 메서드
  private LocalTime parseTimeOrNull(String timeString) {
    if (timeString == null || timeString.trim().isEmpty()) {
      return null;
    }
    try {
      return LocalTime.parse(timeString);
    } catch (DateTimeParseException e) {
      // 파싱 오류가 발생한 경우 null 반환
      return null;
    }
  }



  // 특정 시간대에 예약이 가능한지 여부를 판단
  private boolean isReservationPossible(LocalTime currentTime, RestaurantInfo restaurantInfo, LocalDate date) {
    int maxTables = restaurantInfo.getRestTablenum();
    int currentTables = countReservationsForTime(currentTime, restaurantInfo, date);

    System.out.println("Max Tables: " + maxTables);
    System.out.println("Current Tables: " + currentTables);
    System.out.println("---------------------------------------");

    return currentTables < maxTables;
  }

  // 특정 시간대에 예약된 테이블 수를 조회
  public int countReservationsForTime(LocalTime currentTime, RestaurantInfo restaurantInfo, LocalDate date) {
    ZonedDateTime startDateTime = ZonedDateTime.of(date, currentTime, ZoneId.of("Asia/Seoul"));
    Timestamp startTimestamp = Timestamp.valueOf(startDateTime.toLocalDateTime());

    System.out.println("Start Timestamp: " + startTimestamp);

    List<ReservationStatus> statuses = Arrays.asList(ReservationStatus.RESERVATION_READY, ReservationStatus.RESERVATION_ACCEPTED);

    int count = reservationRepository.countByRestaurantAndResDatetimeAndStatusIn(restaurantInfo.getRestaurant(), startTimestamp, statuses);
    System.out.println("Reservation Count: " + count);

    return count;
  }
}