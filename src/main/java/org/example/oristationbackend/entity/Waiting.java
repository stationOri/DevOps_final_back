package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.UserWaitingStatus;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Table(name = "waiting")
@NoArgsConstructor
@AllArgsConstructor
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int waitingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant;

    private int waitingNum;
    private Timestamp waitingDate;

    @Column(length=11)
    private String waitingPhone;

    @Column(length=20)
    private String waitingName;
    private int waitingPpl;

    @Enumerated(EnumType.STRING)
    private UserWaitingStatus userWaitingStatus;

}
