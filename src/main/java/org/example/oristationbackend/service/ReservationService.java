package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.AdminReservationResDto;
import org.example.oristationbackend.dto.restaurant.DateRequestDto;
import org.example.oristationbackend.dto.restaurant.MenuDto;
import org.example.oristationbackend.dto.restaurant.RestReservationResDto;
import org.example.oristationbackend.dto.user.*;
import org.example.oristationbackend.entity.*;
import org.example.oristationbackend.entity.type.PaymentStatus;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.example.oristationbackend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private final RestaurantRepository restaurantRepository;
    private final RestaurantPeakRepository restaurantPeakRepository;
    private final UserRepository userRepository;
    private final RestaurantMenuRepository restaurantMenuRepository;
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
        List<Reservation> reservations = reservationRepository.findReservationsByDateRange(restId,startofday,endofday);
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

    public String checkAvailableRes(ReservationReqDto reservationReqDto) {
        if(!checkAvailableTable(reservationReqDto.getRestId(),reservationReqDto.getResNum(),reservationReqDto.getSelectedTime(),reservationReqDto.getSelectedDate())){
            return "해당 시간대 예약이 마감되었습니다.";
        }
        if(!checkAvailableDate(reservationReqDto.getRestId(), reservationReqDto.getSelectedDate())){
            return "해당 날짜는 성수기 예약 기간으로, 추후 오픈됩니다. 가게공지를 확인해주세요.";
        }

        return "available";
    }
    private boolean checkAvailableTable(int restId, int num, String selectedtime, String selectedDate){
        String dateTimeString = selectedDate + " " + selectedtime + ":00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        LocalDateTime before5Minutes = localDateTime.minusMinutes(5);
        LocalDateTime after5Minutes = localDateTime.plusMinutes(5);
        Timestamp before = Timestamp.valueOf(before5Minutes);
        Timestamp after = Timestamp.valueOf(after5Minutes);
        List<Reservation> reservlist= reservationRepository.findReservationsByDateRange(restId,before,after);
        int sum = reservlist.stream()
                .mapToInt(Reservation::getResNum)
                .sum();
        RestaurantInfo restaurantinfo= restaurantRepository.findById(restId).get().getRestaurantInfo();
        return restaurantinfo.getMaxPpl() >= (sum + num) && reservlist.size() < restaurantinfo.getRestTablenum();
    }
    private boolean checkAvailableDate(int restId, String selectedDate){
        List<RestaurantPeak> peakList= restaurantPeakRepository.findByRestaurant_RestId(restId);
        LocalDate targetDate = LocalDate.parse(selectedDate);
        for (RestaurantPeak peak : peakList) {
            LocalDate startDate = peak.getDateStart().toLocalDate();
            LocalDate endDate = peak.getDateEnd().toLocalDate();
            if (targetDate.isEqual(startDate) || targetDate.isEqual(endDate) ||
                    (targetDate.isAfter(startDate) && targetDate.isBefore(endDate))) {
                return false;
            }
        }
        return true;

    }
    @Transactional(readOnly = false)
    public String saveReservation(ReservationReqDto reservationReqDto, PayDto payDto) {
        if(paymentRepository.existsByImpUid(payDto.getImp_uid())||paymentRepository.existsByMerchantUid(payDto.getMerchant_uid())){
            return "동일한 결제 내역이 이미 존재합니다.";
        }

        User user = userRepository.findById(reservationReqDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Restaurant restaurant = restaurantRepository.findById(reservationReqDto.getRestId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        String dateTimeString = reservationReqDto.getSelectedDate() + " " + reservationReqDto.getSelectedTime() + ":00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        Timestamp resdate= Timestamp.valueOf(localDateTime);

        LocalDateTime now = LocalDateTime.now();
        Timestamp currentTimestamp = Timestamp.valueOf(now);

        Reservation reservation = new Reservation(0,currentTimestamp,resdate,currentTimestamp,reservationReqDto.getResNum(),0,ReservationStatus.RESERVATION_READY,
                reservationReqDto.getRequest(),user,restaurant,null,null);

        Reservation savedReservation = reservationRepository.save(reservation);
        List<MenuReservedDto> menuList = reservationReqDto.getMenulist();
        for (MenuReservedDto menuReservedDto : menuList) {
            System.out.println(menuReservedDto);
            RestaurantMenu restaurantMenu = restaurantMenuRepository.findById(menuReservedDto.getMenuId())
                    .orElseThrow(() -> new RuntimeException("Menu not found"));

            ReservedMenu reservedMenu = new ReservedMenu(0,savedReservation,restaurantMenu,menuReservedDto.getAmount());
            reservedMenuRepository.save(reservedMenu);
        }

        Payment payment = new Payment(savedReservation,0,user,(int) payDto.getAmount().longValue(),0,currentTimestamp,payDto.getImp_uid(),payDto.getMerchant_uid(), PaymentStatus.PAYMENT_DONE);
        paymentRepository.save(payment);
        return "success";
    }
}
