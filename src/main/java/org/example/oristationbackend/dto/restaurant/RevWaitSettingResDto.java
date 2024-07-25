package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.MinuteType;
import org.example.oristationbackend.entity.type.MoneyMethod;
import org.example.oristationbackend.entity.type.PeriodType;
import org.example.oristationbackend.entity.type.ReservationType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RevWaitSettingResDto {
    private ReservationType revWait; // 예약 대기 방식 -> A(웨이팅) / B(예약) / C(둘다)
    private PeriodType restReserveopenRule; // 예약 오픈 단위 -> WEEK(일주일) / MONTH(한달)
    private MinuteType restReserveInterval; // 예약 받을 간격(분) -> ONEHOUR(1시간) / HALFHOUR(30분)
    private int maxPpl; // 예약 최대 인원(한 테이블 최대 인원)
    private int restTablenum; // 한 타임에 예약 가능한 테이블 수
    private MoneyMethod restDepositMethod; // 예약금 받는 방법 -> A(고정금) / B(메뉴 20%)
    private int restDeposit; // 예약금

}
