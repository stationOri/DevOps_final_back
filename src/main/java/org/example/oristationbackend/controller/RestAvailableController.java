package org.example.oristationbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestAvailableResDto;
import org.example.oristationbackend.service.RestAvailableResService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/res/available")
@RequiredArgsConstructor
public class RestAvailableController {

  private final RestAvailableResService restAvailableResService;

  // 특정 식당의 특정 날짜의 예약 가능 시간 조회
  @GetMapping("/{restId}/times/{date}")
  public ResponseEntity<RestAvailableResDto> getAvailableTimes(@PathVariable("restId") int restId, @PathVariable("date") String dateString) {
    LocalDate date = LocalDate.parse(dateString);
    RestAvailableResDto dto = restAvailableResService.getAvailableTimes(restId, date);
    return ResponseEntity.ok(dto);
  }
}