package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.AdminReservationResDto;
import org.example.oristationbackend.dto.restaurant.DateRequestDto;
import org.example.oristationbackend.dto.restaurant.MenuDto;
import org.example.oristationbackend.dto.restaurant.RestReservationResDto;
import org.example.oristationbackend.dto.user.ResRestCountDto;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.dto.user.UserReservationResDto;
import org.example.oristationbackend.entity.*;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.example.oristationbackend.repository.PaymentRepository;
import org.example.oristationbackend.repository.ReservationRepository;
import org.example.oristationbackend.repository.ReservedMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.Timestamp;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final ReservedMenuRepository reservedMenuRepository;
    //예약 시간 조회
    public List<String> findReservedTime(int restId, String targetDate) {
        try {
            // 날짜 문자열을 Timestamp로 변환
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            java.util.Date parsedDate = dateFormat.parse(targetDate);
            Timestamp startTimestamp = new Timestamp(parsedDate.getTime());

            // targetDate의 날짜 끝 시간을 구하기 위해 하루를 더함
            Timestamp endTimestamp = new Timestamp(startTimestamp.getTime() + 24 * 60 * 60 * 1000 - 1);

            // 로그 추가
            System.out.println("Start Timestamp: " + startTimestamp);
            System.out.println("End Timestamp: " + endTimestamp);

            // 예약 조회
            List<Reservation> reservations = reservationRepository.findReservationsByDateRange(restId, startTimestamp, endTimestamp);

            // 로그 추가
            System.out.println("Reservations found: " + reservations.size());

            // 예약 시간 포맷팅 및 반환
            return reservations.stream()
                    .map(reservation -> formatTimestamp(reservation.getResDatetime()))
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Please use 'yyyy-MM-dd'");
        }
    }


    // 어드민 예약 조회
    public List<AdminReservationResDto> findAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(reservation -> new AdminReservationResDto(
                    reservation.getResId(),
                    reservation.getRestaurant().getRestId(),
                    reservation.getRestaurant().getRestName(),
                    reservation.getUser().getUserId(),
                        formatTimestamp(reservation.getReqDatetime()),
                        formatTimestamp(reservation.getResDatetime()),
                    reservation.getResNum(),
                    reservation.getStatus().getDescription()
                ))
                .collect(Collectors.toList());
    }

    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(timestamp);
        }
        return null;
    }

    // 사용자 예약 내역 조회
    public List<UserReservationResDto> getRecentReservations(int userId) {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        Timestamp oneYearAgoTimestamp = Timestamp.valueOf(oneYearAgo);

        List<Reservation> reservations = reservationRepository.findRecentReservationsByUserId(userId, oneYearAgoTimestamp);

        return reservations.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // 엔티티를 DTO로 변환
    private UserReservationResDto convertToDto(Reservation reservation) {
        return new UserReservationResDto(
            reservation.getResId(),
            reservation.getRestaurant().getRestId(),
            reservation.getResDatetime(),
            reservation.getRestaurant().getRestName(),
            reservation.getRestaurant().getRestPhoto(),
            reservation.getResNum(),
            reservation.getStatus(),
            reservation.getPayment().getStatus(),
            reservation.getRefund()
        );
    }

    // 사용자 예약/방문한 식당 수 조회
    public ResRestCountDto getReservationCounts(int userId) {
        List<ReservationStatus> nowStatuses = Arrays.asList(ReservationStatus.RESERVATION_READY, ReservationStatus.RESERVATION_ACCEPTED);
        int nowCount = reservationRepository.countNowReservations(userId, nowStatuses);
        int pastCount = reservationRepository.countPastReservations(userId, ReservationStatus.VISITED);
        int restCount = reservationRepository.countVisitedRestaurants(userId, ReservationStatus.VISITED);

        return new ResRestCountDto(nowCount, pastCount, restCount);
    }

    public List<RestReservationResDto> getReservationByRestIdAndDate(DateRequestDto dateRequestDto) {
        int restId = dateRequestDto.getRestId();
        LocalDate date = dateRequestDto.getDate(); //localdate에서 date로 바꾸기
        Timestamp startofday=Timestamp.valueOf(date.atStartOfDay());
        Timestamp endofday= Timestamp.valueOf(date.atTime(23, 59, 59));
        List<Reservation> reservations = reservationRepository.findByRestIDAndDate(restId,startofday,endofday);
        List<RestReservationResDto> result= new ArrayList<>();
        for (Reservation reservation : reservations) {
        List<MenuDto> reservedMenus = reservedMenuRepository.findByReservation_ResId(reservation.getResId());
            RestReservationResDto restReservationResDto = new RestReservationResDto(reservation.getUser().getUserName(), reservation.getUser().getUserId(), reservation.getRestaurant().getRestName(),
                    reservation.getRestaurant().getRestId(), reservation.getResId(), reservation.getResDatetime(), reservation.getReqDatetime(), reservation.getResNum(),
                    reservation.getStatus(), reservation.getRequest(), reservation.getStatusChangedDate(), reservedMenus);
            result.add(restReservationResDto);
        }
        return result;
    }

}
