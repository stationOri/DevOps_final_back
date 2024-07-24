package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.RestaurantMenu;
import org.example.oristationbackend.entity.RestaurantPeak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantPeakRepository extends JpaRepository<RestaurantPeak, Integer> {
    List<RestaurantPeak> findByRestaurant_RestId(int restId);
    @Query("SELECT rp FROM RestaurantPeak rp WHERE rp.restaurant.restId = :restId AND rp.peakOpendate > CURRENT_TIMESTAMP")
    List<RestaurantPeak> findByRestaurant_RestIdAndDate(int restId);

}
