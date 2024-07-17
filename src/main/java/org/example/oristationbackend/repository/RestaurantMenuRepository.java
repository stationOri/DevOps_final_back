package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Integer> {
  List<RestaurantMenu> findAllByRestaurant_RestId(int restId);
  Optional<RestaurantMenu> findByMenuNameAndRestaurant_RestId(String menuName, int restId);
}
