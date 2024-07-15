package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestaurantOpenDto;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantOpen;
import org.example.oristationbackend.repository.RestaurantOpenRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantOpenService {
  private final RestaurantOpenRepository restaurantOpenRepository;
  private final RestaurantRepository restaurantRepository;

  // 식당 id로 식당 오픈 시간 조회
  public List<RestaurantOpenDto> findByRestId(int restId) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restId);

    if (restaurantOptional.isEmpty()) {
      throw new RuntimeException("식당을 찾을 수 없습니다: " + restId);
    }

    Restaurant restaurant = restaurantOptional.get();
    List<RestaurantOpen> restaurantOpens = restaurantOpenRepository.findByRestaurant(restaurant);

    restaurantOpens.forEach(restaurantOpen -> Hibernate.initialize(restaurantOpen.getRestaurant()));

    return restaurantOpens.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  // 식당 오픈시간 등록
  public RestaurantOpenDto createRestaurantOpen(RestaurantOpenDto restaurantOpenDto) {
    Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantOpenDto.getRestId());

    if (restaurantOptional.isEmpty()) {
      throw new RuntimeException("식당을 찾을 수 없습니다: " + restaurantOpenDto.getRestId());
    }

    Restaurant restaurant = restaurantOptional.get();
    RestaurantOpen restaurantOpen = new RestaurantOpen();
    restaurantOpen.setRestaurant(restaurant);
    restaurantOpen.setRestDay(restaurantOpenDto.getRestDay());
    restaurantOpen.setRestOpen(restaurantOpenDto.getRestOpen());
    restaurantOpen.setRestClose(restaurantOpenDto.getRestClose());
    restaurantOpen.setRestLastorder(restaurantOpenDto.getRestLastorder());
    restaurantOpen.setRestBreakstart(restaurantOpenDto.getRestBreakstart());
    restaurantOpen.setRestBreakend(restaurantOpenDto.getRestBreakend());

    RestaurantOpen savedRestaurantOpen = restaurantOpenRepository.save(restaurantOpen);

    return convertToDto(savedRestaurantOpen);
  }

  // 식당 오픈시간 수정
  public RestaurantOpenDto updateRestaurantOpen(RestaurantOpenDto restaurantOpenDto) {
    Optional<RestaurantOpen> restaurantOpenOptional = restaurantOpenRepository.findById(restaurantOpenDto.getRestaurantOpenId());

    if (restaurantOpenOptional.isEmpty()) {
      throw new RuntimeException("식당 오픈시간을 찾을 수 없습니다: " + restaurantOpenDto.getRestaurantOpenId());
    }

    RestaurantOpen restaurantOpen = restaurantOpenOptional.get();
    restaurantOpen.setRestDay(restaurantOpenDto.getRestDay());
    restaurantOpen.setRestOpen(restaurantOpenDto.getRestOpen());
    restaurantOpen.setRestClose(restaurantOpenDto.getRestClose());
    restaurantOpen.setRestLastorder(restaurantOpenDto.getRestLastorder());
    restaurantOpen.setRestBreakstart(restaurantOpenDto.getRestBreakstart());
    restaurantOpen.setRestBreakend(restaurantOpenDto.getRestBreakend());

    RestaurantOpen savedRestaurantOpen = restaurantOpenRepository.save(restaurantOpen);

    return convertToDto(savedRestaurantOpen);
  }

  // 엔티티를 DTO로 변환
  private RestaurantOpenDto convertToDto(RestaurantOpen restaurantOpen) {
    return new RestaurantOpenDto(
        restaurantOpen.getRestaurantOpenId(),
        restaurantOpen.getRestaurant().getRestId(),
        restaurantOpen.getRestDay(),
        restaurantOpen.getRestOpen(),
        restaurantOpen.getRestClose(),
        restaurantOpen.getRestLastorder(),
        restaurantOpen.getRestBreakstart(),
        restaurantOpen.getRestBreakend()
    );
  }
}