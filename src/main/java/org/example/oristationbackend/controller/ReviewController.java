package org.example.oristationbackend.controller;


import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.UserReportResDto;
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
    @GetMapping("/rest/{restId}/user/{user_id}")
    public List<ReviewRestDto> getReviewByrestId(@PathVariable("restId") int restId, @PathVariable("user_id") int userId){
        return reviewService.getReviewsByrestId(restId,userId);
    }
    @GetMapping("/user/{userId}")
    public List<ReviewResDto> getReviewByUserId(@PathVariable("userId") int userId){
        return reviewService.getReviewsByuserId(userId);
    }
    @PostMapping("/review")
    public int addReview(@RequestBody ReviewReqDto reviewReqDto){
        return reviewService.addReview(reviewReqDto);
    }
    @PostMapping("/like/review/{reviewId}/user/{userId}")
    public int likeReview(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId){
        return reviewService.likeReview(reviewId,userId);
    }
    @DeleteMapping("/review/{reviewId}")
    public void deleteReview(@PathVariable("reviewId")int reviewId){
        reviewService.deleteReview(reviewId);
    }

}
