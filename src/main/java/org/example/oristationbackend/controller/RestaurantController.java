package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.admin.restAcceptReadyDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.entity.type.RestaurantStatus;
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

  @Autowired
  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
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

  //식당 승인 전 불러오기
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


}