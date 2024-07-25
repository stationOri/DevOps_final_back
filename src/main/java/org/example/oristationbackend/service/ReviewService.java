package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.ReviewInfoDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final S3Service s3Service;

    // 식당 페이지에서 리뷰 조회(사용자 화면)
    public List<ReviewRestDto> getReviewsByrestIdAnduserId(int restId,int userId){
        return reviewRepository.findReviewAndLikesByRestaurantIdAndUserId(restId,userId);
    }

    // 식당 페이지에서 리뷰 조회(식당 화면)
    public List<ReviewInfoDto> getReviewsByrestId(int restId){
        return reviewRepository.findReviewAndLikesByRestaurantId(restId);
    }


    // 사용자가 작성한 리뷰 조회
    public List<ReviewResDto> getReviewsByuserId(int userId){
        return reviewRepository.findByUser_UserId(userId);
    }

    // 리뷰 등록
    @Transactional
    public int addReview(ReviewReqDto reviewReqDto, MultipartFile file, MultipartFile file2, MultipartFile file3) throws IOException {
        User user = userRepository.findById(reviewReqDto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + reviewReqDto.getUserId()));
        Restaurant restaurant = restaurantRepository.findById(reviewReqDto.getRestId())
            .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with id: " + reviewReqDto.getRestId()));

        String fileUrl = file != null ? s3Service.uploadFile(file) : null;
        String fileUrl2 = file2 != null ? s3Service.uploadFile(file2) : null;
        String fileUrl3 = file3 != null ? s3Service.uploadFile(file3) : null;

        Review review = new Review();
        review.setReviewId(0);
        review.setReviewGrade(reviewReqDto.getReviewGrade());
        review.setReviewData(reviewReqDto.getReviewData());
        review.setReviewImg(fileUrl);
        review.setReviewImg2(fileUrl2);
        review.setReviewImg3(fileUrl3);
        review.setBlind(false);
        review.setReviewDate(new Timestamp(System.currentTimeMillis()));
        review.setLikeNum(0);
        review.setRestaurant(restaurant);
        review.setUser(user);
        review.setReviewLikes(null);

        return reviewRepository.save(review).getReviewId();
    }

    // 리뷰 좋아요
    @Transactional
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

    // 리뷰 삭제
    @Transactional
    public void deleteReview(int reviewId) {
        Review review=reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Review not found with id: " +reviewId));
        reviewLikesRepository.deleteByReview(review);
        reviewRepository.delete(review);
    }
}
