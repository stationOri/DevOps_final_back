package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findRestaurantByRestStatus(RestaurantStatus restaurantStatus);
}
