package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.ReservationStatus;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int resId; // 예약 id

    private Timestamp reqDatetime; // 예약 요청 시간

    private Timestamp resDatetime; // 예약 시간

    private Timestamp status_changed_date; // 예약 상태 변경 시간

    private int resNum; // 예약 인원

    private int refund; // 환불될 예약금

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // 예약 상태 -> RESERVATION_READY("예약대기"), RESERVATION_ACCEPTED("예약승인"), RESERVATION_REJECTED("예약거절"), RESERVATION_CANCELED("예약취소"), VISITED("방문"), NOSHOW("노쇼");

    @Column(length = 200)
    private String request; // 요청사항

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 사용자 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    private Restaurant restaurant; // 식당 id

    @OneToOne(mappedBy = "reservation", fetch = FetchType.LAZY)
    private Payment payment; // 결제 정보

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY)
    private List<ReservedMenu> reservedMenus; // 예약된 메뉴
}
