package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.repository.ReviewLikesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewLikeService {
    private final ReviewLikesRepository reviewLikesRepository;

    //리뷰 좋아요 삭제
    @Transactional
    public void reviewlikescancel(int reviewId, int userId) {
        reviewLikesRepository.deleteByUser_UserIdAndReview_ReviewId(reviewId,userId);
    }
}
