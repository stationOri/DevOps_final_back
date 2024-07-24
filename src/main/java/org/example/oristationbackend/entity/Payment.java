package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.PaymentStatus;

import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @OneToOne
    @MapsId
    @JoinColumn(name = "res_id")
    private Reservation reservation;
    @Id
    private int resId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private int amount;

    private int refund;

    private Timestamp statusChangedDate;

    private String imp_uid;

    private String merchant_uid;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
