package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.oristationbackend.dto.restaurant.MenuAddReqDto;
import org.example.oristationbackend.dto.restaurant.MenuListResDto;
import org.example.oristationbackend.dto.restaurant.MenuModReqDto;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantMenu;
import org.example.oristationbackend.repository.RestaurantMenuRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RestaurantMenuService {
  private final RestaurantRepository restaurantRepository;
  private final RestaurantMenuRepository restaurantMenuRepository;

  // 식당 id로 식당 메뉴 전체 조회
  public List<MenuListResDto> getAllMenusByRestaurantId(int restId) {
    return restaurantMenuRepository.findAllByRestaurant_RestId(restId).stream()
        .map(this::convertToMenuListResDto)
        .collect(Collectors.toList());
  }

  // 메뉴 추가
  @Transactional
  public int addRestaurantMenu(MenuAddReqDto menuAddReqDto) {
    Restaurant restaurant = restaurantRepository.findById(menuAddReqDto.getRestId())
        .orElseThrow(() -> new IllegalArgumentException("식당을 찾을 수 없습니다: " + menuAddReqDto.getRestId()));

    restaurantMenuRepository.findByMenuNameAndRestaurant_RestId(menuAddReqDto.getMenuName(), menuAddReqDto.getRestId())
        .ifPresent(existingMenu -> {
          throw new IllegalArgumentException("이 식당에 이미 같은 이름의 메뉴가 등록되어 있습니다: " + menuAddReqDto.getMenuName());
        });

    RestaurantMenu restaurantMenu = new RestaurantMenu();
    restaurantMenu.setMenuName(menuAddReqDto.getMenuName());
    restaurantMenu.setMenuPrice(menuAddReqDto.getMenuPrice());
    restaurantMenu.setMenuPhoto(menuAddReqDto.getMenuPhoto());
    restaurantMenu.setRestaurant(restaurant);

    return restaurantMenuRepository.save(restaurantMenu).getMenuId();
  }

  // 메뉴 id로 메뉴 수정
  @Transactional
  public int updateRestaurantMenu(int menuId, MenuModReqDto menuModReqDto) {
    RestaurantMenu menuToUpdate = restaurantMenuRepository.findById(menuId)
        .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다: " + menuId));

    if (menuModReqDto.getMenuPrice() != 0) {
      menuToUpdate.setMenuPrice(menuModReqDto.getMenuPrice());
    }
    if (menuModReqDto.getMenuPhoto() != null) {
      menuToUpdate.setMenuPhoto(menuModReqDto.getMenuPhoto());
    }

    return restaurantMenuRepository.save(menuToUpdate).getMenuId();
  }

  // 메뉴 id로 메뉴 삭제
  @Transactional
  public void deleteRestaurantMenu(int menuId) {
    restaurantMenuRepository.deleteById(menuId);
  }

  // 엔티티를 dto로 변환
  private MenuListResDto convertToMenuListResDto(RestaurantMenu restaurantMenu) {
    return new MenuListResDto(
        restaurantMenu.getMenuId(),
        restaurantMenu.getRestaurant().getRestId(),
        restaurantMenu.getMenuName(),
        restaurantMenu.getMenuPrice(),
        restaurantMenu.getMenuPhoto()
    );
  }
}