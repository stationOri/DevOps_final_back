package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeakListResDto {
    private int peakId; // 성수기 예약 id
    private String dateStart; // 성수기 시작 날짜
    private String dateEnd; // 성수기 마감 날짜
    private String peakOpendate; //성수기 예약 오픈 날짜
    private int restId; //식당 ID
}
