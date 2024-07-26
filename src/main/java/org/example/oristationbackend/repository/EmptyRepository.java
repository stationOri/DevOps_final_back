package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Empty;
import org.example.oristationbackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface EmptyRepository extends JpaRepository<Empty, Integer> {
    @Query("SELECT e FROM Empty e WHERE e.date = :date AND e.time = :time AND e.restaurant = :restaurant AND e.status= false" )
    List<Empty> findByDateAndTime(@Param("date") Date date, @Param("time") Time time, @Param("restaurant")Restaurant restaurant);
}
