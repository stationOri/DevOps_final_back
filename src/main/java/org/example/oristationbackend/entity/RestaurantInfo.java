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
    private int rest_id;
    @Column(length = 100)
    private String rest_opentime;
    private int keyword;
    private boolean rest_holiday;
    private int rest_deposit;
    private MoneyMethod rest_deposit_method;
    @Column(length=100)
    private String rest_address;
    @Column(length=100)
    private String rest_intro;
    private int rest_closeday;
    @Column(length=11)
    private String rest_phone;
    private PeriodType rest_reserveopen_rule;
    private MinuteType rest_reserve_interval;
    private double rest_grade;
    private int max_ppl;
    private int rest_tablenum;
    private String rest_post;
    private ReservationType rev_wait;
    private RestWatingStatus rest_waiting_status;

    @OneToOne
    @MapsId
    @JoinColumn(name = "rest_id")
    private Restaurant restaurant;

}
