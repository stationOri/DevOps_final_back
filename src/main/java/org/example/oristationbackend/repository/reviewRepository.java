package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Favorite;
import org.example.oristationbackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface reviewRepository extends JpaRepository<Review, Integer> {
}
