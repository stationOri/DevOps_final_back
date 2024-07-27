package org.example.oristationbackend.entity.type;

import lombok.Getter;

@Getter
public enum RestWatingStatus {
    A("접수 중지"),
    B("접수 종료"),
    C("접수 중");
    private final String description;

    RestWatingStatus(String description) {
        this.description = description;
    }
}
