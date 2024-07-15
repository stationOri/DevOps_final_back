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
    private int restId;
    @Column(length = 200)
    private String restOpentime;

    private boolean restHoliday;

    private int restDeposit;
    @Enumerated(EnumType.STRING)
    private MoneyMethod restDepositMethod;
    @Column(length=100)
    private String restAddress;

    @Column(length=100)
    private String restIntro;
    private int restCloseday;
    @Column(length=20)
    private String restPhone;

    @Enumerated(EnumType.STRING)
    private PeriodType restReserveopenRule;
    @Enumerated(EnumType.STRING)
    private MinuteType restReserveInterval;

    private double restGrade;
    private int maxPpl;
    private int restTablenum;

    private String restPost;
    @Enumerated(EnumType.STRING)
    private ReservationType revWait;
    @Enumerated(EnumType.STRING)
    private RestWatingStatus restWaitingStatus;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rest_id")
    private Restaurant restaurant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="key_id")
    private Keyword keyword1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="key_id2")
    private Keyword keyword2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="key_id3")
    private Keyword keyword3;
}
