package org.example.oristationbackend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.MoneyMethod;
import org.example.oristationbackend.entity.type.OpenDay;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOpen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restaurantOpenId; // 식당 오픈 시간 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant; // 식당 id

    @Enumerated(EnumType.STRING)
    private OpenDay restDay; // 식당 영업 요일 -> MON(월요일) / TUE(화요일) / WED(수요일) / THU(목요일) / FRI(금요일) / SAT(토요일) / SUN(일요일) / HOL(공휴일)

    @Column(length=20)
    private String restOpen; // 식당 오픈 시간

    @Column(length=20)
    private String restClose; // 식당 마감 시간

    @Column(length=20)
    private String restLastorder; // 식당 마지막 주문 시간

    @Column(length=20)
    private String restBreakstart; // 식당 휴식 시작 시간

    @Column(length=20)
    private String restBreakend; // 식당 휴식 마감 시간

    public RestaurantOpen(Restaurant restaurant, OpenDay restDay) {
        this.restaurant = restaurant;
        this.restDay = restDay;
    }
}

