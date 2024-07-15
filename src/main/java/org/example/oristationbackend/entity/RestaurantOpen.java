package org.example.oristationbackend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.MoneyMethod;
import org.example.oristationbackend.entity.type.OpenDay;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantOpen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int restaurantOpenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rest_id")
    private Restaurant restaurant;
    @Column(length=20)
    private String restOpen;
    @Column(length=20)
    private String restClose;
    @Column(length=20)
    private String restBreakstart;
    @Column(length=20)
    private String restBreakend;

    @Enumerated(EnumType.STRING)
    private OpenDay restDay;
    @Column(length=20)
    private String restLastorder;

}

