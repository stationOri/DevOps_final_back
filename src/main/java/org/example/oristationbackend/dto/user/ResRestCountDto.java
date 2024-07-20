package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResRestCountDto {
    private int nowCount; // 진행중인 예약 수
    private int pastCount; // 완료된 예약 수
    private int restCount; // 방문한 식당 수
}
