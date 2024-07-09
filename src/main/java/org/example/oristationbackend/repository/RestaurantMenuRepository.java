package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.RestaurantMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantMenuRepository extends JpaRepository<RestaurantMenu, Integer> {
}
