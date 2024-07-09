package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "reservedMenu")
@NoArgsConstructor
@AllArgsConstructor
public class ReservedMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rev_menu_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="res_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private RestaurantMenu restaurantMenu;

    private int amount;
}
