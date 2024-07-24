package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.RestTempHoliday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestTempHolidayRepository extends JpaRepository<RestTempHoliday, Integer> {
    List<RestTempHoliday> findByRestaurant_RestId(int restaurantId);
}
