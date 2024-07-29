package org.example.oristationbackend.controller;


import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.UserReportResDto;
import org.example.oristationbackend.dto.restaurant.ReviewInfoDto;
import org.example.oristationbackend.dto.user.ReviewReqDto;
import org.example.oristationbackend.dto.user.ReviewResDto;
import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.example.oristationbackend.entity.Review;
import org.example.oristationbackend.entity.type.ReportStatus;
import org.example.oristationbackend.service.ReviewLikeService;
import org.example.oristationbackend.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;

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
    public ResponseEntity<?> addReview(
        @ModelAttribute ReviewReqDto reviewReqDto,
        @RequestParam(value = "file", required = false) MultipartFile file,
        @RequestParam(value = "file2", required = false) MultipartFile file2,
        @RequestParam(value = "file3", required = false) MultipartFile file3) {
        try {
            int result = reviewService.addReview(reviewReqDto, file, file2, file3);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }

    // 리뷰 좋아요
    @PostMapping("/like/review/{reviewId}/user/{userId}")
    public int likeReview(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId){
        return reviewService.likeReview(reviewId,userId);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable("reviewId")int reviewId){
        reviewService.deleteReview(reviewId);
    }

    //리뷰 좋아요 취소
    @DeleteMapping("/{reviewId}/user/{userId}")
    public void deleteReview(@PathVariable("reviewId") int reviewId, @PathVariable("userId") int userId){
        reviewLikeService.reviewlikescancel(userId,reviewId);
    }

}
