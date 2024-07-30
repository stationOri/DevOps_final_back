package org.example.oristationbackend.service;

import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.example.oristationbackend.dto.user.PayCancelDto;
import org.example.oristationbackend.dto.user.SmsDto;
import org.example.oristationbackend.entity.Payment;
import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.example.oristationbackend.repository.PaymentRepository;
import org.example.oristationbackend.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ScheduledService {
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final SmsService smsService;
    private final PaymentService paymentService;
    @Scheduled(cron = "0 0 8 * * *") public void todayResNotice() {//초 분 시 일 월 요일
        Timestamp today = new Timestamp(System.currentTimeMillis()); // Get current time
        List<Reservation> reservations= reservationRepository.findReservationsByToday(today, ReservationStatus.RESERVATION_ACCEPTED);
        for (Reservation reservation : reservations) {
            LocalDateTime localDateTime = reservation.getResDatetime().toLocalDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            StringBuilder sb= new StringBuilder();
            sb.append("[WaitMate]");
            sb.append(reservation.getUser().getUserName());
            sb.append("고객님, ");
            sb.append(reservation.getRestaurant().getRestName());
            sb.append(" 식당에 방문하시는 날입니다! 예약 시간에 맞춰 식당에 방문해주세요. (예약 시간: ");
            sb.append(localDateTime.format(formatter));
            sb.append("/방문 인원: ");
            sb.append(reservation.getResNum());
            sb.append("명) 감사합니다. ");
            SmsDto smsDto=new SmsDto(reservation.getUser().getUserPhone(),sb.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
            //SingleMessageSentResponse resp=smsService.sendOne(smsDto);
            //System.out.println(resp.getStatusMessage());
        }
    }
    @Transactional(readOnly = false)
    @Scheduled(cron = "0 0 * * * *") public void restAcceptCancel() throws IamportResponseException, IOException {//초 분 시 일 월 요일
        Timestamp now = new Timestamp(System.currentTimeMillis()); // Get current time
        List<Reservation> reservations= reservationRepository.findByStatus(ReservationStatus.RESERVATION_READY);

        for (Reservation reservation : reservations) {
            reservationRepository.save(reservation.changeStatus(ReservationStatus.RESERVATION_CANCELED_BYADMIN));

            Optional<Payment> payment =  paymentRepository.findById(reservation.getResId());
            if(payment.isEmpty()){
                continue;
            }
            Payment pay=payment.get();
            Timestamp requestTime = reservation.getReqDatetime(); // 예약 요청 시간 가져오기
            long diffInMillis = now.getTime() - requestTime.getTime(); // 현재 시간과 요청 시간의 차이 계산
            long diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis); // 차이를 시간 단위로 변환

            if (diffInHours >= 12) {
                PayCancelDto cancelDto = new PayCancelDto("관리자 측 취소: ",pay.getImpUid(),pay.getMerchantUid(),pay.getAmount(),pay.getAmount());
                paymentService.refundPayment(cancelDto);
                paymentRepository.save(pay.refund(pay.getAmount()));
                LocalDateTime localDateTime = reservation.getResDatetime().toLocalDateTime();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                StringBuilder sb= new StringBuilder();
                sb.append("[WaitMate]");
                sb.append(reservation.getUser().getUserName());
                sb.append("고객님, ");
                sb.append(reservation.getRestaurant().getRestName());
                sb.append(" 식당에 ");
                sb.append(localDateTime.format(formatter));
                sb.append(" 예약 요청하신 건이 12시간이 지나 자동으로 취소되었습니다.");
                SmsDto smsDto=new SmsDto(reservation.getUser().getUserPhone(),sb.toString()); //SmsDto(전송할번호: 01012341234 형식, 내용: String)
                //SingleMessageSentResponse resp=smsService.sendOne(smsDto); //해당 코드로 전송
                //System.out.println(reservation.getResId());
            }
        }

    }
}
