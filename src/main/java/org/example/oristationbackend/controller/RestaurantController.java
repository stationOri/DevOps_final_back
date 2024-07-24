package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.admin.restAcceptReadyDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.restaurant.MenuAddReqDto;
import org.example.oristationbackend.dto.restaurant.MenuListResDto;
import org.example.oristationbackend.dto.restaurant.MenuModReqDto;
import org.example.oristationbackend.dto.user.HotRestDto;
import org.example.oristationbackend.dto.user.MostRestDto;
import org.example.oristationbackend.dto.user.RecommendRestDto;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.service.ReservationService;
import org.example.oristationbackend.service.RestaurantMenuService;
import org.example.oristationbackend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
  private final RestaurantService restaurantService;
  private final RestaurantMenuService restaurantMenuService;

  @Autowired
  public RestaurantController(RestaurantService restaurantService, RestaurantMenuService restaurantMenuService, ReservationService reservationService) {
    this.restaurantService = restaurantService;
    this.restaurantMenuService = restaurantMenuService;
  }

  // 식당 전체 조회
  @GetMapping()
  public ResponseEntity<List<SearchResDto>> getAllRestaurants() {
    List<SearchResDto> restaurants = restaurantService.findAllRestaurants();
    if (restaurants.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(restaurants);
    }
  }

  // 식당 페이지별 조회
  @GetMapping("/page/{page}")
  public ResponseEntity<List<SearchResDto>> getRestaurantsByPage(@PathVariable("page") int page) {
    List<SearchResDto> restaurants = restaurantService.getRestaurantsByPage(page);

    if (restaurants.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(restaurants);
    }
  }

  // 식당 id로 식당 정보 조회
  @GetMapping("/{restId}")
  public ResponseEntity<SearchResDto> getRestaurantById(@PathVariable(name = "restId") int restId) {
    SearchResDto restaurant = restaurantService.findRestaurantById(restId);
    if (restaurant == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(restaurant);
    }
  }
  //식당이 환불받을 예약금 조회
  @GetMapping("/deposit/{restId}")
  public int getRestaurantDeposit(@PathVariable(name = "restId") int restId){
    return restaurantService.getRestaurantDeposit(restId);
  }

  // 식당 이름으로 식당 정보 조회
  @GetMapping("/name/{restName}")
  public ResponseEntity<List<SearchResDto>> getRestaurantsByName(@PathVariable(name = "restName") String restName) {
    List<SearchResDto> restaurants = restaurantService.findRestaurantsByName(restName);
    if (restaurants.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(restaurants);
    }
  }

  // 식당 id로 식당 메뉴 전체 조회
  @GetMapping("/menu/{restId}")
  public ResponseEntity<List<MenuListResDto>> getAllMenusByRestId(@PathVariable(name = "restId") int restId) {
    List<MenuListResDto> menus = restaurantMenuService.getAllMenusByRestaurantId(restId);
    if (menus.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(menus);
    }
  }

  // 메뉴 추가
  @PostMapping("/menu")
  public ResponseEntity<Integer> addRestaurantMenu(@RequestBody MenuAddReqDto menuAddReqDto) {
    int menuId = restaurantMenuService.addRestaurantMenu(menuAddReqDto);
    return ResponseEntity.ok(menuId);
  }

  // 메뉴 수정
  @PutMapping("/menu/{menuId}")
  public ResponseEntity<Integer> updateRestaurantMenu(@PathVariable(name = "menuId") int menuId, @RequestBody MenuModReqDto menuModReqDto) {
    int updatedMenuId = restaurantMenuService.updateRestaurantMenu(menuId, menuModReqDto);
    return ResponseEntity.ok(updatedMenuId);
  }

  // 메뉴 id로 메뉴 삭제
  @DeleteMapping("/menu/{menuId}")
  public ResponseEntity<Void> deleteRestaurantMenu(@PathVariable(name = "menuId") int menuId) {
    restaurantMenuService.deleteRestaurantMenu(menuId);
    return ResponseEntity.ok().build();
  }

  //식당 승인 전
  @GetMapping("/beforeAccept")
  public ResponseEntity<List<restAcceptReadyDto>> getRestaurantsBeforeAccept() {
    List<restAcceptReadyDto> resultRestaurants = restaurantService.findRestraurantByStatusBefore(RestaurantStatus.A);
    if (resultRestaurants == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(resultRestaurants);
    }
  }

  //식당 승인 후
  @GetMapping("/afterAccept")
  public ResponseEntity<List<restAfterAcceptDto>> getRestaurantsAfterAccept() {
    List<restAfterAcceptDto> resultRestaurants = restaurantService.findRestraurantByStatusAfter(RestaurantStatus.B);
    if (resultRestaurants == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(resultRestaurants);
    }
  }

  //식당 승인 및 거절
  @PutMapping("/status/{rest_id}")
  public int updateRestStatus(@PathVariable(name = "rest_id") int restId, @RequestBody Map<String, String> body) {
    String statusStr = body.get("status");
    RestaurantStatus status = RestaurantStatus.fromValue(statusStr);
    return restaurantService.updateRestaurantStatus(status, restId);
  }

  // 최근 2년간 가장 많이 예약된 식당 조회
  @GetMapping("/most/{userId}")
  public List<MostRestDto> getMostReservedRestaurantsByUser(@PathVariable(name = "userId") int userId) {
    return restaurantService.getMostReservedRestaurantsByUser(userId);
  }

  // 추천 식당 조회 (평점 4.0 이상, 최근 3개월간 예약이 가장 많은 식당 7개)
  @GetMapping("/recommend")
  public ResponseEntity<List<RecommendRestDto>> getRecommendedRestaurants() {
    List<RecommendRestDto> recommendedRestaurants = restaurantService.getRecommendedRestaurants();
    return ResponseEntity.ok(recommendedRestaurants);
  }

  // 최근 2주 동안 예약이 가장 많은 식당 조회
  @GetMapping("/hot")
  public ResponseEntity<List<HotRestDto>> getHotRestaurants() {
    List<HotRestDto> hotRestaurants = restaurantService.getHotRestaurants();
    return ResponseEntity.ok(hotRestaurants);
  }

  // 주변 식당 조회(사용자 위치로부터 주변 5km 이내의 식당)



}