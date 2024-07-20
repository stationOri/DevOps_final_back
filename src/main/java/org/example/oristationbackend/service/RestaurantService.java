package org.example.oristationbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.restAcceptReadyDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.user.MostRestDto;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.entity.Keyword;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.repository.KeywordRepository;
import org.example.oristationbackend.repository.ReservationRepository;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.example.oristationbackend.repository.RestaurantRepository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;
  private final RestaurantInfoRepository restaurantInfoRepository;
  private final ReservationRepository reservationRepository;
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
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

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
                    formatter.format(restaurant.getJoinDate())))
            .collect(Collectors.toList());
  }

  //식당 승인 후 매장 불러오기
  public List<restAfterAcceptDto> findRestraurantByStatusAfter(RestaurantStatus status) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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
                    restaurant.getJoinDate() != null ? formatter.format(restaurant.getJoinDate()) : "",
                    restaurant.getQuitDate() != null ? formatter.format(restaurant.getQuitDate()) : "",
                    restaurant.isBlocked(),
                    restaurant.isRestIsopen()))
            .collect(Collectors.toList());
  }

  // 식당 정보 수정
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

  // 페이징 처리된 식당 정보 조회
  public List<SearchResDto> getRestaurantsByPage(int page) {
    Pageable pageable = PageRequest.of(page, 20);
    Page<RestaurantInfo> restaurantInfos=restaurantInfoRepository.findAll(pageable);
    return restaurantInfos.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
  }

  // 자주 방문한 식당 조회
  public List<MostRestDto> getMostReservedRestaurantsByUser(int userId) {
    LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
    LocalDateTime now = LocalDateTime.now();
    ReservationStatus visitedStatus = ReservationStatus.VISITED;

    List<Object[]> results = reservationRepository.findMostReservedRestaurantsByUser(userId, twoYearsAgo, now, visitedStatus);

    return results.stream()
        .map(result -> {
          Restaurant restaurant = (Restaurant) result[0];
          Long revCount = (Long) result[1];
          return new MostRestDto(
              restaurant.getRestId(),
              restaurant.getRestName(),
              restaurant.getRestaurantInfo().getRestAddress(),
              restaurant.getRestPhone(),
              restaurant.getRestaurantInfo().getRestGrade(),
              restaurant.getRestPhoto(),
              Math.toIntExact(revCount)
          );
        })
        .limit(4) // 4개만 반환
        .collect(Collectors.toList());
  }
}