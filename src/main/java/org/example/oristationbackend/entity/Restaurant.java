package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.RestaurantStatus;

import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @OneToOne
    @MapsId
    @JoinColumn(name = "rest_id")
    private Login login;
    @Id
    private int restId; // 식당 id

    @Column(nullable = false, length=20)
    private String restOwner; // 점주 이름

    @Column(nullable = false, length=20)
    private String restPhone; // 점주 전화번호

    @Column(nullable = false, length=30)
    private String restName; // 식당 이름

    @Column(nullable = false, length=250)
    private String restPhoto; // 식당 사진

    @Column(nullable = false, length=250)
    private String restData; // 사업자등록증 경로

    @Column(nullable = false, length=10)
    private String restNum; // 사업자등록번호

    private boolean isBlocked; // 계정 금지 유무

    @Column(nullable = false)
    private Date joinDate; // 식당 가입일

    private Date quitDate; // 식당 탈퇴일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus restStatus; // 식당 상태 -> A("승인 대기") / B("승인 완료") / C("승인 거부")

    @Column(nullable = false)
    private boolean restIsopen; // 예약 오픈 여부

    @Column(length=30)
    private String restAccount; // 식당 계좌

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RestaurantInfo restaurantInfo;
}
