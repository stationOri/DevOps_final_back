package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.AdminReservationResDto;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.dto.user.UserReservationResDto;
import org.example.oristationbackend.entity.Payment;
import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.repository.PaymentRepository;
import org.example.oristationbackend.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

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

}
