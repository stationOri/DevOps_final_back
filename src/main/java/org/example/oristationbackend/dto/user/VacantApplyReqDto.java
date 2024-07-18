package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VacantApplyReqDto {
    private int userId; // 사용자 id
    private int restId; // 식당 id
    private String date; // 타겟 날짜
    private String time; // 타겟 시간
    private int people; // 예약 인원
}
