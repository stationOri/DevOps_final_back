package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.entity.Review;
import org.example.oristationbackend.repository.ReviewLikesRepository;
import org.example.oristationbackend.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewLikeService {
    private final ReviewLikesRepository reviewLikesRepository;
    private final ReviewRepository reviewRepository;

    //리뷰 좋아요 삭제
    @Transactional
    public void reviewlikescancel(int reviewId, int userId) {
        Review review=reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("Review not found with id: " +reviewId));
        int num = review.getLikeNum();
        review.setLikeNum(--num);
        reviewRepository.save(review);
        reviewLikesRepository.deleteByUser_UserIdAndReview_ReviewId(reviewId,userId);
    }
}
