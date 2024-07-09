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
    private int peak_id;
    @Column(nullable = false)
    private Date date_start;
    @Column(nullable = false)
    private Date date_end;
    @Column(nullable = false)
    private Timestamp peak_opendate;
    @Column(nullable = false)
    private PeriodType peak_interval;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant;
}
