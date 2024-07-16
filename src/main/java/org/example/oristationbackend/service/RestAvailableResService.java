package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestAvailableResDto;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.RestaurantOpen;
import org.example.oristationbackend.entity.type.MinuteType;
import org.example.oristationbackend.entity.type.OpenDay;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.example.oristationbackend.repository.RestaurantOpenRepository;
import org.example.oristationbackend.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestAvailableResService {

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

  // 식당의 오픈 시간을 받아서 해당 식당의 예약 가능 시간을 계산하여 반환
  private Map<String, Boolean> calculateAvailableTimes(RestaurantOpen openTime, RestaurantInfo restaurantInfo, LocalDate date) {
    Map<String, Boolean> times = new HashMap<>();
    int intervalInMinutes = restaurantInfo.getRestReserveInterval() == MinuteType.ONEHOUR ? 60 : 30;

    LocalTime currentTime = LocalTime.parse(openTime.getRestOpen());

    while (currentTime.isBefore(LocalTime.parse(openTime.getRestLastorder())) || currentTime.equals(LocalTime.parse(openTime.getRestLastorder()))) {
      if (!(currentTime.isAfter(LocalTime.parse(openTime.getRestBreakstart())) && currentTime.isBefore(LocalTime.parse(openTime.getRestBreakend())))) {
        times.put(currentTime.toString(), isReservationPossible(currentTime, restaurantInfo, date));
      }
      currentTime = currentTime.plusMinutes(intervalInMinutes);
    }

    return times;
  }

  // 특정 시간대에 예약이 가능한지 여부를 판단
  private boolean isReservationPossible(LocalTime currentTime, RestaurantInfo restaurantInfo, LocalDate date) {
    int maxTables = restaurantInfo.getRestTablenum();
    int currentTables = countReservationsForTime(currentTime, restaurantInfo, date);

    return currentTables < maxTables;
  }

  // 특정 시간대에 예약된 테이블 수를 조회
  private int countReservationsForTime(LocalTime currentTime, RestaurantInfo restaurantInfo, LocalDate date) {
    LocalTime endTime = currentTime.plusMinutes(restaurantInfo.getRestReserveInterval() == MinuteType.ONEHOUR ? 60 : 30);
    LocalDateTime startDateTime = date.atTime(currentTime);
    LocalDateTime endDateTime = date.atTime(endTime);
    Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
    Timestamp endTimestamp = Timestamp.valueOf(endDateTime);
    return reservationRepository.countByRestaurantAndResDatetimeBetween(restaurantInfo.getRestaurant(), startTimestamp, endTimestamp);
  }
}