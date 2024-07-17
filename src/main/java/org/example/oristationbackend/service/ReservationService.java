package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.AdminReservationResDto;
import org.example.oristationbackend.dto.user.SearchResDto;
import org.example.oristationbackend.entity.Reservation;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.sql.Timestamp;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

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
}
