package org.example.oristationbackend.service;

import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
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

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
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
    private final SmsService smsService;
    private final PaymentService paymentService;
    private final EmptyRepository emptyRepository;
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
        int nowCountReady = reservationRepository.countReservationsByStatus(userId, ReservationStatus.RESERVATION_READY);
        int nowCountAccepted = reservationRepository.countReservationsByStatus(userId, ReservationStatus.RESERVATION_ACCEPTED);

        int nowCount = nowCountReady + nowCountAccepted;
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
            RestReservationResDto restReservationResDto = new RestReservationResDto(reservation.getResId(),reservation.getUser().getUserName(), reservation.getUser().getUserId(), reservation.getRestaurant().getRestName(),
                    reservation.getRestaurant().getRestId(), reservation.getResId(), convertTimestampToString(reservation.getResDatetime()), convertTimestampToString(reservation.getReqDatetime()), reservation.getResNum(),
                    reservation.getStatus(), reservation.getRequest(), convertTimestampToString(reservation.getStatusChangedDate()), reservedMenus);
            result.add(restReservationResDto);
        }
        return result;
    }


    public static String convertTimestampToString(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
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
        List<Reservation> reservlist= reservationRepository.findReservationsByDateRangeAndStatus(restId,before,after,ReservationStatus.RESERVATION_ACCEPTED);
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
        LocalDateTime localDateTime2 = reservation.getResDatetime().toLocalDateTime();
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb= new StringBuilder();
        sb.append("[WaitMate]");
        sb.append(reservation.getRestaurant().getRestOwner());
        sb.append(" 사장님, ");
        sb.append(reservation.getRestaurant().getRestName());
        sb.append(" 식당에 ");
        sb.append(localDateTime.format(formatter));
        sb.append(" 예약 요청이 접수되었습니다. 빠른 시일 내로 예약 상태 변경 부탁드립니다. 감사합니다.");
        SmsDto smsDto=new SmsDto(reservation.getRestaurant().getRestPhone(),sb.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
        SingleMessageSentResponse resp=smsService.sendOne(smsDto); //해당 코드로 전송
        System.out.println(resp.getStatusMessage());
        return "success";
    }

    @Transactional(readOnly = false)
    public String changeStatus(int resId, ReservationStatus status) throws IamportResponseException, IOException {
        Reservation reservation = reservationRepository.findById(resId).orElseThrow(() -> new RuntimeException("reservation not found with reservation ID"));
        if(status.equals(ReservationStatus.RESERVATION_ACCEPTED)){
            if(reservation.getStatus()!=ReservationStatus.RESERVATION_READY){
                return "예약 승인이 가능한 상태가 아닙니다.";
            }
            reservationRepository.save(reservation.changeStatus(status));
            LocalDateTime localDateTime = reservation.getResDatetime().toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            StringBuilder sb= new StringBuilder();
            sb.append("[WaitMate]");
            sb.append(reservation.getUser().getUserName());
            sb.append("고객님, ");
            sb.append(reservation.getRestaurant().getRestName());
            sb.append(" 식당에 ");
            sb.append(localDateTime.format(formatter));
            sb.append(" 예약 요청하신 건이 승인되었습니다. 예약일에 방문 부탁드립니다. 감사합니다.");
            SmsDto smsDto=new SmsDto(reservation.getUser().getUserPhone(),sb.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
            SingleMessageSentResponse resp=smsService.sendOne(smsDto); //해당 코드로 전송
            System.out.println(resp.getStatusMessage()); // 상태 확인(정상: 정상 접수(이통사로 접수 예정))
        }else{
            LocalDateTime now = LocalDateTime.now();
            Timestamp currentTimestamp = Timestamp.valueOf(now);
            if(currentTimestamp.after(reservation.getResDatetime())){
                return "방문 및 노쇼 처리는 예약시간이 지난 후 가능합니다.";
            }
            if(status.equals(ReservationStatus.VISITED)){
                PayCancelDto cancelDto2 = new PayCancelDto("방문 후 취소",reservation.getPayment().getImpUid(),reservation.getPayment().getMerchantUid(), (int) (reservation.getPayment().getAmount()),reservation.getPayment().getAmount());
                paymentService.refundPayment(cancelDto2);
                paymentRepository.save(reservation.getPayment().refund(reservation.getPayment().getAmount()));
            }
            reservationRepository.save(reservation.changeStatus(status));

        }
        return  "success";
    }

    @Transactional(readOnly = false)
    public String changeCancel(int resId, ReservationStatus status, String reason) throws IamportResponseException, IOException {
        Reservation reservation = reservationRepository.findById(resId).orElseThrow(() -> new RuntimeException("reservation not found with reservation ID"));
        Payment payment = paymentRepository.findById(resId).orElseThrow(()-> new RuntimeException("payment not found with reservation ID"));

        if(!(reservation.getStatus().equals(ReservationStatus.RESERVATION_ACCEPTED)||reservation.getStatus().equals(ReservationStatus.RESERVATION_READY))){
            return "예약 취소가 가능한 상태가 아닙니다.";
        }
        reservationRepository.save(reservation.changeStatus(status));


        switch (status) {
            case RESERVATION_CANCELED_BYREST:
                PayCancelDto cancelDto = new PayCancelDto("식당 측 취소: "+reason,payment.getImpUid(),payment.getMerchantUid(),payment.getAmount(),payment.getAmount());
                paymentService.refundPayment(cancelDto);
                paymentRepository.save(payment.refund(payment.getAmount()));
                LocalDateTime localDateTime = reservation.getResDatetime().toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                StringBuilder sb= new StringBuilder();
                sb.append("[WaitMate]");
                sb.append(reservation.getUser().getUserName());
                sb.append("고객님, ");
                sb.append(reservation.getRestaurant().getRestName());
                sb.append(" 식당에 ");
                sb.append(localDateTime.format(formatter));
                sb.append(" 예약 요청하신 건이 식당 측에서 취소되었습니다. (사유: ");
                sb.append(reason);
                sb.append(")");
                SmsDto smsDto=new SmsDto(reservation.getUser().getUserPhone(),sb.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
                SingleMessageSentResponse resp=smsService.sendOne(smsDto); //해당 코드로 전송
                emptyNotice(reservation);
                System.out.println(resp.getStatusMessage()); // 상태 확인(정상: 정상 접수(이통사로 접수 예정))
                return  "success";
            case RESERVATION_CANCELED_BYUSER:
                double cal=0;
                int amount=payment.getAmount();
                if(reservation.getStatus().equals(ReservationStatus.RESERVATION_READY)){
                    cal=1;
                }else{
                    LocalDate today = LocalDate.now();
                    LocalDate dateFromTimestamp = reservation.getResDatetime().toLocalDateTime().toLocalDate();
                    long daysDifference = ChronoUnit.DAYS.between(dateFromTimestamp, today);
                    daysDifference=daysDifference*-1;
                    if(daysDifference>=7){
                        cal= 1;
                    }else if(daysDifference>=3){
                        cal= 0.5;
                    }else if(daysDifference>=1){
                        cal=0.1;
                    }else{
                        return "당일 및 예약일 이후 취소는 불가합니다.";
                    }
                }

                emptyNotice(reservation);
                PayCancelDto cancelDto2 = new PayCancelDto("사용자 측 취소",payment.getImpUid(),payment.getMerchantUid(), (int) (payment.getAmount()*cal),payment.getAmount());
                paymentService.refundPayment(cancelDto2);
                paymentRepository.save(payment.refund(payment.getAmount()));
                return "success";

            case RESERVATION_REJECTED:
                PayCancelDto cancelDto3 = new PayCancelDto("식당 측 예약 거절",payment.getImpUid(),payment.getMerchantUid(),payment.getAmount(),payment.getAmount());
                paymentService.refundPayment(cancelDto3);
                paymentRepository.save(payment.refund(payment.getAmount()));
                LocalDateTime localDateTime2 = reservation.getResDatetime().toLocalDateTime();
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                StringBuilder sb2= new StringBuilder();
                sb2.append("[WaitMate]");
                sb2.append(reservation.getUser().getUserName());
                sb2.append("고객님, ");
                sb2.append(reservation.getRestaurant().getRestName());
                sb2.append(" 식당에 ");
                sb2.append(localDateTime2.format(formatter2));
                sb2.append(" 예약 요청하신 건이 식당 측에서 거절되었습니다. 감사합니다.");

                SmsDto smsDto2=new SmsDto(reservation.getUser().getUserPhone(),sb2.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
                SingleMessageSentResponse resp2=smsService.sendOne(smsDto2); //해당 코드로 전송
                System.out.println(resp2.getStatusMessage()); // 상태 확인(정상: 정상 접수(이통사로 접수 예정))
                return "success";
            default:
                return "";
        }
    }
    private void emptyNotice(Reservation reservation) {
        Timestamp timestamp= reservation.getResDatetime();
        Date date = new Date(timestamp.getTime());
        Time time = new Time(timestamp.getTime());
        List<Empty> emptyList=emptyRepository.findByDateAndTime(date,time,reservation.getRestaurant());
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Empty empty : emptyList) {
            StringBuilder sb= new StringBuilder();
            sb.append("[WaitMate]");
            sb.append(empty.getUser().getUserName());
            sb.append("고객님, ");
            sb.append(reservation.getRestaurant().getRestName());
            sb.append(" 식당에 ");
            sb.append(localDateTime.format(formatter));
            sb.append(" 시간에 빈 테이블이 생겼습니다. 사이트에 접속하여 예약 부탁드립니다. 감사합니다.");
            SmsDto smsDto=new SmsDto(empty.getUser().getUserPhone(),sb.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
            SingleMessageSentResponse resp=smsService.sendOne(smsDto); //해당 코드로 전송
            empty.setStatus(true);
            emptyRepository.save(empty);
            System.out.println(resp.getStatusMessage());
        }
    }


}
