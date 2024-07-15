package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name="emptytable")
public class Empty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emptyId; // 빈자리 알림 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant; // 식당 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user; // 사용자 id

    @Column(nullable = false)
    private Date date; // 타겟 날짜

    @Column(nullable = false)
    private Time time; // 타겟 시간

    @Column(nullable = false)
    private int people; // 예약 인원

    @Column(nullable = false)
    private boolean status; // 알림 전송 여부
}
