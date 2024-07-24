package org.example.oristationbackend.repository;

import org.example.oristationbackend.dto.restaurant.ReviewInfoDto;
import org.example.oristationbackend.dto.user.ReviewResDto;
import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.example.oristationbackend.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> , ReviewCustomRepository{

    @Query("SELECT new org.example.oristationbackend.dto.user.ReviewResDto(r.restaurant.restName,r.user.userNickname," +
            " r.reviewGrade, r.reviewData, r.reviewImg, r.reviewImg2, r.reviewImg3, r.reviewDate, r.likeNum, r.blind) " +
            "FROM Review r WHERE r.user.userId = :userId")
    List<ReviewResDto> findByUser_UserId(@Param("userId") int userId);

    @Query("SELECT new org.example.oristationbackend.dto.restaurant.ReviewInfoDto(r.reviewId, r.user.userNickname, r.reviewGrade, r.reviewData, r.reviewImg, r.reviewImg2, r.reviewImg3, r.reviewDate, r.likeNum, r.blind) " +
            "FROM Review r WHERE r.restaurant.restId = :restId")
    List<ReviewInfoDto> findReviewAndLikesByRestaurantId(@Param("restId") int restId);

    List<Review> findByRestaurant_RestId(int restId);
}
