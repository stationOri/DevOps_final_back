package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.RestaurantStatus;

import java.sql.Date;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    @OneToOne
    @MapsId
    @JoinColumn(name = "rest_id")
    private Login login;
    @Id
    private int rest_id;

    @Column(nullable = false, length=20)
    private String rest_name;
    @Column(nullable = false, length=20)
    private String rest_owner;
    @Column(nullable = false, length=11)
    private String rest_phone;
    private String rest_photo;
    @Column(nullable = false)
    private String rest_data;
    @Column(nullable = false, length=10)
    private String rest_num;
    private boolean is_blocked;
    @Column(nullable = false)
    private Date join_date;
    private Date quit_date;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus rest_status;
    @Column(nullable = false)
    private boolean rest_isopen;
    @Column(length=30)
    private String rest_account;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RestaurantInfo restaurantInfo;
}
