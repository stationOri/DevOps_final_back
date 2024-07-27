package org.example.oristationbackend.entity.type;

import lombok.Getter;

@Getter
public enum MoneyMethod {
    A("고정금"),
    B("메뉴 20%");

    private final String description;

    MoneyMethod(String description) {
        this.description = description;
    }
}
