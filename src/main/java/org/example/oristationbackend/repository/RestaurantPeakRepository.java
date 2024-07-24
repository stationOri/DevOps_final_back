package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.RestaurantMenu;
import org.example.oristationbackend.entity.RestaurantPeak;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantPeakRepository extends JpaRepository<RestaurantPeak, Integer> {
    List<RestaurantPeak> findByRestaurant_RestId(int restId);
}
