package org.example.oristationbackend.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.AdminReservationResDto;
import org.example.oristationbackend.dto.admin.restAfterAcceptDto;
import org.example.oristationbackend.dto.restaurant.*;
import org.example.oristationbackend.dto.user.CombinedDto;
import org.example.oristationbackend.dto.user.ResRestCountDto;
import org.example.oristationbackend.dto.user.ReservationReqDto;
import org.example.oristationbackend.dto.user.UserReservationResDto;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.service.ReservationService;
import org.example.oristationbackend.service.RestAvailableResService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/reservations")
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

  // 관리자 예약 조회
  @GetMapping ("/admin")
  public ResponseEntity<List<AdminReservationResDto>> getAllReservationInfos() {
    List<AdminReservationResDto> resultreservations = reservationService.findAllReservations();
    if (resultreservations == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(resultreservations);
    }
  }

  @GetMapping("/rest/{restId}/time/{targetDate}")
  public List<String> getReservationTimes(
          @PathVariable("restId") int restId,
          @PathVariable("targetDate") String targetDate) {
    return reservationService.findReservedTime(restId, targetDate);
  }

  // 사용자 예약 내역 조회
  @GetMapping("/user/{userId}")
  public List<UserReservationResDto> getRecentReservations(@PathVariable(name = "userId") int userId) {
    return reservationService.getRecentReservations(userId);
  }

  // 사용자가 예약/방문한 식당 수 조회
  @GetMapping("/user/{userId}/counts")
  public ResRestCountDto getReservationCounts(@PathVariable(name = "userId") int userId) {
    return reservationService.getReservationCounts(userId);
  }

  //식당 예약 상세페이지
  @GetMapping("/reservation/rest/{restId}/{date}")
  public List<RestReservationResDto> getReservationByRestIdAndDate(@PathVariable(name = "restId") int restId,@PathVariable(name="date") LocalDate date) {
    DateRequestDto dateRequestDto = new DateRequestDto(restId, date);
    return reservationService.getReservationByRestIdAndDate(dateRequestDto);
  }
  @PostMapping("/reservationcheck")
  public String checkReservation(@RequestBody ReservationReqDto reservationReqDto) {
    return reservationService.checkAvailableRes(reservationReqDto);
  }
  @PostMapping("/reservation")
  public String addReservation(@RequestBody CombinedDto combinedDto) {
    return reservationService.saveReservation(combinedDto.getReservationReqDto(),combinedDto.getPayDto());
  }
  @PutMapping("/status/{resId}")
  public String changeState(@PathVariable(name = "resId") int resId,@RequestBody ReservationUpdateDto updateDto) throws IamportResponseException, IOException {
      return switch (updateDto.getStatus()) {
          case RESERVATION_ACCEPTED, VISITED, NOSHOW -> reservationService.changeStatus(resId, updateDto.getStatus());
          case RESERVATION_CANCELED_BYREST, RESERVATION_CANCELED_BYUSER, RESERVATION_REJECTED ->
                  reservationService.changeCancel(resId, updateDto.getStatus(),updateDto.getReason());
          default -> "예약 대기 상태로 변경은 불가합니다.";
      };
  }
  @PostMapping("/notice")
  public String notice(@RequestBody NoticeDto noticeDto) {
    return reservationService.notice(noticeDto);
  }

}
