package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.type.MinuteType;
import org.example.oristationbackend.entity.type.MoneyMethod;
import org.example.oristationbackend.entity.type.PeriodType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestResInfoDto {
  private int restId; // 식당 id
  private PeriodType restReserveopenRule; // 예약 오픈 단위 -> WEEK(일주일) / MONTH(한달)
  private MinuteType restReserveInterval; // 예약 받을 간격(분) -> ONEHOUR(1시간) / HALFHOUR(30분)
  private MoneyMethod restDepositMethod; // 예약금 받는 방법 -> A(고정금) / B(메뉴 20%)
  private int restDeposit; // 예약금
  private int maxPpl; // 예약 최대 인원(한 테이블 최대 인원)
  private int restTablenum; // 한 타임에 예약 가능한 테이블 수

  public RestResInfoDto(Restaurant restaurant, RestaurantInfo restaurantInfo) {
    this.restId = restaurant.getRestId();
    this.restReserveopenRule = restaurantInfo.getRestReserveopenRule();
    this.restReserveInterval = restaurantInfo.getRestReserveInterval();
    this.restDepositMethod = restaurantInfo.getRestDepositMethod();
    this.restDeposit = restaurantInfo.getRestDeposit();
    this.maxPpl = restaurantInfo.getMaxPpl();
    this.restTablenum = restaurantInfo.getRestTablenum();
  }
}
