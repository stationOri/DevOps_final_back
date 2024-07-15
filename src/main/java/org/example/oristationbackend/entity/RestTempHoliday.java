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
    private int tempHolidayId; // 임시 휴무 id

    @Column(nullable = false)
    private Date startDate; // 임시 휴무 시작일

    @Column(nullable = false)
    private Date endDate; // 임시 휴무 종료일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant; // 식당 id
}
