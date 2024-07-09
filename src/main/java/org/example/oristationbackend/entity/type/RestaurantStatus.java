package org.example.oristationbackend.entity.type;

import lombok.Getter;

public enum RestaurantStatus {
    A("승인 대기"),
    B("승인 완료"),
    C("승인 거부");


    @Getter
    private final String description;

    RestaurantStatus(String description) {
        this.description = description;
    }
}
