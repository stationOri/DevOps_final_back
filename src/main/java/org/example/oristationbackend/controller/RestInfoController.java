package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.restaurant.RestResInfoDto;
import org.example.oristationbackend.service.RestResInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants/info")
public class RestInfoController {
  private final RestResInfoService restResInfoService;

  @Autowired
  public RestInfoController(RestResInfoService restResInfoService) {
    this.restResInfoService = restResInfoService;
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

}
