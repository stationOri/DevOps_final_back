package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.OpenDay;
import org.example.oristationbackend.entity.RestaurantOpen; // Import your RestaurantOpen entity

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

    // RestaurantOpen 객체를 매개변수로 받는 생성자
    public RestaurantOpenDto(RestaurantOpen restaurantOpen) {
        this.restaurantOpenId = restaurantOpen.getRestaurantOpenId();
        this.restId = restaurantOpen.getRestaurant().getRestId();
        this.restDay = restaurantOpen.getRestDay();
        this.restOpen = restaurantOpen.getRestOpen();
        this.restClose = restaurantOpen.getRestClose();
        this.restLastorder = restaurantOpen.getRestLastorder();
        this.restBreakstart = restaurantOpen.getRestBreakstart();
        this.restBreakend = restaurantOpen.getRestBreakend();
    }
}