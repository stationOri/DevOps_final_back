package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.Review;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    List<Restaurant> findRestaurantByRestStatus(RestaurantStatus restaurantStatus);
    Optional<Restaurant> findByRestPhone(String phone);
    boolean existsByRestPhone(String phone);
    List<Restaurant> findRestaurantInfoByRestNameContaining(String restName);

    @Query(value = """
            SELECT ri.rest_id
            FROM restaurant_info ri
            WHERE ST_Distance_Sphere(
                    POINT(ri.lng, ri.lat),
                    POINT(:userLng, :userLat)
                ) <= :radius
            """, nativeQuery = true)
    List<Integer> findNearbyRestaurantIds(@Param("userLat") double userLat,
                                          @Param("userLng") double userLng,
                                          @Param("radius") double radius);

}
