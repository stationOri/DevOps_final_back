package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.PeriodType;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestaurantPeak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int peakId; // 성수기 예약 id

    @Column(nullable = false)
    private Date dateStart; // 성수기 시작 날짜

    @Column(nullable = false)
    private Date dateEnd; // 성수기 마감 날짜

    @Column(nullable = false)
    private Timestamp peakOpendate; // 성수기 예약 오픈 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant; // 식당 id
}
