package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.restaurant.RestaurantOpenDto;
import org.example.oristationbackend.service.RestaurantOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/opentime")
public class RestOpenController {
  private final RestaurantOpenService restaurantOpenService;

  @Autowired
  public RestOpenController(RestaurantOpenService restaurantOpenService) {
    this.restaurantOpenService = restaurantOpenService;
  }

  // 식당 id로 식당 오픈 시간 조회
  @GetMapping("/{restId}")
  public ResponseEntity<List<RestaurantOpenDto>> getRestaurantOpen(@PathVariable(name = "restId") int restId) {
    List<RestaurantOpenDto> restaurantOpenDtos = restaurantOpenService.findByRestId(restId);
    if (restaurantOpenDtos.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(restaurantOpenDtos);
    }
  }

  // 식당 오픈 시간 등록
  @PostMapping("/{restId}")
  public ResponseEntity<RestaurantOpenDto> createRestaurantOpen(@RequestBody RestaurantOpenDto restaurantOpenDto) {
    try {
      RestaurantOpenDto createdOpenTime = restaurantOpenService.createRestaurantOpen(restaurantOpenDto);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdOpenTime);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  // 식당 오픈 시간 수정
  @PutMapping("/{restId}")
  public ResponseEntity<RestaurantOpenDto> updateRestaurantOpen(@RequestBody RestaurantOpenDto restaurantOpenDto) {
    try {
      RestaurantOpenDto updatedOpenTime = restaurantOpenService.updateRestaurantOpen(restaurantOpenDto);
      return ResponseEntity.ok(updatedOpenTime);
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
