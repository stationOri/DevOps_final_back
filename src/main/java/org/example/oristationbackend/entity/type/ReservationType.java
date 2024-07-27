package org.example.oristationbackend.entity.type;

import lombok.Getter;

@Getter
public enum ReservationType {
    A("웨이팅"),
    B("예약"),
    C("웨이팅 및 예약");
    private final String description;

    ReservationType(String description) {
        this.description = description;
    }
}
