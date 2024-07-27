package org.example.oristationbackend.entity.type;

import lombok.Getter;

@Getter
public enum OpenDay {

    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일"),
    HOL("공휴일");

    private final String description;

    OpenDay(String description) {
        this.description = description;
    }
}
