package org.example.oristationbackend.dto.restaurant;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TempHolidayResDto {
    private int tempHolidayId; // 임시 휴무 id
    private String startDate;
    private String endDate;
    private int restId;
}
