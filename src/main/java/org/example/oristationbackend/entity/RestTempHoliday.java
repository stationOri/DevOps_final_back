package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.sql.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RestTempHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int temp_holiday_id;
    @Column(nullable = false)
    private Date start_date;
    @Column(nullable = false)
    private Date end_date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant;
}
