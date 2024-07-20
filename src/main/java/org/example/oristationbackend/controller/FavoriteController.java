package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.user.FavRestResDto;
import org.example.oristationbackend.dto.user.FavoriteDto;
import org.example.oristationbackend.entity.Favorite;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
  private final FavoriteService favoriteService;

  @Autowired
  public FavoriteController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
  }

  // 사용자 id에 따라 찜 목록 조회
  @GetMapping("/{userId}")
  public ResponseEntity<List<FavoriteDto>> getFavoritesByUserId(@PathVariable(name = "userId") int userId) {
    List<FavoriteDto> favorites = favoriteService.getFavoritesByUserId(userId);
    if (favorites.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(favorites);
    }
  }

  // 사용자 id에 따라 찜한 식당 정보 조회
  @GetMapping("/{userId}/rest")
  public List<FavRestResDto> getFavoriteRestaurants(@PathVariable(name = "userId") int userId) {
    return favoriteService.getFavoriteRestaurants(userId);
  }

  // 찜 추가
  @PostMapping("/{userId}/rest/{restId}")
  public ResponseEntity<FavoriteDto> addFavorite(@PathVariable(name = "userId") int userId, @PathVariable(name = "restId") int restId) {
    User user = new User();
    user.setUserId(userId);

    Restaurant restaurant = new Restaurant();
    restaurant.setRestId(restId);

    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setRestaurant(restaurant);

    FavoriteDto favoriteDto = favoriteService.addFavorite(favorite);
    return ResponseEntity.ok(favoriteDto);
  }

  // 찜 삭제 - favoriteId 로 삭제
  @DeleteMapping("/{favoriteId}")
  public ResponseEntity<Void> deleteFavorite(@PathVariable(name = "favoriteId") int favoriteId) {
    favoriteService.deleteFavorite(favoriteId);
    return ResponseEntity.ok().build();
  }

  // 찜 삭제 - userId, restId 로 삭제
  @DeleteMapping("/{userId}/rest/{restId}")
  public ResponseEntity<Void> deleteFavoriteByUserId_RestId(@PathVariable(name = "userId") int userId, @PathVariable(name = "restId") int restId) {
    favoriteService.deleteFavoriteByUserIdRestId(userId, restId);
    return ResponseEntity.ok().build();
  }


}