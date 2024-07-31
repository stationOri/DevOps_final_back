package org.example.oristationbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.example.oristationbackend.dto.admin.restAcceptReadyDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.restaurant.RestRegisterDto;
import org.example.oristationbackend.dto.restaurant.RestaurantOpenDto;
import org.example.oristationbackend.dto.user.*;
import org.example.oristationbackend.entity.*;
import org.example.oristationbackend.entity.type.*;
import org.example.oristationbackend.repository.*;


import org.example.oristationbackend.util.DistanceCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;
  private final RestaurantInfoRepository restaurantInfoRepository;
  private final ReservationRepository reservationRepository;
  private final RestaurantOpenRepository restaurantOpenRepository;
  private final RestaurantPeakRepository restaurantPeakRepository;
  private final KeywordRepository keywordRepository;
  private final LoginRepository loginRepository;
  private final ReviewRepository reviewRepository;
  private final ReviewLikesRepository reviewLikesRepository;
  private final DistanceCalculator distanceCalculator;
  private final S3Service s3Service;
  private final SmsService smsService;

  // 전체 식당 정보 조회
  public List<SearchResDto> findAllRestaurants() {
    List<Restaurant> restaurants = restaurantRepository.findByRestIsopenTrueAndIsBlockedFalseAndRestStatus(RestaurantStatus.B);
    List<Integer> restIds = restaurants.stream()
        .map(Restaurant::getRestId)
        .collect(Collectors.toList());

    List<RestaurantInfo> restaurantInfos = restaurantInfoRepository.findByRestIdIn(restIds);

    return restaurantInfos.stream()
        .map(restaurantInfo -> {
          Restaurant restaurant = restaurants.stream()
              .filter(r -> r.getRestId() == restaurantInfo.getRestId())
              .findFirst()
              .orElse(null);
          List<RestaurantOpen> restaurantOpens = restaurantOpenRepository.findByRestaurantRestId(restaurant.getRestId());
          List<RestaurantOpenDto> restOpentimes = restaurantOpens.stream()
              .map(RestaurantOpenDto::new)
              .collect(Collectors.toList());
          return new SearchResDto(restaurant, restaurantInfo, restOpentimes);
        })
        .collect(Collectors.toList());
  }

  // 식당 id로 식당 정보 조회
  public SearchResDto findRestaurantById(int restId) {
    Restaurant restaurant = restaurantRepository.findById(restId)
        .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다: " + restId));
    RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restId)
        .orElseThrow(() -> new RuntimeException("식당 정보를 찾을 수 없습니다: " + restId));
    List<RestaurantOpen> restaurantOpens = restaurantOpenRepository.findByRestaurantRestId(restId);
    List<RestaurantOpenDto> restOpentimes = restaurantOpens.stream()
        .map(RestaurantOpenDto::new)
        .collect(Collectors.toList());

    return new SearchResDto(restaurant, restaurantInfo, restOpentimes);
  }

  // 식당 이름으로 식당 정보 조회
  public List<SearchResDto> findRestaurantsByName(String restName) {
    List<Restaurant> restaurants = restaurantRepository.findRestaurantInfoByRestNameContaining(restName);
    return restaurants.stream()
        .map(this::convertToDtoRest)
        .collect(Collectors.toList());
  }

  // 식당 승인 전 매장 불러오기
  public Page<restAcceptReadyDto> findRestraurantByStatusBefore(RestaurantStatus status, int page) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Pageable pageable = PageRequest.of(page, 10, Sort.by("joinDate").descending());
    Page<Restaurant> restaurantsPage = restaurantRepository.findRestaurantByRestStatus(status, pageable);

    return restaurantsPage.map(restaurant -> new restAcceptReadyDto(
        restaurant.getRestId(),
        restaurant.getRestName(),
        restaurant.getRestStatus(),
        restaurant.getRestNum(),
        restaurant.getRestOwner(),
        restaurant.getRestPhone(),
        restaurant.getRestData(),
        formatter.format(restaurant.getJoinDate())));
  }

  // 식당 승인 후 매장 불러오기
  public Page<restAfterAcceptDto> findRestraurantByStatusAfter(RestaurantStatus status, int page) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    Pageable pageable = PageRequest.of(page, 20, Sort.by("joinDate").descending());
    Page<Restaurant> restaurantsPage = restaurantRepository.findRestaurantByRestStatus(status, pageable);

    return restaurantsPage.map(restaurant -> new restAfterAcceptDto(
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
        restaurant.isRestIsopen()));
  }

  // 식당 정보 수정
  @Transactional
  public int updateRestaurantStatus(RestaurantStatus status, int restId) {
    Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(restId);
    if (optionalRestaurant.isPresent()) {
      Restaurant restaurant = optionalRestaurant.get();
      restaurant.setRestStatus(status);
      if(status.equals(RestaurantStatus.B)){
        StringBuilder sb= new StringBuilder();
        sb.append("[WaitMate]");
        sb.append(restaurant.getRestOwner());
        sb.append(" 사장님, ");
        sb.append(restaurant.getRestName());
        sb.append(" 식당이 관리자에 의해 승인되었습니다. 로그인하여 가게 정보를 등록하고 서비스를 이용해주세요. 감사합니다");
        SmsDto smsDto=new SmsDto(restaurant.getRestPhone(),sb.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
        SingleMessageSentResponse resp=smsService.sendOne(smsDto); //해당 코드로 전송
      }
      else if(status.equals(RestaurantStatus.C)){
        StringBuilder sb= new StringBuilder();
        sb.append("[WaitMate]");
        sb.append(restaurant.getRestOwner());
        sb.append(" 사장님, ");
        sb.append(restaurant.getRestName());
        sb.append(" 식당이 관리자에 의해 승인 거절되었습니다. 다시 회원가입하여주세요)");
        SmsDto smsDto=new SmsDto(restaurant.getRestPhone(),sb.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
        SingleMessageSentResponse resp=smsService.sendOne(smsDto); //
      }
      restaurant.setRestPhone("0");
      return restaurantRepository.save(restaurant).getRestId();
    } else {
      throw new EntityNotFoundException("Restaurant not found with id: " + restId);
    }
  }

  // 엔티티를 DTO로 변환
  private SearchResDto convertToDto(RestaurantInfo restaurantInfo) {
    Restaurant restaurant = restaurantRepository.findById(restaurantInfo.getRestId())
        .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다: " + restaurantInfo.getRestId()));

    List<RestaurantOpen> restaurantOpens = restaurantOpenRepository.findByRestaurantRestId(restaurant.getRestId());
    List<RestaurantOpenDto> restOpentimes = restaurantOpens.stream()
        .map(RestaurantOpenDto::new)
        .collect(Collectors.toList());

    return new SearchResDto(restaurant, restaurantInfo, restOpentimes);
  }
  private SearchResDto convertToDtoRest(Restaurant restaurant) {
    RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restaurant.getRestId())
        .orElseThrow(() -> new RuntimeException("식당 정보를 찾을 수 없습니다: " + restaurant.getRestId()));

    List<RestaurantOpen> restaurantOpens = restaurantOpenRepository.findByRestaurantRestId(restaurant.getRestId());
    List<RestaurantOpenDto> restOpentimes = restaurantOpens.stream()
        .map(RestaurantOpenDto::new)
        .collect(Collectors.toList());

    return new SearchResDto(restaurant, restaurantInfo, restOpentimes);
  }

  // 페이징 처리된 조건 필터링된 식당 정보 조회
  public List<SearchResDto> getRestaurantsByPage(int page) {
    Pageable pageable = PageRequest.of(page, 100);
    Page<RestaurantInfo> restaurantInfos = restaurantInfoRepository.findAllActiveRestaurants(pageable);
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
          .limit(2)
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
  public int addRestaurant(RestRegisterDto restRegisterDto, MultipartFile file) throws IOException {
    if (existRestaurant(restRegisterDto.getRestPhone())) {
      return 0;
    }

    Login login = new Login(0, restRegisterDto.getEmail(), null, ChatType.RESTAURANT, null, null, null);
    login = loginRepository.save(login);

    String fileUrl = s3Service.uploadFile(file);

    Restaurant restaurant = new Restaurant(
        login,
        0,
        restRegisterDto.getRestName(),
        restRegisterDto.getRestPhone(),
        restRegisterDto.getRestName2(),
        "",
        fileUrl,
        restRegisterDto.getRestData(),
        false,
        new Date(System.currentTimeMillis()),
        null,
        RestaurantStatus.A,
        false,
        null,
        null
    );
    RestaurantInfo restaurantInfo = new RestaurantInfo(null, null, null, ReservationType.A, RestWatingStatus.A);
    restaurantInfo.setRestaurant(restaurant);
    restaurant.setRestaurantInfo(restaurantInfo);
    RestaurantOpen restaurantMon = new RestaurantOpen("","","","","",restaurant,OpenDay.MON );
    RestaurantOpen restaurantTue = new RestaurantOpen("","","","","",restaurant,OpenDay.TUE );
    RestaurantOpen restaurantWed = new RestaurantOpen("","","","","",restaurant,OpenDay.WED );
    RestaurantOpen restaurantThu = new RestaurantOpen("","","","","",restaurant,OpenDay.THU );
    RestaurantOpen restaurantFri = new RestaurantOpen("","","","","",restaurant,OpenDay.FRI );
    RestaurantOpen restaurantSat = new RestaurantOpen("","","","","",restaurant,OpenDay.SAT );
    RestaurantOpen restaurantSun = new RestaurantOpen("","","","","",restaurant,OpenDay.SUN );
    restaurantOpenRepository.save(restaurantMon);
    restaurantOpenRepository.save(restaurantTue);
    restaurantOpenRepository.save(restaurantWed);
    restaurantOpenRepository.save(restaurantThu);
    restaurantOpenRepository.save(restaurantFri);
    restaurantOpenRepository.save(restaurantSat);
    restaurantOpenRepository.save(restaurantSun);


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


  public String getRestarantAccount(int restId) {
    Restaurant restaurant = restaurantRepository.findById(restId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restId));
    return restaurant.getRestAccount();
  }

  public boolean isopened(int restId) {
    Restaurant restaurant = restaurantRepository.findById(restId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant ID: " + restId));
    return restaurant.isRestIsopen();
  }
}