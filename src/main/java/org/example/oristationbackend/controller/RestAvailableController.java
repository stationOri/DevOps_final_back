package org.example.oristationbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestAvailableResDto;
import org.example.oristationbackend.entity.type.OpenDay;
import org.example.oristationbackend.service.RestAvailableResService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/res/available")
@RequiredArgsConstructor
public class RestAvailableController {

  private final RestAvailableResService restAvailableResService;

  // 특정 식당의 특정 요일의 예약 가능 시간 조회 API
  @GetMapping("/{restId}/times/{day}")
  public ResponseEntity<RestAvailableResDto> getAvailableTimes(@PathVariable("restId") int restId, @PathVariable("day") OpenDay day) {
    RestAvailableResDto dto = restAvailableResService.getAvailableTimes(restId, day);
    return ResponseEntity.ok(dto);
  }

  // 식당의 모든 요일의 예약 가능 시간 조회 API
  @GetMapping("/{restId}/times")
  public ResponseEntity<List<RestAvailableResDto>> getAllAvailableTimes(@PathVariable("restId") int restId) {
    List<RestAvailableResDto> dtos = Arrays.asList(
        restAvailableResService.getAvailableTimes(restId, OpenDay.MON),
        restAvailableResService.getAvailableTimes(restId, OpenDay.TUE),
        restAvailableResService.getAvailableTimes(restId, OpenDay.WED),
        restAvailableResService.getAvailableTimes(restId, OpenDay.THU),
        restAvailableResService.getAvailableTimes(restId, OpenDay.FRI),
        restAvailableResService.getAvailableTimes(restId, OpenDay.SAT),
        restAvailableResService.getAvailableTimes(restId, OpenDay.SUN),
        restAvailableResService.getAvailableTimes(restId, OpenDay.HOL)
    );
    return ResponseEntity.ok(dtos);
  }
}