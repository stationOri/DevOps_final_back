package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.OpenDay;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantOpenDto {
    private int restaurantOpenId; // 식당 오픈 시간 id
    private int restId; // 식당 id
    private OpenDay restDay; // 식당 영업 요일
    private String restOpen; // 식당 오픈 시간
    private String restClose; // 식당 마감 시간
    private String restLastorder; // 식당 마지막 주문 시간
    private String restBreakstart; // 식당 휴식 시작 시간
    private String restBreakend; // 식당 휴식 마감 시간
}
