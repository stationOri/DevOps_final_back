package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestaurantOpenDto;
import org.example.oristationbackend.dto.user.FavRestResDto;
import org.example.oristationbackend.dto.user.FavoriteDto;
import org.example.oristationbackend.dto.user.FavoriteDto;
import org.example.oristationbackend.entity.Favorite;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantOpen;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {
  private final FavoriteRepository favoriteRepository;
  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;
  private final RestaurantInfoRepository restaurantInfoRepository;
  private final RestaurantOpenRepository restaurantOpenRepository;

  // 사용자 id에 따라 찜 목록 조회
  public List<FavoriteDto> getFavoritesByUserId(int userId) {
    return favoriteRepository.findByUser_UserId(userId).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  // 사용자 id에 따라 찜한 식당 정보 조회
  @Transactional(readOnly = true)
  public List<FavRestResDto> getFavoriteRestaurants(int userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    List<Favorite> favorites = favoriteRepository.findByUser(user);

    return favorites.stream()
        .map(favorite -> convertToDto(favorite.getRestaurant()))
        .collect(Collectors.toList());
  }

  // 찜 추가
  @Transactional
  public FavoriteDto addFavorite(Favorite favorite) {
    Favorite savedFavorite = favoriteRepository.save(favorite);
    return convertToDto(savedFavorite);
  }

  // 찜 삭제 - favoriteId 로 삭제
  @Transactional
  public void deleteFavorite(int favoriteId) {
    favoriteRepository.deleteById(favoriteId);
  }

  // 찜 삭제 - user id와 rest id로 삭제
  @Transactional
  public void deleteFavoriteByUserIdRestId(int userId, int restId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    Restaurant restaurant = restaurantRepository.findById(restId)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));

    favoriteRepository.deleteByUser_UserIdAndRestaurant_RestId(user.getUserId(), restaurant.getRestId());
  }

  // 엔티티를 DTO로 변환
  private FavoriteDto convertToDto(Favorite favorite) {
    return new FavoriteDto(
        favorite.getFavoriteId(),
        favorite.getUser().getUserId(),
        favorite.getRestaurant().getRestId()
    );
  }

  private FavRestResDto convertToDto(Restaurant restaurant) {
    List<RestaurantOpen> restaurantOpens = restaurantOpenRepository.findByRestaurantRestId(restaurant.getRestId());
    List<RestaurantOpenDto> openTimes = restaurantOpens.stream()
        .map(open -> new RestaurantOpenDto(
            open.getRestaurantOpenId(),
            open.getRestaurant().getRestId(),
            open.getRestDay(),
            open.getRestOpen(),
            open.getRestClose(),
            open.getRestLastorder(),
            open.getRestBreakstart(),
            open.getRestBreakend()
        ))
        .collect(Collectors.toList());

    return new FavRestResDto(
        restaurant.getRestId(),
        restaurant.getRestPhoto(),
        restaurant.getRestName(),
        restaurant.getRestaurantInfo().getRestAddress(),
        openTimes,
        restaurant.getRestaurantInfo().getKeyword1().getKeyword(),
        restaurant.getRestaurantInfo().getKeyword2().getKeyword(),
        restaurant.getRestaurantInfo().getKeyword3().getKeyword(),
        true
    );
  }
}