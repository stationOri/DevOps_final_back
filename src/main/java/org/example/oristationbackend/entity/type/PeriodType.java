package org.example.oristationbackend.entity.type;

import lombok.Getter;

@Getter
public enum PeriodType {
    WEEK("일주일"),
    MONTH("한달");
    private final String description;

    PeriodType(String description) {
        this.description = description;
    }
}
