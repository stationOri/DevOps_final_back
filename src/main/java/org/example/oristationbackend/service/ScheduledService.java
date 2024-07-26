package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.example.oristationbackend.dto.user.SmsDto;
import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.type.ReservationStatus;
import org.example.oristationbackend.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduledService {
    private final ReservationRepository reservationRepository;
    private final SmsService smsService;
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
            SingleMessageSentResponse resp=smsService.sendOne(smsDto);
            System.out.println(resp.getStatusMessage());
        }
    }

}
