package org.example.oristationbackend.controller;


import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.UserReportResDto;
import org.example.oristationbackend.dto.restaurant.ReviewInfoDto;
import org.example.oristationbackend.dto.user.ReviewReqDto;
import org.example.oristationbackend.dto.user.ReviewResDto;
import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.example.oristationbackend.entity.Review;
import org.example.oristationbackend.entity.type.ReportStatus;
import org.example.oristationbackend.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    // 식당 페이지에서 리뷰 조회(사용자 화면)
    @GetMapping("/rest/{restId}/user/{user_id}")
    public List<ReviewRestDto> getReviewsByrestIdAnduserId(@PathVariable("restId") int restId, @PathVariable("user_id") int userId){
        return reviewService.getReviewsByrestIdAnduserId(restId,userId);
    }

    // 식당 페이지에서 리뷰 조회(식당 화면)
    @GetMapping("/rest/{restId}")
    public List<ReviewInfoDto> getReviewsByrestId(@PathVariable("restId") int restId){
        return reviewService.getReviewsByrestId(restId);
    }


    // 사용자가 작성한 리뷰 조회
    @GetMapping("/user/{userId}")
    public List<ReviewResDto> getReviewByUserId(@PathVariable("userId") int userId){
        return reviewService.getReviewsByuserId(userId);
    }

    // 리뷰 작성
    @PostMapping("/review")
    public int addReview(@RequestBody ReviewReqDto reviewReqDto){
        return reviewService.addReview(reviewReqDto);
    }

    // 리뷰 좋아요
    @PostMapping("/like/review/{reviewId}/user/{userId}")
    public int likeReview(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId){
        return reviewService.likeReview(reviewId,userId);
    }

    // 리뷰 삭제
    @DeleteMapping("/review/{reviewId}")
    public void deleteReview(@PathVariable("reviewId")int reviewId){
        reviewService.deleteReview(reviewId);
    }

}
