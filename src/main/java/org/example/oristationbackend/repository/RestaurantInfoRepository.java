package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantInfoRepository extends JpaRepository<RestaurantInfo, Integer> {
    public RestaurantInfo findRestaurantInfoByRestId(int restId);
}
