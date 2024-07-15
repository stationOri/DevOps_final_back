package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantOpen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantOpenRepository extends JpaRepository<RestaurantOpen, Integer> {
  List<RestaurantOpen> findByRestaurant(Restaurant restaurant);
}
