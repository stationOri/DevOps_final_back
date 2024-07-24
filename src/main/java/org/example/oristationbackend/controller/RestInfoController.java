package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.restaurant.NoticeEditDto;
import org.example.oristationbackend.dto.restaurant.RestResInfoDto;
import org.example.oristationbackend.service.RestResInfoService;
import org.example.oristationbackend.service.RestaurantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants/info")
public class RestInfoController {
  private final RestResInfoService restResInfoService;
  private final RestaurantInfoService restaurantInfoService;

  @Autowired
  public RestInfoController(RestResInfoService restResInfoService, RestaurantInfoService restaurantInfoService) {
    this.restResInfoService = restResInfoService;
      this.restaurantInfoService = restaurantInfoService;
  }

  // 식당 id로 식당 예약 관련 정보 조회
  @GetMapping("/res/{restId}")
  public ResponseEntity<RestResInfoDto> getRestResInfoById(@PathVariable(name = "restId") int restId) {
    RestResInfoDto restResInfo = restResInfoService.findRestResInfoById(restId);
    if (restResInfo == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(restResInfo);
    }
  }

  // 예약및 웨이팅 여부
  @GetMapping("/revWait/{restId}")
  public ResponseEntity<String> getRestRevWaitById(@PathVariable(name = "restId") int restId) {
    String status = restaurantInfoService.FindRev_WaitingTypeByRestId(restId);
    return ResponseEntity.ok(status);
  }

  @GetMapping("/waitingstatus/{restId}")
  public ResponseEntity<String> getRestWaitById(@PathVariable(name = "restId") int restId) {
    String status = restaurantInfoService.FindWaitingStatusByRestId(restId);
    return ResponseEntity.ok(status);
  }

  // 식당 id로 식당 공지 수정
  @PutMapping("/notice/{restId}")
  public ResponseEntity<Void> updateRestPost(
      @PathVariable(name = "restId") int restId,
      @RequestBody NoticeEditDto noticeEditDto) {
    restaurantInfoService.updateRestPost(restId, noticeEditDto.getRestPost());
    return ResponseEntity.ok().build();
  }


}
