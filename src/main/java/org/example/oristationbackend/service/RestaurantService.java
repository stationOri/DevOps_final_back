package org.example.oristationbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.restAcceptReadyDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.restaurant.RestRegisterDto;
import org.example.oristationbackend.dto.user.*;
import org.example.oristationbackend.entity.*;
import org.example.oristationbackend.entity.type.*;
import org.example.oristationbackend.repository.*;


import org.example.oristationbackend.util.DistanceCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.SimpleDateFormat;
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
  private final RestaurantPeakRepository restaurantPeakRepository;
  private final KeywordRepository keywordRepository;
  private final LoginRepository loginRepository;
  private final ReviewRepository reviewRepository;
  private final ReviewLikesRepository reviewLikesRepository;
  private final DistanceCalculator distanceCalculator;

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

  // 식당 이름으로 식당 정보 조회
  public List<SearchResDto> findRestaurantsByName(String restName) {
    List<Restaurant> restaurants = restaurantRepository.findRestaurantInfoByRestNameContaining(restName);
    return restaurants.stream()
        .map(this::convertToDtoRest)
        .collect(Collectors.toList());
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
  private SearchResDto convertToDtoRest(Restaurant restaurant) {
    RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restaurant.getRestId())
        .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다: " + restaurant.getRestId()));

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

  // 추천 식당 조회 (평점 4.0 이상, 최근 3개월간 예약이 가장 많은 식당 7개)
  public List<RecommendRestDto> getRecommendedRestaurants() {
    LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
    ReservationStatus visitedStatus = ReservationStatus.VISITED;

    List<Object[]> results = reservationRepository.findRecommendedRestaurants(threeMonthsAgo, visitedStatus);

    return results.stream()
        .map(result -> {
          Restaurant restaurant = (Restaurant) result[0];
          return new RecommendRestDto(
              restaurant.getRestId(),
              restaurant.getRestName(),
              restaurant.getRestPhoto(),
              restaurant.getRestaurantInfo().getRestAddress(),
              restaurant.getRestaurantInfo().getRestGrade()
          );
        })
        .limit(7) // 7개만 반환
        .collect(Collectors.toList());
  }

  // 주변 식당 조회(사용자 위치로부터 주변 5km 이내의 식당)
  public List<NearRestDto> getNearbyRestaurants(double userLat, double userLng) {
    // 1. 반경 내의 식당 ID를 가져온다
    List<Integer> restIds = restaurantRepository.findNearbyRestaurantIds(userLat, userLng, 5000);

    // 2. 식당 정보 및 리뷰 정보를 가져온다
    List<RestaurantInfo> restaurantInfos = restaurantInfoRepository.findAllById(restIds);

    return restaurantInfos.stream().map(restaurantInfo -> {
      Restaurant restaurant = restaurantInfo.getRestaurant();  // 식당 정보 가져오기 (양방향 관계 가정)
      List<NearRestReviewDto> reviews = reviewRepository.findByRestaurant_RestId(restaurant.getRestId()).stream()
          .map(review -> new NearRestReviewDto(
              review.getReviewId(),
              review.getReviewGrade(),
              review.getUser().getUserNickname(),
              review.getLikeNum(),
              review.getReviewData()
          ))
          .collect(Collectors.toList());

      return new NearRestDto(
          restaurant.getRestId(),
          restaurant.getRestName(),
          restaurant.getRestaurantInfo().getRestAddress(),
          restaurant.getRestPhoto(),
          restaurant.getRestaurantInfo().getKeyword1().getKeyword(),
          restaurant.getRestaurantInfo().getKeyword2().getKeyword(),
          restaurant.getRestaurantInfo().getKeyword3().getKeyword(),
          restaurant.getRestaurantInfo().getLat(),
          restaurant.getRestaurantInfo().getLng(),
          reviews
      );
    }).collect(Collectors.toList());
  }

  private NearRestDto convertToNearRestDto(Restaurant restaurant) {
    // 식당 정보를 DTO로 변환
    List<NearRestReviewDto> reviews = reviewRepository.findByRestaurant_RestId(restaurant.getRestId()).stream()
        .map(review -> new NearRestReviewDto(
            review.getReviewId(),
            review.getReviewGrade(),
            review.getUser().getUserNickname(),
            review.getLikeNum(),
            review.getReviewData()
        ))
        .collect(Collectors.toList());

    RestaurantInfo info = restaurant.getRestaurantInfo();

    return new NearRestDto(
        restaurant.getRestId(),
        restaurant.getRestName(),
        info != null ? info.getRestAddress() : null,
        restaurant.getRestPhoto(),
        info != null && info.getKeyword1() != null ? info.getKeyword1().getKeyword() : null,
        info != null && info.getKeyword2() != null ? info.getKeyword2().getKeyword() : null,
        info != null && info.getKeyword3() != null ? info.getKeyword3().getKeyword() : null,
        info != null ? info.getLat() : 0.0,  // 위도
        info != null ? info.getLng() : 0.0,  // 경도
        reviews
    );
  }

  // 최근 인기 식당 조회(최근 2주 동안 예약이 가장 많은 식당 5개 순위대로)
  public List<HotRestDto> getHotRestaurants() {
    LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
    ReservationStatus visitedStatus = ReservationStatus.VISITED;

    List<Object[]> results = reservationRepository.findHotRestaurants(twoWeeksAgo, visitedStatus);

    return results.stream()
        .map(result -> {
          Restaurant restaurant = (Restaurant) result[0];
          return new HotRestDto(
              restaurant.getRestId(),
              restaurant.getRestName(),
              restaurant.getRestPhoto(),
              restaurant.getRestaurantInfo().getRestGrade(),
              restaurant.getRestaurantInfo().getKeyword1().getKeyword(),
              restaurant.getRestaurantInfo().getKeyword2().getKeyword(),
              restaurant.getRestaurantInfo().getKeyword3().getKeyword()
          );
        })
        .limit(5) // 5개만 반환
        .collect(Collectors.toList());
  }


  // 식당 등록
  @Transactional(readOnly = false)
  public int addRestaurant(RestRegisterDto restRegisterDto) {
      if(existRestaurant((restRegisterDto.getRestPhone()))){
        return 0;
      }
    Login login = new Login(0, restRegisterDto.getEmail(), null, ChatType.RESTAURANT, null, null, null);
    login = loginRepository.save(login);
    Restaurant restaurant= new Restaurant(login,0,restRegisterDto.getRestName(),restRegisterDto.getRestPhone(),restRegisterDto.getRestName2(),"",restRegisterDto.getRestData(),
            restRegisterDto.getRestData(),false, new Date(System.currentTimeMillis()),null,RestaurantStatus.A,false,null,null);
    RestaurantInfo restaurantInfo = new RestaurantInfo(null,null,null, ReservationType.A, RestWatingStatus.A);
    restaurantInfo.setRestaurant(restaurant);
    restaurant.setRestaurantInfo(restaurantInfo);
    restaurantRepository.save(restaurant);
    login.setRestaurant(restaurant);
    return loginRepository.save(login).getLoginId();
  }


  private boolean existRestaurant(String phone) {
    return restaurantRepository.existsByRestPhone(phone);
  }

  public int getRestaurantDeposit(int restId) {
    return reservationRepository.findTotalNoshowAmountByRestId(restId, ReservationStatus.NOSHOW);
  }

  // 식당 계좌 수정
  @Transactional
  public void updateRestaurantAccount(int restId, String restAccount) {
    Restaurant restaurant = restaurantRepository.findById(restId)
        .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restId));
    restaurant.setRestAccount(restAccount);
    restaurantRepository.save(restaurant);
  }


}