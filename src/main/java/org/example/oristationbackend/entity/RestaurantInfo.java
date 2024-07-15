package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInfo {
    @Id
    private int restId; // 식당 id

    private int restDeposit; // 예약금

    @Enumerated(EnumType.STRING)
    private MoneyMethod restDepositMethod; // 예약금 받는 방법 -> A(고정금) / B(메뉴 20%)

    @Column(length=100)
    private String restAddress; // 식당 주소

    @Column(length=100)
    private String restIntro; // 식당 소개

    @Column(length=20)
    private String restPhone; // 식당 전화번호

    @Enumerated(EnumType.STRING)
    private PeriodType restReserveopenRule; // 예약 오픈 단위 -> WEEK(일주일) / MONTH(한달)

    @Enumerated(EnumType.STRING)
    private MinuteType restReserveInterval; // 예약 받을 간격(분) -> ONEHOUR(1시간) / HALFHOUR(30분)

    private double restGrade; // 식당 별점

    private int maxPpl; // 예약 최대 인원(한 테이블 최대 인원)

    private int restTablenum; // 한 타임에 예약 가능한 테이블 수

    private String restPost; // 가게 공지

    @Enumerated(EnumType.STRING)
    private ReservationType revWait; // 예약 대기 방식 -> A(웨이팅) / B(예약) / C(둘다)

    @Enumerated(EnumType.STRING)
    private RestWatingStatus restWaitingStatus; // 웨이팅 오픈 여부 -> A(접수 중지) / B(접수 종료) / C(접수 중)

    @OneToOne
    @MapsId
    @JoinColumn(name = "rest_id")
    private Restaurant restaurant; // 식당 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="key_id")
    private Keyword keyword1; // 키워드1 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="key_id2")
    private Keyword keyword2; // 키워드2 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="key_id3")
    private Keyword keyword3; // 키워드3 id
}
