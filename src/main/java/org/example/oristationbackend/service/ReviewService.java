package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.ReviewReqDto;
import org.example.oristationbackend.dto.user.ReviewResDto;
import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.Review;
import org.example.oristationbackend.entity.ReviewLikes;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.exception.DuplicateLikeException;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.example.oristationbackend.repository.ReviewLikesRepository;
import org.example.oristationbackend.repository.ReviewRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewLikesRepository reviewLikesRepository;

    public List<ReviewRestDto> getReviewsByrestId(int restId,int userId){
        return reviewRepository.findReviewAndLikesByRestaurantIdAndUserId(restId,userId);
    }
    public List<ReviewResDto> getReviewsByuserId(int userId){
        return reviewRepository.findByUser_UserId(userId);
    }
    @Transactional(readOnly = false)
    public int addReview(ReviewReqDto reviewReqDto) {
        User user = userRepository.findById(reviewReqDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found with id: " +reviewReqDto.getUserId()));
        Restaurant restaurant = restaurantRepository.findById(reviewReqDto.getRestId()).orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " +reviewReqDto.getRestId()));

        Review review = new Review(0,reviewReqDto.getReviewGrade(),reviewReqDto.getReviewData(),reviewReqDto.getReviewImg()
        ,reviewReqDto.getReviewImg2(),reviewReqDto.getReviewImg3(),false, new Timestamp(System.currentTimeMillis()),0,restaurant,user,null);
        return reviewRepository.save(review).getReviewId();
    }
    @Transactional(readOnly = false)
    public int likeReview(int reviewId, int userId) {
        if(reviewLikesRepository.existsByUser_UserIdAndReview_ReviewId(userId,reviewId) ){
            throw new DuplicateLikeException("이미 좋아요 한 리뷰입니다.");
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " +userId));
        Review review=reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Review not found with id: " +reviewId));

        ReviewLikes like= new ReviewLikes(0,review,user);
        review=review.like(like);
        like.setReview(review);
        reviewRepository.save(review);
        return reviewLikesRepository.save(like).getLikeId();
    }
    @Transactional(readOnly = false)
    public void deleteReview(int reviewId) {
        Review review=reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Review not found with id: " +reviewId));
        reviewLikesRepository.deleteByReview(review);
        reviewRepository.delete(review);
    }
}
