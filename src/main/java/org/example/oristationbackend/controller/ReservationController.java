package org.example.oristationbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.AdminReservationResDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.restaurant.RestAvailableResDto;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.service.ReservationService;
import org.example.oristationbackend.service.RestAvailableResService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
  private final RestAvailableResService restAvailableResService;
  private final ReservationService reservationService;

  // 특정 식당의 특정 날짜의 예약 가능 시간 조회
  @GetMapping("/{restId}/times/{date}")
  public ResponseEntity<RestAvailableResDto> getAvailableTimes(@PathVariable("restId") int restId, @PathVariable("date") String dateString) {
    LocalDate date = LocalDate.parse(dateString);
    RestAvailableResDto dto = restAvailableResService.getAvailableTimes(restId, date);
    return ResponseEntity.ok(dto);
  }

  @GetMapping ("/reservation/admin")
  public ResponseEntity<List<AdminReservationResDto>> getAllReservationInfos() {
    List<AdminReservationResDto> resultreservations = reservationService.findAllReservations();
    if (resultreservations == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(resultreservations);
    }
  }

}
