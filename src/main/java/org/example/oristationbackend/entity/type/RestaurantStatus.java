package org.example.oristationbackend.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

public enum RestaurantStatus {
    A("승인 대기"),
    B("승인 완료"),
    C("승인 거부");

    @Getter
    private final String description;

    RestaurantStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return name();
    }


    @JsonCreator
    public static RestaurantStatus fromValue(String value) {
        for (RestaurantStatus status : RestaurantStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 RestaurantStatus 값: " + value);
    }
}
