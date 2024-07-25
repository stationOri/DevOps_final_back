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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RestaurantMenuService {
  private final RestaurantRepository restaurantRepository;
  private final RestaurantMenuRepository restaurantMenuRepository;
  private final S3Service s3Service;

  @Autowired
  public RestaurantMenuService(S3Service s3Service, RestaurantRepository restaurantRepository, RestaurantMenuRepository restaurantMenuRepository) {
    this.s3Service = s3Service;
    this.restaurantRepository = restaurantRepository;
    this.restaurantMenuRepository = restaurantMenuRepository;
  }

  // 식당 id로 식당 메뉴 전체 조회
  public List<MenuListResDto> getAllMenusByRestaurantId(int restId) {
    return restaurantMenuRepository.findAllByRestaurant_RestId(restId).stream()
        .map(this::convertToMenuListResDto)
        .collect(Collectors.toList());
  }

  // 메뉴 추가
  @Transactional
  public int addRestaurantMenu(MenuAddReqDto menuAddReqDto, MultipartFile file) throws IOException {
    Restaurant restaurant = restaurantRepository.findById(menuAddReqDto.getRestId())
        .orElseThrow(() -> new IllegalArgumentException("식당을 찾을 수 없습니다: " + menuAddReqDto.getRestId()));

    String fileUrl = s3Service.uploadFile(file);
    RestaurantMenu restaurantMenu = new RestaurantMenu();
    restaurantMenu.setMenuName(menuAddReqDto.getMenuName());
    restaurantMenu.setMenuPrice(menuAddReqDto.getMenuPrice());
    restaurantMenu.setMenuPhoto(fileUrl); // Use the file URL obtained from S3
    restaurantMenu.setRestaurant(restaurant);

    return restaurantMenuRepository.save(restaurantMenu).getMenuId();
  }

  // 메뉴 id로 메뉴 수정
  @Transactional
  public int updateRestaurantMenu(int menuId, MenuModReqDto menuModReqDto, MultipartFile file) throws IOException {
    RestaurantMenu menuToUpdate = restaurantMenuRepository.findById(menuId)
        .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다: " + menuId));

    if (menuModReqDto.getMenuPrice() != 0) {
      menuToUpdate.setMenuPrice(menuModReqDto.getMenuPrice());
    }
    if (file != null) {
      String fileUrl = s3Service.uploadFile(file); // 파일이 있으면 S3에 업로드하고 URL을 얻음
      menuToUpdate.setMenuPhoto(fileUrl);
    } else if (menuModReqDto.getMenuPhoto() != null) {
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