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
    private int tempHolidayId;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant;
}
