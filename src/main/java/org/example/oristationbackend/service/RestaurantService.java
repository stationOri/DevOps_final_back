package org.example.oristationbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.restAcceptReadyDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.entity.Keyword;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.repository.KeywordRepository;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.example.oristationbackend.repository.RestaurantRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;
  private final RestaurantInfoRepository restaurantInfoRepository;
  private final KeywordRepository keywordRepository;

  // 전체 식당 정보 조회
  public List<SearchResDto> findAllRestaurants() {
    List<RestaurantInfo> restaurantInfos = restaurantInfoRepository.findAll();
    return restaurantInfos.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  // 식당 id로 식당 정보 조회
  public SearchResDto findRestaurantById(int restId) {
    RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restId)
        .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다: " + restId));

    return convertToDto(restaurantInfo);
  }

  //식당 승인 전 매장 불러오기
  public List<restAcceptReadyDto> findRestraurantByStatusBefore(RestaurantStatus status) {
    List<Restaurant> restaurants = restaurantRepository.findRestaurantByRestStatus(status);

    return restaurants.stream()
            .map(restaurant -> new restAcceptReadyDto(
                    restaurant.getRestId(),
                    restaurant.getRestName(),
                    restaurant.getRestStatus(),
                    restaurant.getRestNum(),
                    restaurant.getRestOwner(),
                    restaurant.getRestPhone(),
                    restaurant.getRestData(),
                    restaurant.getJoinDate()))
            .collect(Collectors.toList());
  };

  //식당 승인 후 매장 불러오기
  public List<restAfterAcceptDto> findRestraurantByStatusAfter(RestaurantStatus status) {
    List<Restaurant> restaurants = restaurantRepository.findRestaurantByRestStatus(status);

    return restaurants.stream()
            .map(restaurant -> new restAfterAcceptDto(
                    restaurant.getRestId(),
                    restaurant.getRestName(),
                    restaurant.getRestStatus().getDescription(),
                    restaurant.getRestNum(),
                    restaurant.getRestOwner(),
                    restaurant.getRestPhone(),
                    restaurant.getRestData(),
                    restaurant.getJoinDate(),
                    restaurant.getQuitDate(),
                    restaurant.isBlocked(),
                    restaurant.isRestIsopen()))
            .collect(Collectors.toList());

  };


  @Transactional
  public int updateRestaurantStatus(RestaurantStatus status, int restId) {
    Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restId);
    if (optionalRestaurant.isPresent()) {
      Restaurant restaurant = optionalRestaurant.get();
      restaurant.setRestStatus(status);
      return restaurantRepository.save(restaurant).getRestId();
    } else {
      throw new EntityNotFoundException("Restaurant not found with id: " + restId);
    }
  }

  // 엔티티를 DTO로 변환
  private SearchResDto convertToDto(RestaurantInfo restaurantInfo) {
    Restaurant restaurant = restaurantRepository.findById(restaurantInfo.getRestId())
        .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다: " + restaurantInfo.getRestId()));

    Keyword keyword1 = restaurantInfo.getKeyword1();
    Keyword keyword2 = restaurantInfo.getKeyword2();
    Keyword keyword3 = restaurantInfo.getKeyword3();

    String keyword1Name = keyword1 != null ? keyword1.getKeyword() : null;
    String keyword2Name = keyword2 != null ? keyword2.getKeyword() : null;
    String keyword3Name = keyword3 != null ? keyword3.getKeyword() : null;

    return new SearchResDto(
        restaurant.getRestId(),
        restaurant.getRestName(),
        restaurantInfo.getRestAddress(),
        restaurant.getRestPhone(),
        restaurantInfo.getRestGrade(),
        restaurant.getRestPhoto(),
        restaurantInfo.getRestIntro(),
        restaurantInfo.getRestPost(),
        restaurantInfo.getRevWait(),
        keyword1Name,
        keyword2Name,
        keyword3Name
    );
  }

  public List<SearchResDto> getRestaurantsByPage(int page) {

    Pageable pageable = PageRequest.of(page, 20);
    Page<RestaurantInfo> restaurantInfos=restaurantInfoRepository.findAll(pageable);
    return restaurantInfos.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
  }

}