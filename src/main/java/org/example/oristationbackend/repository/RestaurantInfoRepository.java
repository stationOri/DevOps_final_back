package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.RestaurantInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantInfoRepository extends JpaRepository<RestaurantInfo, Integer> {
    public RestaurantInfo findRestaurantInfoByRestId(int restId);
    Page<RestaurantInfo> findAll(Pageable pageable);
    List<RestaurantInfo> findAll();
    List<RestaurantInfo> findByRestIdIn(List<Integer> restIds);

    @Query("SELECT r FROM RestaurantInfo r JOIN r.restaurant res " +
        "WHERE res.restIsopen = true AND res.isBlocked = false AND res.restStatus = 'B'")
    Page<RestaurantInfo> findAllActiveRestaurants(Pageable pageable);
}
