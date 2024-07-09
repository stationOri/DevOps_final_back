package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.RestaurantMenu;
import org.example.oristationbackend.entity.RestaurantPeak;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantPeakRepository extends JpaRepository<RestaurantPeak, Integer> {
}
