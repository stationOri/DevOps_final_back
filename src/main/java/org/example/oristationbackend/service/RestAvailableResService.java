package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestAvailableResDto;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.RestaurantOpen;
import org.example.oristationbackend.entity.type.MinuteType;
import org.example.oristationbackend.entity.type.OpenDay;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.example.oristationbackend.repository.RestaurantOpenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestAvailableResService {

  private final RestaurantOpenRepository restaurantOpenRepository;
  private final RestaurantInfoRepository restaurantInfoRepository;

  // 식당 id와 요일을 받아서 해당 식당의 해당 요일의 예약 가능 시간을 반환
  public RestAvailableResDto getAvailableTimes(int restId, OpenDay day) {
    RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restId)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));

    List<RestaurantOpen> openTimes = restaurantOpenRepository.findByRestaurantAndRestDay(restaurantInfo.getRestaurant(), day);

    List<String> availableTimes = new ArrayList<>();

    for (RestaurantOpen openTime : openTimes) {
      List<String> times = calculateAvailableTimes(openTime, restaurantInfo);

      availableTimes.addAll(times);
    }

    RestAvailableResDto dto = new RestAvailableResDto();
    dto.setRestId(restId);
    dto.setRestDay(day);
    dto.setAvailableTimes(availableTimes);

    return dto;
  }

  // 식당의 오픈 시간을 받아서 해당 식당의 예약 가능 시간을 계산하여 반환
  private List<String> calculateAvailableTimes(RestaurantOpen openTime, RestaurantInfo restaurantInfo) {
    List<String> times = new ArrayList<>();
    int intervalInMinutes = restaurantInfo.getRestReserveInterval() == MinuteType.ONEHOUR ? 60 : 30;

    LocalTime currentTime = LocalTime.parse(openTime.getRestOpen());

    while (currentTime.isBefore(LocalTime.parse(openTime.getRestLastorder())) || currentTime.equals(LocalTime.parse(openTime.getRestLastorder()))) {
      if (!(currentTime.isAfter(LocalTime.parse(openTime.getRestBreakstart())) && currentTime.isBefore(LocalTime.parse(openTime.getRestBreakend())))) {
        times.add(currentTime.toString());
      }
      currentTime = currentTime.plusMinutes(intervalInMinutes);
    }

    return times;
  }
}