package org.example.oristationbackend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminReservationResDto {
    private int res_id; // 예약 id
    private int rest_id;
    private String rest_name;
    private int user_id; // 사용자 id
    private String req_datetime; // 예약 요청 시간
    private String res_datetime; // 예약 시간
    private int res_num; // 예약 인원
    private String status; // 예약 상태 -> RESERVATION_READY("예약대기"), RESERVATION_ACCEPTED("예약승인"), RESERVATION_REJECTED("예약거절"), RESERVATION_CANCELED("예약취소"), VISITED("방문"), NOSHOW("노쇼");
}
