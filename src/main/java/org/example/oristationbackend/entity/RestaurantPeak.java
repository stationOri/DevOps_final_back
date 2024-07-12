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
    private int peakId;
    @Column(nullable = false)
    private Date dateStart;
    @Column(nullable = false)
    private Date dateEnd;
    @Column(nullable = false)
    private Timestamp peakOpendate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PeriodType peakInterval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant;
}
