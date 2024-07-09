package org.example.oristationbackend.entity.type;

import lombok.Getter;

public enum UserWaitingStatus {
    IN_QUEUE("대기등록"),
    WALKIN_REQUESTED("입장요청"),
    WALKIN("입장완료"),
    QUEUE_CANCELED("대기취소"),
    NOSHOW("노쇼");

    @Getter
    private final String description;

    UserWaitingStatus(String description) {
        this.description = description;
    }
}
