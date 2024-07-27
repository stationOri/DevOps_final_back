package org.example.oristationbackend.entity.type;

import lombok.Getter;

@Getter
public enum MinuteType {
    ONEHOUR("60분"),
    HALFHOUR("30분");
    private final String description;

    MinuteType(String description) {
        this.description = description;
    }
}
