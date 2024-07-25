package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.restaurant.*;
import org.example.oristationbackend.service.RestResInfoService;
import org.example.oristationbackend.service.RestaurantInfoService;
import org.example.oristationbackend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/restaurants/info")
public class RestInfoController {
  private final RestResInfoService restResInfoService;
  private final RestaurantInfoService restaurantInfoService;
  private final RestaurantService restaurantService;

  @Autowired
  public RestInfoController(RestResInfoService restResInfoService, RestaurantInfoService restaurantInfoService, RestaurantService restaurantService) {
    this.restResInfoService = restResInfoService;
    this.restaurantInfoService = restaurantInfoService;
    this.restaurantService = restaurantService;
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

  // 식당 id로 식당 계좌 수정
  @PutMapping("/account/{restId}")
  public ResponseEntity<Void> updateRestAccount(
      @PathVariable(name = "restId") int restId,
      @RequestBody AccountEditDto accountEditDto) {
    restaurantService.updateRestaurantAccount(restId, accountEditDto.getRestAccount());
    return ResponseEntity.ok().build();
  }
  @GetMapping("/account/{restId}")
  public String getRestAccount(@PathVariable(name = "restId") int restId) {
    return restaurantService.getRestarantAccount(restId);
  }

  //예약 및 웨이팅 받아오기
  @GetMapping("/setting/revwait/{restId}")
  public ResponseEntity<RevWaitSettingResDto> getRestRevWait(@PathVariable(name = "restId") int restId) {
    RevWaitSettingResDto setting = restaurantInfoService.findRevWaitSettingByRestId(restId);
    return ResponseEntity.ok(setting);
  }


  //예약 및 웨이팅 설정
  @PutMapping("/setting/revwait/{restId}")
  public int putRestRevWait(
          @PathVariable(name = "restId") int restId,
          @RequestBody RevWaitSettingResDto revwait
  ) {
    return restaurantInfoService.updateResWaitSetting(restId, revwait);
  }

  //식당 점포운영 불러오기
  @GetMapping("/setting/{restId}")
  public ResponseEntity<RestMainSettingResDto> getMaininfo(@PathVariable(name = "restId") int restId) {
    return ResponseEntity.ok(restaurantInfoService.findRestMainSettingByRestId(restId));
  }

  //식당 점포 운영 수정하기
  @PutMapping("/setting/{restId}")
  public ResponseEntity<?> putMaininfo(
          @PathVariable(name = "restId") int restId,
          @ModelAttribute RestMainSettingReqDto maininfo,
          @RequestParam(value = "file", required = false) MultipartFile file) {
    try {
      int result = restaurantInfoService.updateRestMainSetting(restId, maininfo, file);
      return ResponseEntity.ok(result);
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
    }
  }


}
