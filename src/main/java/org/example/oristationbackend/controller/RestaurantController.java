package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}