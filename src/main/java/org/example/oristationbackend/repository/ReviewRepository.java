package org.example.oristationbackend.repository;

import org.example.oristationbackend.dto.restaurant.ReviewInfoDto;
import org.example.oristationbackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
