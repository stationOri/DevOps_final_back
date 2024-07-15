package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.entity.Keyword;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.repository.KeywordRepository;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restId));

    return convertToDto(restaurantInfo);
  }

  // 엔티티를 DTO로 변환
  private SearchResDto convertToDto(RestaurantInfo restaurantInfo) {
    Restaurant restaurant = restaurantRepository.findById(restaurantInfo.getRestId())
        .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantInfo.getRestId()));

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
}