package org.example.oristationbackend.entity.type;

import lombok.Getter;

public enum ReservationStatus {
    RESERVATION_READY("예약대기"),
    RESERVATION_ACCEPTED("예약승인"),
    RESERVATION_REJECTED("예약거절"),
    RESERVATION_CANCELED("예약취소"),
    VISITED("방문"),
    NOSHOW("노쇼");

    @Getter
    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }
}
