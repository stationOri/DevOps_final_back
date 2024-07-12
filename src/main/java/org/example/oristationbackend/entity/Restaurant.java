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
    private int restId;

    @Column(nullable = false, length=30)
    private String restName;
    @Column(nullable = false, length=20)
    private String restOwner;
    @Column(nullable = false, length=20)
    private String restPhone;
    private String restPhoto;
    @Column(nullable = false)
    private String restData;
    @Column(nullable = false, length=10)
    private String restNum;
    private boolean isBlocked;
    @Column(nullable = false)
    private Date joinDate;
    private Date quitDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus restStatus;
    @Column(nullable = false)
    private boolean restIsopen;
    @Column(length=30)
    private String restAccount;

    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RestaurantInfo restaurantInfo;


}
