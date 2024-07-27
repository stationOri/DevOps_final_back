package org.example.oristationbackend.entity.type;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PAYMENT_DONE("결제완료"),
    REFUND_READY("환불대기"),
    REFUND_DONE("환불완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
