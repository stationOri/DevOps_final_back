package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.admin.restAcceptReadyDto;
import org.example.oristationbackend.dto.restaurant.MenuAddReqDto;
import org.example.oristationbackend.dto.restaurant.MenuListResDto;
import org.example.oristationbackend.dto.restaurant.MenuModReqDto;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.service.RestaurantMenuService;
import org.example.oristationbackend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
  private final RestaurantService restaurantService;
  private final RestaurantMenuService restaurantMenuService;

  @Autowired
  public RestaurantController(RestaurantService restaurantService, RestaurantMenuService restaurantMenuService) {
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


  @GetMapping("/beforeAccept")
  public ResponseEntity<List<restAcceptReadyDto>> getRestaurantsBeforeAccept() {
    List<restAcceptReadyDto> resultRestaurants = restaurantService.findRestraurantByStatus(RestaurantStatus.A);
    if (resultRestaurants == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(resultRestaurants);
    }
  }
}