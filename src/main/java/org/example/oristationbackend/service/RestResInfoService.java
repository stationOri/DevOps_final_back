package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestResInfoDto;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.repository.RestaurantInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestResInfoService {
  private final RestaurantInfoRepository restaurantInfoRepository;

  // 식당 id 로 식당 예약 관련 정보 조회
  public RestResInfoDto findRestResInfoById(int restId) {
    RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restId)
        .orElseThrow(() -> new RuntimeException("식당을 찾을 수 없습니다: " + restId));

    return convertToDto(restaurantInfo);
  }

  // 엔티티를 DTO로 변환
  private RestResInfoDto convertToDto(RestaurantInfo restaurantInfo) {
    return new RestResInfoDto(
        restaurantInfo.getRestId(),
        restaurantInfo.getRestReserveopenRule(),
        restaurantInfo.getRestReserveInterval(),
        restaurantInfo.getRestDepositMethod(),
        restaurantInfo.getRestDeposit(),
        restaurantInfo.getMaxPpl(),
        restaurantInfo.getRestTablenum()
    );
  }
}
