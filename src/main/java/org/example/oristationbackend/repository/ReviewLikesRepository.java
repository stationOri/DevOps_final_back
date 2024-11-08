package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.Favorite;
import org.example.oristationbackend.entity.Review;
import org.example.oristationbackend.entity.ReviewLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewLikesRepository extends JpaRepository<ReviewLikes, Integer> {
    boolean existsByUser_UserIdAndReview_ReviewId(int userId, int reviewId);
    void deleteByReview_ReviewId(int reviewId);

    void deleteByReview(Review review);
    void deleteByUser_UserIdAndReview_ReviewId(int userId, int reviewId);
}
